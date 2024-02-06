package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing an OAuth error returned by the Nylas API.
 */
data class NylasOAuthError(
  /**
   * Error type.
   */
  @Json(name = "error")
  val error: String,
  /**
   * Human readable error description.
   */
  @Json(name = "error_description")
  val errorDescription: String,
  /**
   * URL to the related documentation and troubleshooting regarding this error.
   */
  @Json(name = "error_uri")
  val errorUri: String,
  /**
   * Error code used for referencing the docs, logs, and data stream.
   */
  @Json(name = "error_code")
  var errorCode: String,
  /**
   * The HTTP status code of the error response.
   */
  @Json(name = "request_id")
  override var requestId: String? = null,
  /**
   * The HTTP status code of the error response.
   */
  override var statusCode: Int? = null,
) : AbstractNylasApiError(error, statusCode, requestId)
