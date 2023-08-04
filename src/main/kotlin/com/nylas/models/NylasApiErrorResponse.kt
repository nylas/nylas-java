package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing an error response from the Nylas API.
 */
data class NylasApiErrorResponse(
  /**
   * The error.
   */
  @Json(name = "error")
  val error: NylasApiError,
  /**
   * The request ID.
   */
  @Json(name = "request_id")
  val requestId: String,
)
