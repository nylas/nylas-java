package com.nylas.models

import com.squareup.moshi.Json

/**
 * Enum for confirm booking statuses.
 */
enum class ConfirmBookingStatus {
  @Json(name = "confirm")
  CONFIRM,

  @Json(name = "cancel")
  CANCEL,
}
