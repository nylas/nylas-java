package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas send-rsvp request
 */
data class SendRsvpRequest(
  /**
   * RSVP status. must be yes, no, or maybe
   */
  @Json(name = "status")
  val status: String,
)
