package com.nylas.models

/**
 * Class representation for phone numbers in a contact.
 */
data class PhoneNumber(
  val number: String? = null,
  val type: ContactType? = null,
) {
  class Builder {
    private var number: String? = null
    private var type: ContactType? = null

    fun number(number: String) = apply { this.number = number }
    fun type(type: ContactType) = apply { this.type = type }
    fun build() = PhoneNumber(number, type)
  }
}
