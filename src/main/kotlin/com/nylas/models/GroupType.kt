package com.nylas.models

import com.squareup.moshi.Json

enum class GroupType {
  @Json(name = "user")
  USER,

  @Json(name = "system")
  SYSTEM,

  @Json(name = "other")
  OTHER,
}
