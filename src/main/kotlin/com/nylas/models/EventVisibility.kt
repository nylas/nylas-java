package com.nylas.models

import com.squareup.moshi.Json

/**
 * Enum representation of visibility of the event, if the event is private or public.
 *
 * [DEFAULT] is only valid for Google events, where it defers to the calendar's own sharing
 * settings. Microsoft and EWS events only support [PUBLIC] and [PRIVATE]; sending `default`
 * for these providers returns a 400 error.
 */
enum class EventVisibility {
  @Json(name = "default")
  DEFAULT,

  @Json(name = "public")
  PUBLIC,

  @Json(name = "private")
  PRIVATE,
}
