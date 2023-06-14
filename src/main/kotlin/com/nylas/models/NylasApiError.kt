package com.nylas.models

import com.squareup.moshi.Json

//TODO::Enum for type
data class NylasApiError(
  @Json(name = "type")
  val type: String,
  @Json(name = "message")
  override val message: String,
  @Json(name = "provider_error")
  val providerError: Map<String, Any?>? = null,
): Error()
