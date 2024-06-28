package com.nylas.util

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class WebhookRequestValidatorTest {

  @Test
  fun isRequestValid() {
    val webhookSecret = "UfZWFrkjsbg5bxLbax-y"
    val payload = """{"specversion":"1.0","type":"grant.deleted","source":"/nylas/system","id":"Kkx2QQB7TWjWM3KXRQQn562394","time":1717162394,"webhook_delivery_attempt":1,"data":{"application_id":"a7966b5a-9bc2-44b7-9895-4e2b281ce6be","object":{"code":25013,"grant_id":"4c22c3c5-d163-48b6-ad95-45fedc879683","integration_id":"b7dd6e6e-af6a-463b-92fa-be35c2683f5c","metric":1934,"provider":"google"}}}"""

    // When the signature header value is valid
    val validSigHeader = "a4534f7e68dcd54f0621f7539ddd5a0d14a9a46ace9b68a4cfd9c99b4b6b89cc"
    // Then isRequestValid() returns true
    assertTrue(WebhookRequestValidator.isRequestValid(payload, webhookSecret, validSigHeader))

    // When the signature header value is invalid
    val invalidSigHeader = "a4534f7e68dcd54f0621f7539ddd5a0d14a9a46ace9b68a4cfd9c99b4b6b89ca"
    // Then isRequestValid() returns false
    assertFalse(WebhookRequestValidator.isRequestValid(payload, webhookSecret, invalidSigHeader))
  }
}
