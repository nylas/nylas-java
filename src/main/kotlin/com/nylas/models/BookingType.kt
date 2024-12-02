package com.nylas.models

import com.squareup.moshi.Json

/**
 * Enum for booking types.
 */
enum class BookingType {
  @Json(name = "booking")
  BOOKING,

  @Json(name = "organizer-confirmation")
  ORGANIZER_CONFIRMATION,
}
