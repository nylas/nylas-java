package com.nylas.models

import com.squareup.moshi.Json

/**
 * Interface for email addresses in a contact.
 */
data class ContactEmail(
  @Json(name = "email")
  val email: String? = null,
  @Json(name = "type")
  val type: ContactType? = null,
) {
  class Builder {
    private var email: String? = null
    private var type: ContactType? = null

    fun email(email: String) = apply { this.email = email }
    fun type(type: ContactType) = apply { this.type = type }
    fun build() = ContactEmail(email, type)
  }
}
