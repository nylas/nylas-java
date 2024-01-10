package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing a scheduled message status.
 */
data class ScheduledMessageStatus(
  /**
   * The status code the describes the state of the scheduled message
   */
  @Json(name = "code")
  val code: String,
  /**
   * A description of the status of the scheduled message
   */
  @Json(name = "description")
  val description: String,
)
