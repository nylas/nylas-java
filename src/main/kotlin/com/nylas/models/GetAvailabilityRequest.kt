package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas get availability request
 */
data class GetAvailabilityRequest(
  /**
   * Unix timestamp for the start time to check availability for.
   */
  @Json(name = "start_time")
  val startTime: Int,
  /**
   * Unix timestamp for the end time to check availability for.
   */
  @Json(name = "end_time")
  val endTime: Int,
  /**
   * Participant details to check availability for.
   */
  @Json(name = "participants")
  val participants: List<AvailabilityParticipant>,
  /**
   * The total number of minutes the event should last.
   */
  @Json(name = "duration_minutes")
  val durationMinutes: Int,
  /**
   * Nylas checks from the nearest interval of the passed [startTime].
   * For example, to schedule 30-minute meetings ([durationMinutes]) with 15 minutes between them ([intervalMinutes]).
   * If you have a meeting starting at 9:59, the API returns times starting at 10:00. 10:00-10:30, 10:15-10:45.
   */
  @Json(name = "interval_minutes")
  val intervalMinutes: Int? = null,
  /**
   * When set to true, the availability time slots will start at 30 minutes past or on the hour.
   * For example, a free slot starting at 16:10 is considered available only from 16:30.
   */
  @Deprecated("Use roundTo instead")
  @Json(name = "round_to_30_minutes")
  val roundTo30Minutes: Boolean? = null,
  /**
   * The rules to apply when checking availability.
   */
  @Json(name = "availability_rules")
  val availabilityRules: AvailabilityRules? = null,
  /**
   * The number of minutes to round the time slots to.
   * This allows for rounding to any multiple of 5 minutes, up to a maximum of 60 minutes.
   * The default value is set to 15 minutes.
   * When this variable is assigned a value, it overrides the behavior of the [roundTo30Minutes] flag, if it was set.
   */
  @Json(name = "round_to")
  val roundTo: Int? = null,
) {
  /**
   * A builder for creating a [GetAvailabilityRequest].
   *
   * @param startTime Unix timestamp for the start time to check availability for.
   * @param endTime Unix timestamp for the end time to check availability for.
   * @param participants Participant details to check availability for.
   * @param durationMinutes The total number of minutes the event should last.
   */
  data class Builder(
    private val startTime: Int,
    private val endTime: Int,
    private val participants: List<AvailabilityParticipant>,
    private val durationMinutes: Int,
  ) {
    private var intervalMinutes: Int? = null
    private var roundTo30Minutes: Boolean? = null
    private var availabilityRules: AvailabilityRules? = null
    private var roundTo: Int? = null

    /**
     * Set the Nylas checks from the nearest interval of the passed [startTime].
     * For example, to schedule 30-minute meetings ([durationMinutes]) with 15 minutes between them ([intervalMinutes]).
     * If you have a meeting starting at 9:59, the API returns times starting at 10:00. 10:00-10:30, 10:15-10:45.
     * @param intervalMinutes Nylas checks from the nearest interval of the passed [startTime].
     * @return The builder.
     */
    fun intervalMinutes(intervalMinutes: Int) = apply { this.intervalMinutes = intervalMinutes }

    /**
     * Set when set to true, the availability time slots will start at 30 minutes past or on the hour.
     * For example, a free slot starting at 16:10 is considered available only from 16:30.
     * @param roundTo30Minutes When set to true, the availability time slots will start at 30 minutes past or on the hour.
     * @return The builder.
     */
    @Deprecated("Use roundTo instead")
    fun roundTo30Minutes(roundTo30Minutes: Boolean) = apply { this.roundTo30Minutes = roundTo30Minutes }

    /**
     * Set the rules to apply when checking availability.
     * @param availabilityRules The rules to apply when checking availability.
     * @return The builder.
     */
    fun availabilityRules(availabilityRules: AvailabilityRules) = apply { this.availabilityRules = availabilityRules }

    /**
     * Set the number of minutes to round the time slots to.
     * This allows for rounding to any multiple of 5 minutes, up to a maximum of 60 minutes.
     * The default value is set to 15 minutes.
     * When this variable is assigned a value, it overrides the behavior of the [roundTo30Minutes] flag, if it was set.
     * @param roundTo The number of minutes to round the time slots to.
     * @return The builder.
     */
    fun roundTo(roundTo: Int) = apply { this.roundTo = roundTo }

    /**
     * Build the [GetAvailabilityRequest].
     * @return The [GetAvailabilityRequest].
     */
    fun build() = GetAvailabilityRequest(
      startTime,
      endTime,
      participants,
      durationMinutes,
      intervalMinutes,
      roundTo30Minutes,
      availabilityRules,
      roundTo,
    )
  }
}
