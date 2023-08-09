package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing a request to create a redirect uri.
 */
data class CreateRedirectUriRequest(
  /**
   * Redirect URL.
   */
  @Json(name = "url")
  val url: String,
  /**
   * Platform identifier.
   */
  @Json(name = "platform")
  val platform: Platform,
  /**
   * Optional settings for the redirect uri.
   */
  @Json(name = "settings")
  val settings: RedirectUriSettings?,
) {
  /**
   * Builder for [CreateRedirectUriRequest].
   * @property url Redirect URL.
   * @property platform Platform identifier.
   */
  data class Builder(
    private val url: String,
    private val platform: Platform,
  ) {
    private var settings: RedirectUriSettings? = null

    /**
     * Set the settings for the redirect uri.
     * @param settings Settings for the redirect uri
     * @return This builder
     */
    fun settings(settings: RedirectUriSettings) = apply { this.settings = settings }

    /**
     * Build the [CreateRedirectUriRequest].
     * @return The built [CreateRedirectUriRequest]
     */
    fun build() = CreateRedirectUriRequest(url, platform, settings)
  }
}
