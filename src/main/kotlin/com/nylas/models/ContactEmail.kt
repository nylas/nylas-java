package com.nylas.models

import com.squareup.moshi.Json

/**
 * Interface for email addresses in a contact.
 */
data class ContactEmail(
  @Json(name = "email")
  val email: String? = null,
  @Json(name = "type")
  val type: String? = null,
) {
  class Builder {
    private var email: String? = null
    private var type: String? = null

    fun email(email: String) = apply { this.email = email }

    fun type(type: String) = apply { this.type = type }

    fun build() = ContactEmail(email, type)
  }
}
