package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Redirect URI object
 */
data class RedirectUri(
  /**
   * Globally unique object identifier
   */
  @Json(name = "id")
  val id: String,
  /**
   * Redirect URL
   */
  @Json(name = "url")
  val url: String,
  /**
   * Platform identifier
   */
  @Json(name = "platform")
  val platform: Platform,
  /**
   * Configuration settings
   */
  @Json(name = "settings")
  val settings: RedirectUriSettings?,
)
