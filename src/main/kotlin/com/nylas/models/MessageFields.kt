package com.nylas.models

import com.squareup.moshi.Json

enum class MessageFields {
  @Json(name = "standard")
  STANDARD,

  @Json(name = "include_headers")
  INCLUDE_HEADERS,
}
