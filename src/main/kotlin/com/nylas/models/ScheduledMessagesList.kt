package com.nylas.models

/**
 * Class representing a list of scheduled messages.
 */
data class ScheduledMessagesList(
  /**
   * The list of scheduled messages.
   */
  val schedules: List<ScheduledMessage>,
)
