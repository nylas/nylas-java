package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas response object
 */
data class Response<T>(
  /**
   * The requested data object
   */
  @Json(name = "data")
  val data: T,
  /**
   * The request ID
   */
  @Json(name = "request_id")
  val requestId: String = "",
)
