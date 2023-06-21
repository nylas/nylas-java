package com.nylas.models

import com.squareup.moshi.Json

data class CreateRedirectUriRequest(
  @Json(name = "url")
  val url: String,
  @Json(name = "platform")
  val platform: Platform,
  @Json(name = "settings")
  val settings: RedirectUriSettings?,
) {
  data class Builder(
    private val url: String,
    private val platform: Platform,
  ) {
    private var settings: RedirectUriSettings? = null

    fun settings(settings: RedirectUriSettings) = apply { this.settings = settings }

    fun build() = CreateRedirectUriRequest(url, platform, settings)
  }
}
