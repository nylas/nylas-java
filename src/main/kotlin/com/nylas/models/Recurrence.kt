package com.nylas.models

import com.squareup.moshi.Json

data class Recurrence(
  @Json(name = "rrule")
  val rrule: List<String>,
  @Json(name = "timezone")
  val timezone: String,
)
