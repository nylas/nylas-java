package com.nylas.models

/**
 * Class representing an email address and optional name.
 */
data class EmailName(
  /**
   * Email address.
   */
  val email: String,
  /**
   * Full name.
   */
  val name: String? = null,
)
