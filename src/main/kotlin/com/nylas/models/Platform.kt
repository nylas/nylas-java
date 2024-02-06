package com.nylas.models

import com.squareup.moshi.Json

/**
 * Enum representing the different platforms supported by the redirect URI endpoints.
 */
enum class Platform {
  @Json(name = "web")
  WEB,

  @Json(name = "desktop")
  DESKTOP,

  @Json(name = "mobile")
  JS,

  @Json(name = "ios")
  IOS,

  @Json(name = "android")
  ANDROID,
}
