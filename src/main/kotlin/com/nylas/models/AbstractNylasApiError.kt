package com.nylas.models

sealed class AbstractNylasApiError(
  override val message: String,
  open var statusCode: Int? = null,
) : Exception(message)
