package com.nylas.models

import com.squareup.moshi.Json

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