package com.nylas.models

import com.squareup.moshi.Json

enum class GrantStatus {
  @Json(name = "valid")
  VALID,
  @Json(name = "invalid")
  INVALID,
}