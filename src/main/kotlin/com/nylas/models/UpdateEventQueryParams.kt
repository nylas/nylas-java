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
  /**
   * Comma-separated list of fields to return in the response.
   */
  @Json(name = "select")
  val select: String? = null,
  /**
   * When true, tentative events are counted as busy.
   */
  @Json(name = "tentative_as_busy")
  val tentativeAsBusy: Boolean? = null,
) : IQueryParams {

  /**
   * Builder for [UpdateEventQueryParams].
   * @param calendarId The ID of the calendar containing the event.
   */
  data class Builder(
    private val calendarId: String,
  ) {
    private var notifyParticipants: Boolean? = null
    private var select: String? = null
    private var tentativeAsBusy: Boolean? = null

    /**
     * Sets whether to send email notifications containing the calendar event to all event participants.
     * Microsoft accounts do not support notify_participants = False.
     * @param notifyParticipants Whether to send email notifications containing the calendar event to all event participants.
     * @return The builder.
     */
    fun notifyParticipants(notifyParticipants: Boolean?) = apply { this.notifyParticipants = notifyParticipants }

    /**
     * Sets the comma-separated list of fields to return in the response.
     * @param select The fields to return.
     * @return The builder.
     */
    fun select(select: String?) = apply { this.select = select }

    /**
     * Sets whether tentative events are counted as busy.
     * @param tentativeAsBusy Whether tentative events are counted as busy.
     * @return The builder.
     */
    fun tentativeAsBusy(tentativeAsBusy: Boolean?) = apply { this.tentativeAsBusy = tentativeAsBusy }

    /**
     * Builds a [UpdateEventQueryParams] instance.
     * @return The [UpdateEventQueryParams] instance.
     */
    fun build() = UpdateEventQueryParams(
      calendarId,
      notifyParticipants,
      select,
      tentativeAsBusy,
    )
  }
}
