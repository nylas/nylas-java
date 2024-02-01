package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing the reminder details for an event.
 */
data class ReminderOverride(
  /**
   * The number of minutes before the event start time when a user wants a reminder for this event.
   * Reminder minutes are in the following format: "[20]".
   */
  @Json(name = "reminder_minutes")
  val reminderMinutes: String? = null,
  /**
   * Method to remind the user about the event. (Google only).
   */
  @Json(name = "reminder_method")
  val reminderMethod: ReminderMethod? = null,
)
