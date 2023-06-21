package com.nylas.models

import com.squareup.moshi.Json

data class RedirectUri(
  @Json(name = "id")
  val id: String,
  @Json(name = "url")
  val url: String,
  @Json(name = "platform")
  val platform: Platform,
  @Json(name = "settings")
  val settings: RedirectUriSettings?,
)
