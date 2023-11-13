package com.nylas.models

/**
 * Class representing a message header.
 */
data class MessageHeaders(
  /**
   * The header name.
   */
  val name: String,
  /**
   * The header value.
   */
  val value: String,
)
