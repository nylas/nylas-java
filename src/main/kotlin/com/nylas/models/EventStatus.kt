package com.nylas.models

import com.squareup.moshi.Json

/**
 * Enum representing the status of an event.
 */
enum class EventStatus {
  @Json(name = "confirmed")
  CONFIRMED,

  @Json(name = "maybe")
  MAYBE,

  @Deprecated(
    message = "Use MAYBE instead. TENTATIVE is a legacy alias and will be removed in a future release.",
    replaceWith = ReplaceWith("MAYBE"),
  )
  @Json(name = "tentative")
  TENTATIVE,

  @Json(name = "cancelled")
  CANCELLED,
}
