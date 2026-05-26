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
  /**
   * When true, Nylas does not send an email to the event organizer when the RSVP is sent.
   */
  @Json(name = "skip_nylas_email")
  val skipNylasEmail: Boolean? = null,
) : IQueryParams {

  /**
   * Builder for [SendRsvpQueryParams].
   * @param calendarId The ID of the calendar to create the event in.
   */
  data class Builder(private val calendarId: String) {
    private var skipNylasEmail: Boolean? = null

    /**
     * Sets whether to skip sending a Nylas email to the event organizer.
     * @param skipNylasEmail Whether to skip the email notification.
     * @return The builder.
     */
    fun skipNylasEmail(skipNylasEmail: Boolean?) = apply { this.skipNylasEmail = skipNylasEmail }

    /**
     * Builds a [SendRsvpQueryParams] instance.
     * @return The [SendRsvpQueryParams] instance.
     */
    fun build() = SendRsvpQueryParams(calendarId, skipNylasEmail)
  }
}
