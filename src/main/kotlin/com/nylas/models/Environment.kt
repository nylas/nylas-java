package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing the different Nylas environments.
 */
enum class Environment {
  @Json(name = "production")
  PRODUCTION,

  @Json(name = "staging")
  STAGING,
}
