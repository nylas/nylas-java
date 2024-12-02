package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a participant in a booking.
 */

data class ConfigurationAvailabilityParticipant(
  /**
   * @param calendarIds The calendar IDs that the event is created in.
   */
  @Json(name = "calendar_ids")
  val calendarIds: List<String>? = emptyList(),
  /**
   * Open hours for this participant. The endpoint searches for free time slots during these open hours.
   */
  @Json(name = "open_hours")
  val openHours: List<OpenHours>? = null,
) {
  /**
   * Builder for [ConfigurationAvailabilityParticipant].
   */
  class Builder {
    private var calendarIds: List<String>? = null
    private var openHours: List<OpenHours>? = null

    /**
     * Set the calendar IDs for this participant.
     * @param calendarIds Calendar IDs for this participant.
     * @return The builder.
     */
    fun calendarIds(calendarIds: List<String>) = apply { this.calendarIds = calendarIds }

    /**
     * Set the open hours for this participant.
     * @param openHours Open hours for this participant.
     * @return The builder.
     */
    fun openHours(openHours: List<OpenHours>) = apply { this.openHours = openHours }

    /**
     * Set the open hours for this participant.
     * @param openHours Open hours for this participant.
     * @return The builder.
     */
    fun build() = ConfigurationAvailabilityParticipant(
      calendarIds,
      openHours,
    )
  }
}
