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
   * Field-level validation errors from the API.
   * Maps field names to their specific error messages.
   */
  @Json(name = "validation_errors")
  val validationErrors: Map<String, String>? = null,
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
) : AbstractNylasApiError(message, statusCode, requestId, headers) {

  override fun toString(): String {
    val sb = StringBuilder()
    sb.append("NylasApiError: $message")

    if (statusCode != null) {
      sb.append(" (HTTP $statusCode)")
    }

    if (!validationErrors.isNullOrEmpty()) {
      sb.append("\nValidation errors:")
      validationErrors.forEach { (field, error) ->
        sb.append("\n  - $field: $error")
      }
    }

    if (providerError != null) {
      sb.append("\nProvider error: $providerError")
    }

    if (requestId != null) {
      sb.append("\nRequest ID: $requestId")
    }

    return sb.toString()
  }
}
