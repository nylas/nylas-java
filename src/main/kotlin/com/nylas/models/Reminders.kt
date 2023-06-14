package com.nylas.models

import com.squareup.moshi.Json

data class Reminders(
  @Json(name = "reminder_minutes")
  val reminderMinutes: String,
  @Json(name = "reminder_method")
  val reminderMethod: ReminderMethod? = null
)
