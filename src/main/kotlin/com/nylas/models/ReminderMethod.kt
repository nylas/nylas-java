package com.nylas.models

import com.squareup.moshi.Json

/**
 * Enum representing the different types of reminders.
 */
enum class ReminderMethod {
  @Json(name = "email")
  EMAIL,

  @Json(name = "sound")
  SOUND,

  @Json(name = "popup")
  POPUP,

  @Json(name = "display")
  DISPLAY,
}
