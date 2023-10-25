package com.nylas.models

import com.squareup.moshi.Json

/**
 * Enum for the order options
 */
enum class OrderBy {
  @Json(name = "desc")
  DESCENDING,

  @Json(name = "asc")
  ASCENDING,
}
