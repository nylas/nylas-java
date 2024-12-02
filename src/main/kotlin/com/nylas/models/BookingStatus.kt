package com.nylas.models

import com.squareup.moshi.Json

/**
 * Enum for booking statuses.
 */
enum class BookingStatus {
  @Json(name = "pending")
  PENDING,

  @Json(name = "booked")
  BOOKED,

  @Json(name = "cancelled")
  CANCELLED,
}
