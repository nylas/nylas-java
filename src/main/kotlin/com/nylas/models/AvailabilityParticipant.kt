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
  /**
   * An array of date and time ranges when the participant is available.
   * This can override the open_hours configurations for a specific date and time range.
   */
  @Json(name = "specific_time_availability")
  val specificTimeAvailability: List<SpecificTimeAvailability>? = null,
  /**
   * When set to true, only the times specified in [specificTimeAvailability] are considered available,
   * ignoring the [openHours] configuration.
   */
  @Json(name = "only_specific_time_availability")
  val onlySpecificTimeAvailability: Boolean? = null,
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
    private var specificTimeAvailability: List<SpecificTimeAvailability>? = null
    private var onlySpecificTimeAvailability: Boolean? = null

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
     * Set the specific time availability to override the open hours for specific dates and time ranges.
     * @param specificTimeAvailability An array of date and time ranges when the participant is available.
     * @return The builder.
     */
    fun specificTimeAvailability(specificTimeAvailability: List<SpecificTimeAvailability>) = apply { this.specificTimeAvailability = specificTimeAvailability }

    /**
     * Set whether only the times specified in [SpecificTimeAvailability] are considered available.
     * When set to true, the [OpenHours] configuration is ignored.
     * @param onlySpecificTimeAvailability Whether to only use specific time availability.
     * @return The builder.
     */
    fun onlySpecificTimeAvailability(onlySpecificTimeAvailability: Boolean) = apply { this.onlySpecificTimeAvailability = onlySpecificTimeAvailability }

    /**
     * Build the [AvailabilityParticipant].
     * @return The [AvailabilityParticipant].
     */
    fun build() = AvailabilityParticipant(
      email,
      calendarIds,
      openHours,
      specificTimeAvailability,
      onlySpecificTimeAvailability,
    )
  }
}
