package com.nylas.models

import com.squareup.moshi.Json

data class UpdateEventQueryParams(
  @Json(name = "calendar_id")
  val calendarId: String,
  @Json(name = "notify_participants")
  val notifyParticipants: Boolean? = null,
) : IQueryParams {
  data class Builder(
    private val calendarId: String,
  ) {
    private var notifyParticipants: Boolean? = null

    fun notifyParticipants(notifyParticipants: Boolean?) = apply { this.notifyParticipants = notifyParticipants }

    fun build() = UpdateEventQueryParams(
      calendarId,
      notifyParticipants,
    )
  }
}
