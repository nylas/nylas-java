package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas response object that contains a list of objects.
 */
data class ListResponse<T>(
  /**
   * The list of requested data objects.
   */
  @Json(name = "data")
  val data: List<T> = emptyList(),
  /**
   * The request ID.
   */
  @Json(name = "request_id")
  val requestId: String = "",
  /**
   * The cursor to use to get the next page of data.
   */
  @Json(name = "next_cursor")
  val nextCursor: String? = null,
)
