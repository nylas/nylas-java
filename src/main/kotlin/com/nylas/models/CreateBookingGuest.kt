package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a request to create a booking guest.
 */
data class CreateBookingGuest(
  /**
   * The email address of the guest.
   */
  @Json(name = "email")
  val email: String,
  /**
   * The name of the guest.
   */
  @Json(name = "name")
  val name: String? = null,
) {
  /**
   * Builder for [CreateBookingGuest].
   */
  data class Builder(
    private val email: String,
  ) {
    private var name: String? = null

    /**
     * Set the name of the guest.
     * @param name The name of the guest.
     * @return The builder.
     */
    fun name(name: String) = apply { this.name = name }

    /**
     * Builds a [CreateBookingGuest] instance.
     * @return The [CreateBookingGuest] instance.
     */
    fun build() = CreateBookingGuest(email, name)
  }
}
