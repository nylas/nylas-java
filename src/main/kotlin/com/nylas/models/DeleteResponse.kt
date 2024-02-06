package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing a response to a delete request.
 */
data class DeleteResponse(
  /**
   * ID of the request.
   */
  @Json(name = "request_id")
  val requestId: String = "",
)
