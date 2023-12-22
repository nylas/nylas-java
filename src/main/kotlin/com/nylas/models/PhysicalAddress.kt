package com.nylas.models

/**
 * Class representation for a physical address in a contact.
 */
data class PhysicalAddress(
  val format: String? = null,
  val streetAddress: String? = null,
  val city: String? = null,
  val postalCode: String? = null,
  val state: String? = null,
  val country: String? = null,
  val type: ContactType? = null,
) {
  class Builder {
    private var format: String? = null
    private var streetAddress: String? = null
    private var city: String? = null
    private var postalCode: String? = null
    private var state: String? = null
    private var country: String? = null
    private var type: ContactType? = null

    fun format(format: String) = apply { this.format = format }
    fun streetAddress(streetAddress: String) = apply { this.streetAddress = streetAddress }
    fun city(city: String) = apply { this.city = city }
    fun postalCode(postalCode: String) = apply { this.postalCode = postalCode }
    fun state(state: String) = apply { this.state = state }
    fun country(country: String) = apply { this.country = country }
    fun type(type: ContactType) = apply { this.type = type }

    fun build() = PhysicalAddress(
      format = format,
      streetAddress = streetAddress,
      city = city,
      postalCode = postalCode,
      state = state,
      country = country,
      type = type,
    )
  }
}
