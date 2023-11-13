package com.nylas.models

/**
 * Class representing the query parameters for finding a message.
 */
data class FindMessageQueryParams(
  /**
   * Allows you to specify to the message with headers included.
   */
  val fields: MessageFields? = null,
)
