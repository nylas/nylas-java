package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing a response after stopping a scheduled message.
 */
data class StopScheduledMessageResponse(
  /**
   * A message describing the result of the request.
   */
  @Json(name = "message")
  val message: String,
)
