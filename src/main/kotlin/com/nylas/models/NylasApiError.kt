package com.nylas.models

import com.squareup.moshi.Json
import java.lang.Exception

// TODO::Enum for type
data class NylasApiError(
  @Json(name = "type")
  val type: String,
  @Json(name = "message")
  override val message: String,
  @Json(name = "provider_error")
  val providerError: Map<String, Any?>? = null,
  var statusCode: Int? = null,
) : Exception()
