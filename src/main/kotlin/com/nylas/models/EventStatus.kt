package com.nylas.models

import com.squareup.moshi.Json

/**
 * Enum representing the status of an event.
 */
enum class EventStatus {
  @Json(name = "confirmed")
  CONFIRMED,

  @Json(name = "tentative")
  TENTATIVE,

  @Json(name = "cancelled")
  CANCELLED,
}
