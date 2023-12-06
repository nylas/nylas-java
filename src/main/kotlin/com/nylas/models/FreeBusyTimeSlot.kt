package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas free-busy time slot object.
 */
data class FreeBusyTimeSlot(
  /**
   * Unix timestamp for the start of the slot.
   */
  @Json(name = "start_time")
  val startTime: Int,
  /**
   * Unix timestamp for the end of the slot.
   */
  @Json(name = "end_time")
  val endTime: Int,
  /**
   * The status of the time slot.
   */
  @Json(name = "status")
  val status: String,
)
