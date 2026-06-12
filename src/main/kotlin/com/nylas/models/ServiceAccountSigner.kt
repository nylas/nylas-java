package com.nylas.models

import com.nylas.util.JsonHelper
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.SecureRandom
import java.security.Signature
import java.security.interfaces.RSAPrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.Base64

/**
 * Builds Nylas Service Account request-signing headers for organization admin APIs.
 *
 * For POST, PUT, and PATCH requests, [buildHeaders] also returns the canonical JSON
 * body string that must be sent on the wire for the signature to match.
 */
class ServiceAccountSigner(privateKey: PrivateKey, private val privateKeyId: String) {
  private val privateKey: RSAPrivateKey

  init {
    require(privateKey is RSAPrivateKey) { "Private key must be RSA" }
    this.privateKey = privateKey
  }

  /**
   * Create a signer from an RSA private key in PKCS#8 PEM format.
   *
   * @param privateKeyPem RSA private key PEM text.
   * @param privateKeyId Value to send as `X-Nylas-Kid`.
   */
  constructor(privateKeyPem: String, privateKeyId: String) : this(loadPrivateKeyFromPem(privateKeyPem), privateKeyId)

  /**
   * Build the four required Nylas Service Account signing headers.
   *
   * @param method HTTP method.
   * @param path Request path including `/v3`, without query string.
   * @param body Request body object for POST, PUT, and PATCH requests.
   * @param timestamp Optional Unix timestamp in seconds. Defaults to current time.
   * @param nonce Optional nonce. Defaults to a secure random alphanumeric nonce.
   * @return Signing headers and optional canonical JSON body.
   */
  @JvmOverloads
  fun buildHeaders(
    method: String,
    path: String,
    body: Any? = null,
    timestamp: Long? = null,
    nonce: String? = null,
  ): ServiceAccountSigningResult {
    val normalizedMethod = method.lowercase()
    val timestampSeconds = timestamp ?: (System.currentTimeMillis() / 1000)
    val nonceValue = nonce ?: generateNonce()
    val canonicalBody = if (normalizedMethod in BODY_METHODS && body != null) canonicalJson(body) else null
    val envelope = linkedMapOf<String, Any>(
      "method" to normalizedMethod,
      "nonce" to nonceValue,
      "path" to path,
      "timestamp" to timestampSeconds,
    )
    if (canonicalBody != null) {
      envelope["payload"] = canonicalBody
    }

    val signature = Signature.getInstance("SHA256withRSA")
    signature.initSign(privateKey)
    signature.update(canonicalJson(envelope).toByteArray(Charsets.UTF_8))
    val encodedSignature = Base64.getEncoder().encodeToString(signature.sign())

    return ServiceAccountSigningResult(
      headers = mapOf(
        "X-Nylas-Kid" to privateKeyId,
        "X-Nylas-Nonce" to nonceValue,
        "X-Nylas-Timestamp" to timestampSeconds.toString(),
        "X-Nylas-Signature" to encodedSignature,
      ),
      serializedJsonBody = canonicalBody,
    )
  }

  companion object {
    private val BODY_METHODS = setOf("post", "put", "patch")
    private val NONCE_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray()
    private const val NONCE_LENGTH = 20
    private val secureRandom = SecureRandom()

    /**
     * Deterministic JSON with object keys sorted recursively and no extra whitespace.
     */
    fun canonicalJson(value: Any?): String {
      return when (value) {
        null -> "null"
        is Map<*, *> ->
          value.entries
            .sortedBy { it.key.toString() }
            .joinToString(prefix = "{", postfix = "}", separator = ",") { (key, itemValue) ->
              "${jsonString(key.toString())}:${canonicalJson(itemValue)}"
            }
        is Iterable<*> -> value.joinToString(prefix = "[", postfix = "]", separator = ",") { canonicalJson(it) }
        is Array<*> -> value.joinToString(prefix = "[", postfix = "]", separator = ",") { canonicalJson(it) }
        is String -> jsonString(value)
        is Number, is Boolean -> JsonHelper.moshi().adapter(Any::class.java).toJson(value)
        else -> {
          val jsonValue = JsonHelper.moshi().adapter(value.javaClass).toJsonValue(value)
          canonicalJson(jsonValue)
        }
      }
    }

    fun generateNonce(length: Int = NONCE_LENGTH): String {
      require(length > 0) { "Nonce length must be positive" }
      return CharArray(length) {
        NONCE_ALPHABET[secureRandom.nextInt(NONCE_ALPHABET.size)]
      }.concatToString()
    }

    fun loadPrivateKeyFromPem(privateKeyPem: String): RSAPrivateKey {
      val isPkcs1 = privateKeyPem.contains("BEGIN RSA PRIVATE KEY")
      val keyBytes = privateKeyPem
        .replace("-----BEGIN RSA PRIVATE KEY-----", "")
        .replace("-----END RSA PRIVATE KEY-----", "")
        .replace("-----BEGIN PRIVATE KEY-----", "")
        .replace("-----END PRIVATE KEY-----", "")
        .replace("\\s".toRegex(), "")
      val decoded = Base64.getDecoder().decode(keyBytes)
      val pkcs8Bytes = if (isPkcs1) pkcs1ToPkcs8(decoded) else decoded
      val key = KeyFactory.getInstance("RSA").generatePrivate(PKCS8EncodedKeySpec(pkcs8Bytes))
      require(key is RSAPrivateKey) { "Private key must be RSA" }
      return key
    }

    private fun jsonString(value: String): String {
      return JsonHelper.moshi().adapter(String::class.java).toJson(value)
    }

    private fun pkcs1ToPkcs8(pkcs1: ByteArray): ByteArray {
      val version = byteArrayOf(0x02, 0x01, 0x00)
      val rsaEncryptionAlgorithm = byteArrayOf(
        0x30, 0x0d,
        0x06, 0x09,
        0x2a, 0x86.toByte(), 0x48, 0x86.toByte(), 0xf7.toByte(), 0x0d, 0x01, 0x01, 0x01,
        0x05, 0x00,
      )
      return derSequence(version, rsaEncryptionAlgorithm, derOctetString(pkcs1))
    }

    private fun derSequence(vararg parts: ByteArray): ByteArray {
      val body = parts.fold(byteArrayOf()) { acc, part -> acc + part }
      return byteArrayOf(0x30) + derLength(body.size) + body
    }

    private fun derOctetString(value: ByteArray): ByteArray {
      return byteArrayOf(0x04) + derLength(value.size) + value
    }

    private fun derLength(length: Int): ByteArray {
      if (length < 128) {
        return byteArrayOf(length.toByte())
      }

      val bytes = mutableListOf<Byte>()
      var remaining = length
      while (remaining > 0) {
        bytes.add(0, (remaining and 0xff).toByte())
        remaining = remaining ushr 8
      }
      return byteArrayOf((0x80 or bytes.size).toByte()) + bytes.toByteArray()
    }
  }
}

/**
 * Result of signing a Nylas Service Account request.
 */
data class ServiceAccountSigningResult(
  val headers: Map<String, String>,
  val serializedJsonBody: String? = null,
)
