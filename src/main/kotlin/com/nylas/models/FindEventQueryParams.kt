package com.nylas.models

import com.squareup.moshi.Json

data class FindEventQueryParams(
  @Json(name = "calendar_id")
  val calendarId: String,
) : IQueryParams {
  data class Builder(
    private val calendarId: String,
  ) {
    fun build() = FindEventQueryParams(
      calendarId,
    )
  }
}
