package com.nylas.models

import com.squareup.moshi.Json

/**
 * Enum representing the status of an Event participant.
 */
enum class ParticipantStatus {
  @Json(name = "noreply")
  NOREPLY,

  @Json(name = "yes")
  YES,

  @Json(name = "no")
  NO,

  @Json(name = "maybe")
  MAYBE,
}
