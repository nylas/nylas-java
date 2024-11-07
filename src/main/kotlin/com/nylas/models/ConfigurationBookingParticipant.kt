package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a participant in a booking.
 */
data class ConfigurationBookingParticipant(
  /**
   * The calendar ID that the event is created in.
   */
  @Json(name = "calendar_id")
  val calendarId: String,
) {
  /**
   * Builder for [ConfigurationBookingParticipant].
   */
  class Builder {
    private var calendarId: String? = null

    /**
     * Set the calendar ID for this participant.
     * @param calendarId The calendar ID for this participant.
     * @return The builder.
     */
    fun calendarId(calendarId: String?) = apply { this.calendarId = calendarId }

    /**
     * Builds a [ConfigurationBookingParticipant] instance.
     * @return The [ConfigurationBookingParticipant] instance.
     */
    fun build() = ConfigurationBookingParticipant(calendarId)
  }
}
