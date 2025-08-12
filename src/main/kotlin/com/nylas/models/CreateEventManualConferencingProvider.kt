package com.nylas.models

import com.squareup.moshi.Json

/**
 * Enum for the different conferencing providers that can be used to manually create a meeting.
 */
enum class CreateEventManualConferencingProvider {
  @Json(name = "Google Meet")
  GOOGLE_MEET,

  @Json(name = "Zoom Meeting")
  ZOOM_MEETING,

  @Json(name = "Microsoft Teams")
  MICROSOFT_TEAMS,

  @Json(name = "Teams for Business")
  TEAMS_FOR_BUSINESS,

  @Json(name = "Skype for Business")
  SKYPE_FOR_BUSINESS,

  @Json(name = "Skype for Consumer")
  SKYPE_FOR_CONSUMER,
}
