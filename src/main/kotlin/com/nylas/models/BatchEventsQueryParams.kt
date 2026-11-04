package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of the query parameters for an events batch request.
 * These apply to every operation in the batch.
 */
data class BatchEventsQueryParams(
  /**
   * The ID of the calendar the operations run against.
   */
  @Json(name = "calendar_id")
  val calendarId: String,
  /**
   * Whether to send email notifications to all event participants.
   * Microsoft accounts do not support notify_participants = False.
   */
  @Json(name = "notify_participants")
  val notifyParticipants: Boolean? = null,
  /**
   * The email address of the grant.
   */
  @Json(name = "grant_email")
  val grantEmail: String? = null,
  /**
   * When true, tentative events are counted as busy.
   */
  @Json(name = "tentative_as_busy")
  val tentativeAsBusy: Boolean? = null,
  /**
   * Comma-separated list of fields to return for each event.
   */
  @Json(name = "select")
  val select: String? = null,
) : IQueryParams {
  /**
   * Builder for [BatchEventsQueryParams].
   * @property calendarId The ID of the calendar the operations run against.
   */
  data class Builder(
    private val calendarId: String,
  ) {
    private var notifyParticipants: Boolean? = null
    private var grantEmail: String? = null
    private var tentativeAsBusy: Boolean? = null
    private var select: String? = null

    /**
     * Sets whether to send email notifications to all event participants.
     * Microsoft accounts do not support notify_participants = False.
     * @param notifyParticipants Whether to send email notifications.
     * @return The builder.
     */
    fun notifyParticipants(notifyParticipants: Boolean?) = apply { this.notifyParticipants = notifyParticipants }

    /**
     * Sets the email address of the grant.
     * @param grantEmail The email address of the grant.
     * @return The builder.
     */
    fun grantEmail(grantEmail: String?) = apply { this.grantEmail = grantEmail }

    /**
     * Sets whether tentative events are counted as busy.
     * @param tentativeAsBusy Whether tentative events are counted as busy.
     * @return The builder.
     */
    fun tentativeAsBusy(tentativeAsBusy: Boolean?) = apply { this.tentativeAsBusy = tentativeAsBusy }

    /**
     * Sets the comma-separated list of fields to return for each event.
     * @param select The fields to return.
     * @return The builder.
     */
    fun select(select: String?) = apply { this.select = select }

    /**
     * Builds a [BatchEventsQueryParams] instance.
     * @return The [BatchEventsQueryParams] instance.
     */
    fun build() = BatchEventsQueryParams(
      calendarId,
      notifyParticipants,
      grantEmail,
      tentativeAsBusy,
      select,
    )
  }
}
