package com.nylas.models

import com.squareup.moshi.Json

/**
 * Enum for the different conferencing providers.
 */
enum class ConferencingProvider {
  @Json(name = "Google Meet")
  GOOGLE_MEET,

  @Json(name = "GoToMeeting")
  GOTOMEETING,

  @Json(name = "Microsoft Teams")
  MICROSOFT_TEAMS,

  @Json(name = "WebEx")
  WEBEX,

  @Json(name = "Zoom Meeting")
  ZOOM_MEETING,

  @Json(name = "unknown")
  UNKNOWN,
}
