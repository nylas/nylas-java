package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a general Nylas API error.
 */
data class NylasApiError(
  /**
   * Error type.
   */
  @Json(name = "type")
  val type: String,
  /**
   * Error message.
   */
  @Json(name = "message")
  override val message: String,
  /**
   * Provider Error.
   */
  @Json(name = "provider_error")
  val providerError: Map<String, Any?>? = null,
  /**
   * The HTTP status code of the error response
   */
  override var statusCode: Int? = null,
  /**
   * The HTTP status code of the error response.
   */
  override var requestId: String? = null,
  /**
   * The HTTP headers of the error response
   */
  override var headers: Map<String, List<String>>? = null,
) : AbstractNylasApiError(message, statusCode, requestId, headers)
