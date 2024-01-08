package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of the query parameters for sending RSVP.
 */
data class SendRsvpQueryParams(
  /**
   * The ID of the calendar to create the event in.
   */
  @Json(name = "calendar_id")
  val calendarId: String,
) : IQueryParams {

  /**
   * Builder for [SendRsvpQueryParams].
   * @param calendarId The ID of the calendar to create the event in.
   */
  data class Builder(private val calendarId: String) {

    /**
     * Builds a [SendRsvpQueryParams] instance.
     * @return The [SendRsvpQueryParams] instance.
     */
    fun build() = SendRsvpQueryParams(calendarId)
  }
}
