package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing the different Google Event types.
 */
enum class EventType {
  @Json(name = "default")
  DEFAULT,

  @Json(name = "outOfOffice")
  OUT_OF_OFFICE,

  @Json(name = "focusTime")
  FOCUS_TIME,

  @Json(name = "workingLocation")
  WORKING_LOCATION,
}
