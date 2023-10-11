package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas get free-busy request
 */
data class GetFreeBusyRequest(
  /**
   * Unix timestamp representing the start of the time block for assessing the account's free/busy schedule.
   */
  @Json(name = "start_time")
  val startTime: Int,
  /**
   * Unix timestamp representing the end of the time block for assessing the account's free/busy schedule.
   */
  @Json(name = "end_time")
  val endTime: Int,
  /**
   * A list of email addresses to check the free/busy schedules for.
   */
  @Json(name = "emails")
  val emails: List<String>,
)
