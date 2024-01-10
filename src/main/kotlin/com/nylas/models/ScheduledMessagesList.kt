package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing a list of scheduled messages.
 */
data class ScheduledMessagesList(
  /**
   * The list of scheduled messages.
   */
  @Json(name = "schedules")
  val schedules: List<ScheduledMessage>,
)
