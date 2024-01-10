package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing the object containing the OAuth 2.0 URL as well as the hashed secret.
 */
data class PKCEAuthURL(
  /**
   * The URL for hosted authentication
   */
  @Json(name = "url")
  val url: String,
  /**
   * Server-side challenge used in the OAuth 2.0 flow
   */
  @Json(name = "secret")
  val secret: String,
  /**
   * SHA-256 hash of the secret
   */
  @Json(name = "secret_hash")
  val secretHash: String,
)
