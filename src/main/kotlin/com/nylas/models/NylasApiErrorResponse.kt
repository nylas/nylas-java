package com.nylas.models

import com.squareup.moshi.Json

data class NylasApiErrorResponse(
  @Json(name = "request_id")
  val requestId: String,
  @Json(name = "error")
  val error: NylasApiError,
)
