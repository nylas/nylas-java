package com.nylas.models

import com.squareup.moshi.Json

enum class MessageFields {
  @Json(name = "standard")
  STANDARD,

  @Json(name = "include_headers")
  INCLUDE_HEADERS,

  @Json(name = "include_tracking_options")
  INCLUDE_TRACKING_OPTIONS,

  @Json(name = "raw_mime")
  RAW_MIME,
}
