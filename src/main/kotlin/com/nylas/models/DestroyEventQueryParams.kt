package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing a query to delete an event.
 */
data class DestroyEventQueryParams(
  /**
   * Specify calendar ID of the event. "primary" is a supported value indicating the user's primary calendar.
   */
  @Json(name = "calendar_id")
  val calendarId: String,
  /**
   * Defaults to true, email notifications containing the calendar event is sent to all event participants.
   * Microsoft accounts do not support notify_participants = False.
   */
  @Json(name = "notify_participants")
  val notifyParticipants: Boolean? = null,
) : IQueryParams {
  /**
   * Builder for [DestroyEventQueryParams].
   * @property calendarId Specify calendar ID of the event. "primary" is a supported value indicating the user's primary calendar.
   */
  data class Builder(
    private val calendarId: String,
  ) {
    private var notifyParticipants: Boolean? = null

    /**
     * Set whether to send email notifications containing the calendar event to all event participants.
     * Microsoft accounts do not support notify_participants = False.
     * @param notifyParticipants Whether to send email notifications containing the calendar event to all event participants
     * @return This builder
     */
    fun notifyParticipants(notifyParticipants: Boolean?) = apply { this.notifyParticipants = notifyParticipants }

    /**
     * Build the [DestroyEventQueryParams].
     * @return The built [DestroyEventQueryParams]
     */
    fun build() = DestroyEventQueryParams(
      calendarId,
      notifyParticipants,
    )
  }
}
