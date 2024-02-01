package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing the reminders field of an event.
 */
data class Reminders(
  /**
   * Whether to use the default reminders for the calendar.
   * When true, uses the default reminder settings for the calendar
   */
  @Json(name = "use_default")
  val useDefault: Boolean,
  /**
   *  list of reminders for the event if useDefault is set to false.
   */
  @Json(name = "override")
  val override: List<ReminderOverride>? = null,
)
