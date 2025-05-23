package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas scheduler configuration.
 */
data class Configuration(
  /**
   * Globally unique object identifier.
   */
  @Json(name = "id")
  val id: String,
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
)

/**
 * Class representation of a session.
 */
data class Session(
  /**
   * The ID of the session.
   */
  @Json(name = "session_id")
  val sessionId: String,
)

/**
 * Class representation of a booking.
 */
data class Booking(
  /**
   * The unique ID of the booking.
   */
  @Json(name = "booking_id")
  val bookingId: String,
  /**
   * The unique ID of the event associated with the booking.
   */
  @Json(name = "event_id")
  val eventId: String,
  /**
   * The title of the event.
   */
  @Json(name = "title")
  val title: String,
  /**
   * The participant designated as the organizer of the event.
   */
  @Json(name = "organizer")
  val organizer: BookingOrganizer,
  /**
   * The current status of the booking.
   */
  @Json(name = "status")
  val status: BookingStatus,
  /**
   * The description of the event.
   */
  @Json(name = "description")
  val description: String? = null,
)

/**
 * Class representation of a booking participant.
 */
data class BookingParticipant(
  @Json(name = "email")
  val email: String,
)
