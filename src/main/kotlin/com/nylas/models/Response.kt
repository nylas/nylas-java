package com.nylas.models

import com.squareup.moshi.Json

data class Response<T>(
  @Json(name = "request_id")
  val requestId: String = "",
  @Json(name = "data")
  val data: T,
  // TODO::Define error
  @Json(name = "error")
  val error: Any? = null,
)
