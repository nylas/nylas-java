package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing a request to create a booking organizer.
 */
data class CreateBookingOrganizer(
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
) {
  /**
   * Builder for [CreateBookingOrganizer].
   */
  data class Builder(
    private val email: String,
  ) {
    private var name: String? = null

    /**
     * Set the name of the participant designated as the organizer of the event.
     * @param name The name of the participant designated as the organizer of the event.
     * @return The builder.
     */
    fun name(name: String) = apply { this.name = name }

    /**
     * Builds a [CreateBookingOrganizer] instance.
     * @return The [CreateBookingOrganizer] instance.
     */
    fun build() = CreateBookingOrganizer(email, name)
  }
}
