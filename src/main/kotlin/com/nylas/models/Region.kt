package com.nylas.models

import com.squareup.moshi.Json

enum class Region {
  @Json(name = "us")
  US,
  @Json(name = "eu")
  EU
}