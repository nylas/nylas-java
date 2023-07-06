package com.nylas.models

import com.squareup.moshi.Json

data class UpdateRedirectUriRequest(
  @Json(name = "url")
  val url: String?,
  @Json(name = "platform")
  val platform: Platform?,
  @Json(name = "settings")
  val settings: RedirectUriSettings?,
) {
  class Builder {
    private var url: String? = null
    private var platform: Platform? = null
    private var settings: RedirectUriSettings? = null

    fun url(url: String) = apply { this.url = url }
    fun platform(platform: Platform) = apply { this.platform = platform }
    fun settings(settings: RedirectUriSettings) = apply { this.settings = settings }

    fun build() = UpdateRedirectUriRequest(url, platform, settings)
  }
}
