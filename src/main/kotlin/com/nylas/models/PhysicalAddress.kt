package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation for a physical address in a contact.
 */
data class PhysicalAddress(
  @Json(name = "format")
  val format: String? = null,
  @Json(name = "street_address")
  val streetAddress: String? = null,
  @Json(name = "city")
  val city: String? = null,
  @Json(name = "postal_code")
  val postalCode: String? = null,
  @Json(name = "state")
  val state: String? = null,
  @Json(name = "country")
  val country: String? = null,
  @Json(name = "type")
  val type: String? = null,
) {
  class Builder {
    private var format: String? = null
    private var streetAddress: String? = null
    private var city: String? = null
    private var postalCode: String? = null
    private var state: String? = null
    private var country: String? = null
    private var type: String? = null

    fun format(format: String) = apply { this.format = format }

    fun streetAddress(streetAddress: String) = apply { this.streetAddress = streetAddress }

    fun city(city: String) = apply { this.city = city }

    fun postalCode(postalCode: String) = apply { this.postalCode = postalCode }

    fun state(state: String) = apply { this.state = state }

    fun country(country: String) = apply { this.country = country }

    fun type(type: String) = apply { this.type = type }

    fun build() =
      PhysicalAddress(
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
