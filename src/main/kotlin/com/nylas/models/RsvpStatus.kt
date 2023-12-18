package com.nylas.models

import com.squareup.moshi.Json

/**
 * Enum representing the allowed RSVP status values.
 */
enum class ParticipantStatus {
  @Json(name = "yes")
  YES,

  @Json(name = "no")
  NO,

  @Json(name = "maybe")
  MAYBE,
}
