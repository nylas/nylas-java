package com.nylas.models

import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.Signature
import java.util.Base64
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ServiceAccountSignerTests {
  @Test
  fun `canonical json sorts object keys recursively without extra whitespace`() {
    val json = ServiceAccountSigner.canonicalJson(
      mapOf(
        "z" to mapOf("b" to 1, "a" to 2),
        "items" to listOf(mapOf("d" to true, "c" to "value")),
        "a" to 0,
      ),
    )

    assertEquals("""{"a":0,"items":[{"c":"value","d":true}],"z":{"a":2,"b":1}}""", json)
  }

  @Test
  fun `canonical json uses sdk json names for request models`() {
    val json = ServiceAccountSigner.canonicalJson(DomainVerificationRequest(DomainVerificationType.SPF))

    assertEquals("""{"type":"spf"}""", json)
  }

  @Test
  fun `build headers is deterministic with fixed timestamp and nonce`() {
    val keyPair = testKeyPair()
    val signer = ServiceAccountSigner(keyPair.private, "kid-123")
    val body = mapOf("name" to "My domain", "domain_address" to "mail.example.com")

    val first = signer.buildHeaders("POST", "/v3/admin/domains", body, timestamp = 1700000000, nonce = "nonce123456789012345")
    val second = signer.buildHeaders("POST", "/v3/admin/domains", body, timestamp = 1700000000, nonce = "nonce123456789012345")

    assertEquals(first, second)
    assertEquals("kid-123", first.headers["X-Nylas-Kid"])
    assertEquals("1700000000", first.headers["X-Nylas-Timestamp"])
    assertEquals("nonce123456789012345", first.headers["X-Nylas-Nonce"])
    assertEquals("""{"domain_address":"mail.example.com","name":"My domain"}""", first.serializedJsonBody)
    assertNotNull(first.headers["X-Nylas-Signature"])
    assertTrue(verifySignature(keyPair, signingEnvelope(first, "/v3/admin/domains", "post"), first.headers.getValue("X-Nylas-Signature")))
  }

  @Test
  fun `get requests include no canonical body`() {
    val signer = ServiceAccountSigner(testKeyPair().private, "kid")
    val result = signer.buildHeaders("GET", "/v3/admin/domains", timestamp = 1, nonce = "n".repeat(20))

    assertEquals(null, result.serializedJsonBody)
    assertTrue(result.headers.getValue("X-Nylas-Signature").isNotBlank())
  }

  private fun signingEnvelope(result: ServiceAccountSigningResult, path: String, method: String): ByteArray {
    val envelope = linkedMapOf<String, Any>(
      "method" to method,
      "nonce" to result.headers.getValue("X-Nylas-Nonce"),
      "path" to path,
      "timestamp" to result.headers.getValue("X-Nylas-Timestamp").toLong(),
    )
    result.serializedJsonBody?.let { envelope["payload"] = it }
    return ServiceAccountSigner.canonicalJson(envelope).toByteArray(Charsets.UTF_8)
  }

  private fun verifySignature(keyPair: KeyPair, message: ByteArray, signature: String): Boolean {
    val verifier = Signature.getInstance("SHA256withRSA")
    verifier.initVerify(keyPair.public)
    verifier.update(message)
    return verifier.verify(Base64.getDecoder().decode(signature))
  }

  private fun testKeyPair(): KeyPair {
    val generator = KeyPairGenerator.getInstance("RSA")
    generator.initialize(2048)
    return generator.generateKeyPair()
  }
}
