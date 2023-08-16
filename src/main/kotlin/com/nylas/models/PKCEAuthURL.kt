package com.nylas.models

/**
 * Class representing the object containing the OAuth 2.0 URL as well as the hashed secret.
 */
data class PKCEAuthURL(
  /**
   * The URL for hosted authentication
   */
  val url: String,
  /**
   * Server-side challenge used in the OAuth 2.0 flow
   */
  val secret: String,
  /**
   * SHA-256 hash of the secret
   */
  val secretHash: String,
)
