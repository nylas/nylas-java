package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas availability time slot
 */
data class TimeSlot(
  /**
   * The emails of the participants who are available for the time slot.
   */
  @Json(name = "emails")
  val emails: List<String>,
  /**
   * Unix timestamp for the start of the slot.
   */
  @Json(name = "start_time")
  val startTime: Int,
  @Json(name = "end_time")
  /**
   * Unix timestamp for the end of the slot.
   */
  val endTime: Int,
)