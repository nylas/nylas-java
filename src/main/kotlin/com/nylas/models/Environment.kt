package com.nylas.models

import com.squareup.moshi.Json

enum class Environment {
  @Json(name = "production")
  PRODUCTION,

  @Json(name = "staging")
  STAGING,
}
