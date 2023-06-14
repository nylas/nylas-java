package com.nylas.models

import com.squareup.moshi.Json

enum class ConferencingProvider {
  @Json(name = "Zoom Meeting")
  ZOOM_MEETING,
  @Json(name = "Google Meet")
  GOOGLE_MEET,
  @Json(name = "Microsoft Teams")
  MICROSOFT_TEAMS,
  @Json(name = "WebEx")
  WEBEX,
  @Json(name = "GoToMeeting")
  GOTOMEETING,
}