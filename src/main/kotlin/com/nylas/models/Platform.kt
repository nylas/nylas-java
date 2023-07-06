package com.nylas.models

import com.squareup.moshi.Json

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
