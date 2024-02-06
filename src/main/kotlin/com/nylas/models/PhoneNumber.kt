package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation for phone numbers in a contact.
 */
data class PhoneNumber(
  @Json(name = "number")
  val number: String? = null,
  @Json(name = "type")
  val type: String? = null,
) {
  class Builder {
    private var number: String? = null
    private var type: String? = null

    fun number(number: String) = apply { this.number = number }

    fun type(type: String) = apply { this.type = type }

    fun build() = PhoneNumber(number, type)
  }
}
