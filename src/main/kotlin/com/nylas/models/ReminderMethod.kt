package com.nylas.models

import com.squareup.moshi.Json

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
