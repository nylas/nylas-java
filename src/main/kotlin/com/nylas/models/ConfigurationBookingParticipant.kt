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
   * @param calendarId The calendar ID that the event is created in.
   */
  data class Builder(
    private val calendarId: String,
  ) {
    /**
     * Builds a [ConfigurationBookingParticipant] instance.
     * @return The [ConfigurationBookingParticipant] instance.
     */
    fun build() = ConfigurationBookingParticipant(calendarId)
  }
}
