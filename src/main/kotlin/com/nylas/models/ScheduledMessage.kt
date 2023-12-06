package com.nylas.models

/**
 * Class representing information about a scheduled message.
 */
data class ScheduledMessage(
  /**
   * The unique identifier for the scheduled message.
   */
  val scheduleId: Int,
  /**
   * The status of the scheduled message.
   */
  val status: ScheduledMessageStatus,
  /**
   * The time the message was sent or failed to send, in epoch time.
   */
  val closeTime: Int?,
)
