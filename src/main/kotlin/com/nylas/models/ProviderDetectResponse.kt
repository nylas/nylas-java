package com.nylas.models

import com.squareup.moshi.Json

data class ProviderDetectResponse(
  @Json(name = "email_address")
  val emailAddress: String,
  @Json(name = "detected")
  val detected: Boolean,
  @Json(name = "provider")
  val provider: String?,
  @Json(name = "type")
  val type: String?,
)
