package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of participant details to check availability for.
 */
data class AvailabilityParticipant(
  /**
   * The email address of the participant.
   */
  @Json(name = "email")
  val email: String,
  /**
   * An optional list of the calendar IDs associated with each participant's email address.
   * If not provided, Nylas uses the primary calendar ID.
   */
  @Json(name = "calendar_ids")
  val calendarIds: List<String>? = null,
  /**
   * Open hours for this participant. The endpoint searches for free time slots during these open hours.
   */
  @Json(name = "open_hours")
  val openHours: List<OpenHours>? = null,
) {
  /**
   * A builder for creating an [AvailabilityParticipant].
   * @param email The email address of the participant.
   */
  data class Builder(
    private val email: String,
  ) {
    private var calendarIds: List<String>? = null
    private var openHours: List<OpenHours>? = null

    /**
     * Set the calendar IDs associated with each participant's email address.
     * @param calendarIds The calendar IDs associated with each participant's email address.
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
     * Build the [AvailabilityParticipant].
     * @return The [AvailabilityParticipant].
     */
    fun build() = AvailabilityParticipant(
      email,
      calendarIds,
      openHours,
    )
  }
}
