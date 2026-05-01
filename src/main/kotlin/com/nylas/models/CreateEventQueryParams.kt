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
   * Builder for [CreateEventQueryParams].
   * @property calendarId The ID of the calendar to create the event in.
   */
  data class Builder(
    private val calendarId: String,
  ) {
    private var notifyParticipants: Boolean? = null
    private var select: String? = null
    private var tentativeAsBusy: Boolean? = null

    /**
     * Sets whether notifications containing the calendar event is sent to all event participants.
     * @param notifyParticipants Email notifications containing the calendar event is sent to all event participants.
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
     * Builds a [CreateEventQueryParams] instance.
     * @return The [CreateEventQueryParams] instance.
     */
    fun build() = CreateEventQueryParams(
      calendarId,
      notifyParticipants,
      select,
      tentativeAsBusy,
    )
  }
}
