package com.nylas.models

import com.squareup.moshi.Json

/**
 * Enum representation of the different types of event time configurations.
 */
enum class WhenType(val value: String) {
  @Json(name = "date")
  DATE("date"),

  @Json(name = "datespan")
  DATESPAN("datespan"),

  @Json(name = "time")
  TIME("time"),

  @Json(name = "timespan")
  TIMESPAN("timespan"),
}
