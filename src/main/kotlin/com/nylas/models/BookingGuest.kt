package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a booking guest.
 */
data class BookingGuest(
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
)
