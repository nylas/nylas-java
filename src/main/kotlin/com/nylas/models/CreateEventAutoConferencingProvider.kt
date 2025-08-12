package com.nylas.models

import com.squareup.moshi.Json

/**
 * Enum for the different conferencing providers that can be used to auto-create a meeting.
 */
enum class CreateEventAutoConferencingProvider {
  @Json(name = "Google Meet")
  GOOGLE_MEET,

  @Json(name = "Zoom Meeting")
  ZOOM_MEETING,

  @Json(name = "Microsoft Teams")
  MICROSOFT_TEAMS,
}
