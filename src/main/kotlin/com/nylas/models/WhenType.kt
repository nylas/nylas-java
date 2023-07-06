package com.nylas.models

import com.squareup.moshi.Json

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
