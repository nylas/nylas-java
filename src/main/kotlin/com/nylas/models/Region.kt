package com.nylas.models

import com.squareup.moshi.Json

/**
 * Enum representing the available Nylas API regions.
 * @param nylasApiUrl The base URL for the region's API
 */
enum class Region(val nylasApiUrl: String) {
  @Json(name = "us")
  US("https://api.us.nylas.com"),

  @Json(name = "eu")
  EU("https://api.eu.nylas.com"),
}
