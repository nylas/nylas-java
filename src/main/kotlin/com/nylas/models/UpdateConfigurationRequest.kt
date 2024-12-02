package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a request to update a configuration.
 */
data class UpdateConfigurationRequest(
  /**
   * List of participants included in the scheduled event.
   */
  @Json(name = "participants")
  val participants: List<ConfigurationParticipant>? = null,
  /**
   * Rules that determine available time slots for the event.
   */
  @Json(name = "availability")
  val availability: ConfigurationAvailability? = null,
  /**
   * Booking data for the event.
   */
  @Json(name = "event_booking")
  val eventBooking: ConfigurationEventBooking? = null,
  /**
   * Unique identifier for the Configuration object.
   */
  @Json(name = "slug")
  val slug: String? = null,
  /**
   * If true, scheduling Availability and Bookings endpoints require a valid session ID.
   */
  @Json(name = "requires_session_auth")
  val requiresSessionAuth: Boolean? = null,
  /**
   * Settings for the Scheduler UI.
   */
  @Json(name = "scheduler")
  val scheduler: ConfigurationSchedulerSettings? = null,
  /**
   * Appearance settings for the Scheduler UI.
   */
  @Json(name = "appearance")
  val appearance: Map<String, String>? = null,
) {
  /**
   * A builder for creating a [UpdateConfigurationRequest].
   */
  class Builder {
    private var participants: List<ConfigurationParticipant>? = null
    private var availability: ConfigurationAvailability? = null
    private var eventBooking: ConfigurationEventBooking? = null
    private var requiresSessionAuth: Boolean? = null
    private var slug: String? = null
    private var scheduler: ConfigurationSchedulerSettings? = null
    private var appearance: Map<String, String>? = null

    /**
     * Set the list of participants included in the scheduled event.
     * @param participants List of participants included in the scheduled event.
     * @return The builder
     */
    fun participants(participants: List<ConfigurationParticipant>) = apply { this.participants = participants }

    /**
     * Set the rules that determine available time slots for the event.
     * @param availability Rules that determine available time slots for the event.
     * @return The builder
     */
    fun availability(availability: ConfigurationAvailability) = apply { this.availability = availability }

    /**
     * Set the booking data for the event.
     * @param eventBooking Booking data for the event.
     * @return The builder
     */
    fun eventBooking(eventBooking: ConfigurationEventBooking) = apply { this.eventBooking = eventBooking }

    /**
     * Set the unique identifier for the configuration.
     *
     * @param slug Unique identifier for the Configuration object.
     * @return The builder
     */
    fun slug(slug: String) = apply { this.slug = slug }

    /**
     * Set if scheduling Availability and Bookings endpoints require a valid session ID.
     *
     * @param requiresSessionAuth If true, scheduling Availability and Bookings endpoints require a valid session ID.
     * @return The builder
     */
    fun requiresSessionAuth(requiresSessionAuth: Boolean) = apply { this.requiresSessionAuth = requiresSessionAuth }

    /**
     * Set the settings for the Scheduler UI.
     *
     * @param scheduler Settings for the Scheduler UI.
     * @return The builder
     */
    fun scheduler(scheduler: ConfigurationSchedulerSettings) = apply { this.scheduler = scheduler }

    /**
     * Set the appearance settings for the Scheduler UI.
     *
     * @param appearance Appearance settings for the Scheduler UI.
     * @return The builder
     */
    fun appearance(appearance: Map<String, String>) = apply { this.appearance = appearance }

    /**
     * Build the [UpdateConfigurationRequest].
     *
     * @return The [UpdateConfigurationRequest]
     */
    fun build() = UpdateConfigurationRequest(
      participants,
      availability,
      eventBooking,
      slug,
      requiresSessionAuth,
      scheduler,
      appearance,
    )
  }
}
