package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a participant in a scheduled event.
 * @property email Email address of the participant.
 * @property name Name of the participant.
 * @property role Role of the participant.
 * @property availability Availability data for the participant.
 * @property booking Booking data for the participant.
 * @property timezone Timezone of the participant.
 */
data class ConfigurationParticipant(
  /**
   * Email address of the participant.
   */
  @Json(name = "email")
  val email: String,
  /**
   * Name of the participant.
   */
  @Json(name = "name")
  val name: String? = null,
  /**
   * If true, the participant is the organizer of the event.
   */
  @Json(name = "is_organizer")
  val isOrganizer: Boolean? = null,
  /**
   * Availability data for the participant.
   */
  @Json(name = "availability")
  val availability: ConfigurationAvailabilityParticipant? = null,
  /**
   * Booking data for the participant.
   */
  @Json(name = "booking")
  val booking: ConfigurationBookingParticipant? = null,
  /**
   * Timezone of the participant.
   */
  @Json(name = "timezone")
  val timezone: String? = null,
) {
  /**
   * A builder for creating a [ConfigurationParticipant].
   * @param email Email address of the participant.
   */
  data class Builder(
    private val email: String,
  ) {
    private var name: String? = null
    private var isOrganizer: Boolean? = null
    private var availability: ConfigurationAvailabilityParticipant? = null
    private var booking: ConfigurationBookingParticipant? = null
    private var timezone: String? = null

    /**
     * Set the name of the participant.
     * @param name Name of the participant.
     * @return The builder.
     */
    fun name(name: String) = apply { this.name = name }

    /**
     * Set if the participant is the organizer of the event.
     * @param isOrganizer If true, the participant is the organizer of the event.
     * @return The builder.
     */
    fun isOrganizer(isOrganizer: Boolean) = apply { this.isOrganizer = isOrganizer }

    /**
     * Set the availability data for the participant.
     * @param availability Availability data for the participant.
     * @return The builder.
     */
    fun availability(availability: ConfigurationAvailabilityParticipant) = apply { this.availability = availability }

    /**
     * Set the booking data for the participant.
     * @param booking Booking data for the participant.
     * @return The builder.
     */
    fun booking(booking: ConfigurationBookingParticipant) = apply { this.booking = booking }

    /**
     * Set the timezone of the participant.
     * @param timezone Timezone of the participant.
     * @return The builder.
     */
    fun timezone(timezone: String) = apply { this.timezone = timezone }

    /**
     * Build the [ConfigurationParticipant].
     * @return The [ConfigurationParticipant]
     */
    fun build() = ConfigurationParticipant(email, name, isOrganizer, availability, booking, timezone)
  }
}
