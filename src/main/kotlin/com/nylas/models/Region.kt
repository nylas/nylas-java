package com.nylas.models

import com.squareup.moshi.Json

enum class Region(val nylasApiUrl: String) {
  @Json(name = "us")
  US("https://api.us.nylas.com"),

  @Json(name = "eu")
  EU("https://api.eu.nylas.com"),
}
