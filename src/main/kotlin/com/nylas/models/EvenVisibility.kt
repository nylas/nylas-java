package com.nylas.models

import com.squareup.moshi.Json

enum class EvenVisibility {
  @Json(name = "public")
  PUBLIC,

  @Json(name = "private")
  PRIVATE,
}
