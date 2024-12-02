package com.nylas.models

import com.squareup.moshi.Json

/**
 * Enum for confirm booking statuses.
 */
enum class ConfirmBookingStatus {
  @Json(name = "confirmed")
  CONFIRMED,

  @Json(name = "cancelled")
  CANCELLED,
}
