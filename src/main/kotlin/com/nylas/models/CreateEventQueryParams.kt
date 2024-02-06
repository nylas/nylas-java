package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of the query parameters for creating an event.
 */
data class CreateEventQueryParams(
  /**
   * The ID of the calendar to create the event in.
   */
  @Json(name = "calendar_id")
  val calendarId: String,
  /**
   * Email notifications containing the calendar event is sent to all event participants.
   */
  @Json(name = "notify_participants")
  val notifyParticipants: Boolean? = null,
) : IQueryParams {
  /**
   * Builder for [CreateEventQueryParams].
   * @property calendarId The ID of the calendar to create the event in.
   */
  data class Builder(
    private val calendarId: String,
  ) {
    private var notifyParticipants: Boolean? = null

    /**
     * Sets whether notifications containing the calendar event is sent to all event participants.
     * @param notifyParticipants Email notifications containing the calendar event is sent to all event participants.
     * @return The builder.
     */
    fun notifyParticipants(notifyParticipants: Boolean?) = apply { this.notifyParticipants = notifyParticipants }

    /**
     * Builds a [CreateEventQueryParams] instance.
     * @return The [CreateEventQueryParams] instance.
     */
    fun build() = CreateEventQueryParams(
      calendarId,
      notifyParticipants,
    )
  }
}
