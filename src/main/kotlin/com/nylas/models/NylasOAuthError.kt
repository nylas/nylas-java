package com.nylas.models

import com.squareup.moshi.Json
import java.lang.Exception

data class NylasOAuthError(
  @Json(name = "error")
  val error: String,
  @Json(name = "error_description")
  val errorDescription: String,
  @Json(name = "error_uri")
  val providerError: String,
  @Json(name = "error_code")
  var errorCode: String,
  var statusCode: Int? = null,
) : Exception(error)
