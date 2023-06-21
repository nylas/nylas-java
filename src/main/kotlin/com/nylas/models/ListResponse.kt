package com.nylas.models

import com.squareup.moshi.Json

data class ListResponse<T>(
  @Json(name = "request_id")
  val requestId: String = "",
  @Json(name = "data")
  val data: List<T> = emptyList(),
  @Json(name = "next_cursor")
  val nextCursor: String? = null,
  // TODO::Define error
  @Json(name = "error")
  val error: Any? = null,
)
