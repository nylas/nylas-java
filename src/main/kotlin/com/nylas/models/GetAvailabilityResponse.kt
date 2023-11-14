package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas get availability response
 */
data class GetAvailabilityResponse(
  /**
   * This property is only populated for round-robin events.
   * It will contain the order in which the accounts would be next in line to attend the proposed meeting.
   */
  @Json(name = "order")
  val order: List<String>? = null,
  /**
   * The available time slots where a new meeting can be created for the requested preferences.
   */
  @Json(name = "time_slots")
  val timeSlots: List<TimeSlot>? = null,
)
