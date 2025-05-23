package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a request to create a configuration.
 */
data class CreateConfigurationRequest(
  /**
   * List of participants included in the scheduled event.
   */
  @Json(name = "participants")
  val participants: List<ConfigurationParticipant>,
  /**
   * Rules that determine available time slots for the event.
   */
  @Json(name = "availability")
  val availability: ConfigurationAvailability,
  /**
   * Booking data for the event.
   */
  @Json(name = "event_booking")
  val eventBooking: ConfigurationEventBooking,
  /**
   * The name of the Scheduling Page. If not set, it defaults to the organizer's name.
   */
  @Json(name = "name")
  val name: String? = null,
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
   * A builder for creating a [CreateConfigurationRequest].
   * @param participants List of participants included in the scheduled event.
   * @param availability Rules that determine available time slots for the event.
   * @param eventBooking Booking data for the event.
   */
  data class Builder(
    private val participants: List<ConfigurationParticipant>,
    private val availability: ConfigurationAvailability,
    private val eventBooking: ConfigurationEventBooking,
  ) {
    private var requiresSessionAuth: Boolean? = null
    private var name: String? = null
    private var slug: String? = null
    private var scheduler: ConfigurationSchedulerSettings? = null
    private var appearance: Map<String, String>? = null

    /**
     * Set the name of the Scheduling Page.
     *
     * @param name The name of the Scheduling Page. If not set, it defaults to the organizer's name.
     * @return The builder
     */
    fun name(name: String) = apply { this.name = name }

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
     * Build the [CreateConfigurationRequest].
     *
     * @return The [CreateConfigurationRequest]
     */
    fun build() = CreateConfigurationRequest(
      participants,
      availability,
      eventBooking,
      name,
      slug,
      requiresSessionAuth,
      scheduler,
      appearance,
    )
  }
}
