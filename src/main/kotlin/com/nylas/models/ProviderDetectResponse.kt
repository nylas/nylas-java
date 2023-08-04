package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing the Nylas provider detect response.
 */
data class ProviderDetectResponse(
  /**
   * Email provided for autodetection
   */
  @Json(name = "email_address")
  val emailAddress: String,
  /**
   * Whether the provider was detected
   */
  @Json(name = "detected")
  val detected: Boolean,
  /**
   * Detected provider
   */
  @Json(name = "provider")
  val provider: String?,
  /**
   * Provider type (if IMAP provider detected displays the IMAP provider)
   */
  @Json(name = "type")
  val type: String?,
)
