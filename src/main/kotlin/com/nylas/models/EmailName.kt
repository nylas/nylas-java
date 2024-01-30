package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing an email address and optional name.
 */
data class EmailName(
  /**
   * Email address.
   */
  @Json(name = "email")
  val email: String,
  /**
   * Full name.
   */
  @Json(name = "name")
  val name: String? = null,
)
