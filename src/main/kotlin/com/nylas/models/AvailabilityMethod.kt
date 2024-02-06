package com.nylas.models

import com.squareup.moshi.Json

/**
 * Enum representing the method used to determine availability for a meeting.
 */
enum class AvailabilityMethod {
  @Json(name = "max-fairness")
  MAX_FAIRNESS,

  @Json(name = "max-availability")
  MAX_AVAILABILITY,
}
