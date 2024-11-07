package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of availability settings.
 */
data class ConfigurationAvailability(
  /**
   * The total number of minutes the event should last.
   */
  @Json(name = "duration_minutes")
  val durationMinutes: Int,
  /**
   * The interval between meetings in minutes.
   */
  @Json(name = "interval_minutes")
  val intervalMinutes: Int? = null,
  /**
   * Nylas rounds each time slot to the nearest multiple of this number of minutes.
   */
  @Json(name = "round_to")
  val roundTo: Int? = null,
  /**
   * Availability rules for scheduling configuration.
   */
  @Json(name = "availability_rules")
  val availabilityRules: AvailabilityRules? = null,
) {
  class Builder {
    private var durationMinutes: Int? = null
    private var intervalMinutes: Int? = null
    private var roundTo: Int? = null
    private var availabilityRules: AvailabilityRules? = null

    /**
     * Set the duration of the event in minutes.
     * @param durationMinutes The duration of the event in minutes.
     * @return The builder.
     */
    fun durationMinutes(durationMinutes: Int?) = apply { this.durationMinutes = durationMinutes }

    /**
     * Set the interval between meetings in minutes.
     * @param intervalMinutes The interval between meetings in minutes.
     * @return The builder.
     */
    fun intervalMinutes(intervalMinutes: Int?) = apply { this.intervalMinutes = intervalMinutes }

    /**
     * Set Nylas rounds each time slot to the nearest multiple of this number of minutes.
     * @param roundTo Nylas rounds each time slot to the nearest multiple of this number of minutes.
     * @return The builder.
     */
    fun roundTo(roundTo: Int?) = apply { this.roundTo = roundTo }

    /**
     * Set availability rules for scheduling configuration.
     * @param availabilityRules Availability rules for scheduling configuration.
     * @return The builder.
     */
    fun availabilityRules(availabilityRules: AvailabilityRules?) = apply { this.availabilityRules = availabilityRules }

    /**
     * Build the [ConfigurationAvailability].
     * @return The [ConfigurationAvailability].
     */
    fun build() = ConfigurationAvailability(
      durationMinutes,
      intervalMinutes,
      roundTo,
      availabilityRules,
    )
  }
}
