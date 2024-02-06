package com.nylas.models

import com.squareup.moshi.Json

/**
 * Enum for the access type of the authentication URL.
 */
enum class AccessType {
  @Json(name = "offline")
  OFFLINE,

  @Json(name = "online")
  ONLINE,
}
