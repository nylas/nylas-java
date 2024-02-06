package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of the query parameters for updating events.
 */
data class UpdateEventQueryParams(
  /**
   * The ID of the calendar containing the event.
   */
  @Json(name = "calendar_id")
  val calendarId: String,
  /**
   * Whether to send email notifications containing the calendar event to all event participants.
   * Microsoft accounts do not support notify_participants = False.
   */
  @Json(name = "notify_participants")
  val notifyParticipants: Boolean? = null,
) : IQueryParams {

  /**
   * Builder for [UpdateEventQueryParams].
   * @param calendarId The ID of the calendar containing the event.
   */
  data class Builder(
    private val calendarId: String,
  ) {
    private var notifyParticipants: Boolean? = null

    /**
     * Sets whether to send email notifications containing the calendar event to all event participants.
     * Microsoft accounts do not support notify_participants = False.
     * @param notifyParticipants Whether to send email notifications containing the calendar event to all event participants.
     * @return The builder.
     */
    fun notifyParticipants(notifyParticipants: Boolean?) = apply { this.notifyParticipants = notifyParticipants }

    /**
     * Builds a [UpdateEventQueryParams] instance.
     * @return The [UpdateEventQueryParams] instance.
     */
    fun build() = UpdateEventQueryParams(
      calendarId,
      notifyParticipants,
    )
  }
}
