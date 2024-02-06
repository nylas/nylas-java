package com.nylas.models

/**
 * Base class for all Nylas API errors.
 * @param message The error message.
 * @param statusCode The HTTP status code of the error response.
 * @param requestId The unique identifier of the request.
 */
sealed class AbstractNylasApiError(
  override val message: String,
  open var statusCode: Int? = null,
  open var requestId: String? = null,
) : Exception(message)
