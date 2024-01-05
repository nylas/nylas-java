package com.nylas.models

/**
 * Interface for email addresses in a contact.
 */
data class ContactEmail(
  val email: String? = null,
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
