package com.nylas.models

import com.squareup.moshi.Json

enum class AccessType {
  @Json(name = "offline")
  OFFLINE,

  @Json(name = "online")
  ONLINE,
}
