package com.nylas.models

/**
 * Base class for all Nylas SDK errors.
 * @param message The error message.
 */
sealed class AbstractNylasSdkError(
  override val message: String,
) : Exception(message)
