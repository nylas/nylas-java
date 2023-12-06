package com.nylas.models

import com.squareup.moshi.Json

/**
 * Enum for the sort options
 */
enum class SortBy {
  @Json(name = "created_at")
  CREATED_AT,

  @Json(name = "updated_at")
  UPDATED_AT,
}
