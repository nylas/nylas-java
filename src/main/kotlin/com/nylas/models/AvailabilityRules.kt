package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of the availability rules for a Nylas calendar.
 */
data class AvailabilityRules(
  /**
   * The method used to determine availability for a meeting.
   */
  @Json(name = "availability_method")
  val availabilityMethod: AvailabilityMethod? = null,
  /**
   * The buffer to add to the start and end of a meeting.
   */
  @Json(name = "meeting_buffer")
  val buffer: MeetingBuffer? = null,
  /**
   * A default set of open hours to apply to all participants.
   * You can overwrite these open hours for individual participants by specifying [AvailabilityParticipant.openHours]
   * on the participant object.
   */
  @Json(name = "default_open_hours")
  val defaultOpenHours: List<OpenHours>? = null,
  /**
   * The ID on events that Nylas considers when calculating the order of round-robin participants.
   * This is used for both max-fairness and max-availability methods.
   */
  @Json(name = "round_robin_event_id")
  val roundRobinEventId: String? = null,
) {
  /**
   * A builder for creating a [AvailabilityRules].
   */
  class Builder {
    private var availabilityMethod: AvailabilityMethod? = null
    private var buffer: MeetingBuffer? = null
    private var defaultOpenHours: List<OpenHours>? = null
    private var roundRobinEventId: String? = null

    /**
     * Set the method used to determine availability for a meeting.
     * @param availabilityMethod The method used to determine availability for a meeting.
     * @return The builder.
     */
    fun availabilityMethod(availabilityMethod: AvailabilityMethod) = apply { this.availabilityMethod = availabilityMethod }

    /**
     * Set the buffer to add to the start and end of a meeting.
     * @param buffer The buffer to add to the start and end of a meeting.
     * @return The builder.
     */
    fun buffer(buffer: MeetingBuffer) = apply { this.buffer = buffer }

    /**
     * Set the default set of open hours to apply to all participants.
     * You can overwrite these open hours for individual participants by specifying [AvailabilityParticipant.openHours]
     * on the participant object.
     * @param defaultOpenHours The default set of open hours to apply to all participants.
     * @return The builder.
     */
    fun defaultOpenHours(defaultOpenHours: List<OpenHours>) = apply { this.defaultOpenHours = defaultOpenHours }

    /**
     * Set the ID on events that Nylas considers when calculating the order of round-robin participants.
     * This is used for both max-fairness and max-availability methods.
     * @param roundRobinEventId The ID on events that Nylas considers when calculating the order of round-robin participants.
     * @return The builder.
     */
    fun roundRobinEventId(roundRobinEventId: String) = apply { this.roundRobinEventId = roundRobinEventId }

    /**
     * Build the [AvailabilityRules] object.
     * @return The completed [AvailabilityRules] object.
     */
    fun build() = AvailabilityRules(
      availabilityMethod = availabilityMethod,
      buffer = buffer,
      defaultOpenHours = defaultOpenHours,
      roundRobinEventId = roundRobinEventId,
    )
  }
}
