package com.nylas.models

/**
 * Class representing a scheduled message status.
 */
data class ScheduledMessageStatus(
  /**
   * The status code the describes the state of the scheduled message
   */
  val code: String,
  /**
   * A description of the status of the scheduled message
   */
  val description: String,
)
