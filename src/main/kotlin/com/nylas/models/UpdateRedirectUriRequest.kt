package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas update redirect uri request
 */
data class UpdateRedirectUriRequest(
  /**
   * Redirect URL.
   */
  @Json(name = "url")
  val url: String? = null,
  /**
   * Platform identifier.
   */
  @Json(name = "platform")
  val platform: Platform? = null,
  /**
   * Settings for the redirect uri.
   */
  @Json(name = "settings")
  val settings: RedirectUriSettings? = null,
) {
  class Builder {
    private var url: String? = null
    private var platform: Platform? = null
    private var settings: RedirectUriSettings? = null

    /**
     * Update the redirect URL.
     * @param url Redirect URL.
     * @return This builder
     */
    fun url(url: String) = apply { this.url = url }

    /**
     * Update the platform identifier.
     * @param platform Platform identifier.
     * @return This builder
     */
    fun platform(platform: Platform) = apply { this.platform = platform }

    /**
     * Update the settings for the redirect uri.
     * @param settings Settings for the redirect uri
     * @return This builder
     */
    fun settings(settings: RedirectUriSettings) = apply { this.settings = settings }

    /**
     * Build the [UpdateRedirectUriRequest].
     * @return The built [UpdateRedirectUriRequest]
     */
    fun build() = UpdateRedirectUriRequest(url, platform, settings)
  }
}
