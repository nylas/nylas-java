package com.nylas.models

import com.squareup.moshi.Json

data class NylasOAuthError(
  @Json(name = "error")
  val error: String,
  @Json(name = "error_description")
  val errorDescription: String,
  @Json(name = "error_uri")
  val errorUri: String,
  @Json(name = "error_code")
  var errorCode: String,
  override var statusCode: Int? = null,
) : AbstractNylasApiError(error, statusCode)
