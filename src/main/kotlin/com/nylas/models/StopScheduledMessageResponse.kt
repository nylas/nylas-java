package com.nylas.models

/**
 * Class representing a response after stopping a scheduled message.
 */
data class StopScheduledMessageResponse(
  /**
   * A message describing the result of the request.
   */
  val message: String,
)
