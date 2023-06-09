package com.nylas.models

import com.squareup.moshi.Json

data class DeleteResponse(
  @Json(name = "request_id")
  val requestId: String = "",
)