package com.nylas.models

import com.squareup.moshi.Json

data class Response<T>(
  @Json(name = "data")
  val data: T,
  @Json(name = "request_id")
  val requestId: String = "",
)
