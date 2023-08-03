package com.nylas.models

sealed class AbstractNylasSdkError(
  override val message: String,
) : Exception(message)
