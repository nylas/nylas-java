package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a booking organizer.
 */
data class BookingOrganizer(
  /**
   * The email address of the participant designated as the organizer of the event.
   */
  @Json(name = "email")
  val email: String,
  /**
   * The name of the participant designated as the organizer of the event.
   */
  @Json(name = "name")
  val name: String? = null,
)
