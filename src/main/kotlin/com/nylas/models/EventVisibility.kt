package com.nylas.models

import com.squareup.moshi.Json

/**
 * Enum representation of visibility of the event, if the event is private or public.
 */
enum class EventVisibility {
  @Json(name = "default")
  DEFAULT,

  @Json(name = "public")
  PUBLIC,

  @Json(name = "private")
  PRIVATE,
}
