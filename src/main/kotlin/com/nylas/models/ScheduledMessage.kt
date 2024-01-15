package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing information about a scheduled message.
 */
data class ScheduledMessage(
  /**
   * The unique identifier for the scheduled message.
   */
  @Json(name = "schedule_id")
  val scheduleId: String,
  /**
   * The status of the scheduled message.
   */
  @Json(name = "status")
  val status: ScheduledMessageStatus,
  /**
   * The time the message was sent or failed to send, in epoch time.
   */
  @Json(name = "close_time")
  val closeTime: Int?,
)
