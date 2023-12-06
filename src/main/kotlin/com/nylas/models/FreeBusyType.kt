package com.nylas.models

import com.squareup.moshi.Json

/**
 * Enum representing the type of free/busy information returned for a calendar.
 */
enum class FreeBusyType(val value: String) {
  @Json(name = "free_busy")
  FREE_BUSY("free_busy"),

  @Json(name = "error")
  ERROR("error"),
}
