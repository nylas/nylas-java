package com.nylas.util

import java.nio.charset.StandardCharsets
import java.security.GeneralSecurityException
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * Utility class to validate the signature of a webhook request.
 */
object WebhookRequestValidator {

  /**
   * Validates the signature of a webhook request
   *
   * @param payload the raw payload of the request
   * @param webhookSecret your webhook secret key
   * @param expectedSignature the expected signature of the request. (Header "X-Nylas-Signature")
   * @return true if the request is valid
   */
  @JvmStatic
  fun isRequestValid(payload: String, webhookSecret: String, expectedSignature: String): Boolean {
    return try {
      val expectedBytes = hexStringToByteArray(expectedSignature)
      val mac = Mac.getInstance("HmacSHA256")
      val secretKeySpec = SecretKeySpec(webhookSecret.toByteArray(StandardCharsets.UTF_8), "HmacSHA256")
      mac.init(secretKeySpec)
      val sigBytes = mac.doFinal(payload.toByteArray(StandardCharsets.UTF_8))
      expectedBytes contentEquals sigBytes
    } catch (e: GeneralSecurityException) {
      throw RuntimeException(e)
    }
  }

  /**
   * Converts a hex string to a byte array
   *
   * @param s the hex string to convert
   * @return the byte array
   */
  private fun hexStringToByteArray(s: String): ByteArray {
    val len = s.length
    val data = ByteArray(len / 2)
    for (i in 0 until len step 2) {
      data[i / 2] = ((Character.digit(s[i], 16) shl 4) + Character.digit(s[i + 1], 16)).toByte()
    }
    return data
  }
}
