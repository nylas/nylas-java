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
 * Class representation of an additional field.
 */
data class AdditionalField(
  /**
   * The text label to be displayed in the Scheduler UI.
   */
  @Json(name = "label")
  val label: String,
  /**
   * The field type.
   * Supported values are text, multi_line_text, email, phone_number, dropdown, date, checkbox, and radio_button.
   */
  @Json(name = "type")
  val type: AdditionalFieldType,
  /**
   * Whether the field is required to be filled out by the guest when booking an event.
   */
  @Json(name = "required")
  val required: Boolean,
  /**
   * A regular expression pattern that the value of the field must match.
   */
  @Json(name = "pattern")
  val pattern: String? = null,
  /**
   * The order in which the field will be displayed in the Scheduler UI.
   * Fields with lower order values will be displayed first.
   */
  @Json(name = "order")
  val order: Int? = null,
  /**
   * A list of options for the dropdown or radio_button types.
   * This field is required for the dropdown and radio_button types.
   */
  @Json(name = "options")
  val options: AdditonalFieldOptionsType? = null,
)

/**
 * Class representation of an email template.
 */
data class EmailTemplate(
  /**
   * Configurable settings specifically for booking confirmed emails.
   */
  @Json(name = "booking_confirmed")
  val bookingConfirmed: BookingConfirmedTemplate? = null,
)

/**
 * Class representation of booking confirmed template settings.
 */
data class BookingConfirmedTemplate(
  /**
   * The title to replace the default 'Booking Confirmed' title.
   */
  @Json(name = "title")
  val title: String? = null,
  /**
   * The additional body to be appended after the default body.
   */
  @Json(name = "body")
  val body: String? = null,
)

/**
 * Class representation of a booking reminder.
 */
data class BookingReminder(
  /**
   * The reminder type.
   */
  @Json(name = "type")
  val type: String,
  /**
   * The number of minutes before the event to send the reminder.
   */
  @Json(name = "minutes_before_event")
  val minutesBeforeEvent: Int,
  /**
   * The recipient of the reminder.
   */
  @Json(name = "recipient")
  val recipient: String? = null,
  /**
   * The subject of the email reminder.
   */
  @Json(name = "email_subject")
  val emailSubject: String? = null,
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
  val name: String,
)

/**
 * Class representation of a create booking request.
 */
data class CreateBookingRequest(
  /**
   * The event's start time, in Unix epoch format.
   */
  @Json(name = "start_time")
  val startTime: Long,
  /**
   * The event's end time, in Unix epoch format.
   */
  @Json(name = "end_time")
  val endTime: Long,
  /**
   * Details about the guest that is creating the booking.
   */
  @Json(name = "guest")
  val guest: BookingGuest,
  /**
   * List of participant email addresses from the Configuration object to include in the booking.
   */
  @Json(name = "participants")
  val participants: List<ConfigurationBookingParticipant>? = null,
  /**
   * The guest's timezone that is used in email notifications.
   */
  @Json(name = "timezone")
  val timezone: String? = null,
  /**
   * The language of the guest email notifications.
   */
  @Json(name = "email_language")
  val emailLanguage: EmailLanguage? = null,
  /**
   * List of additional guest email addresses to include in the booking.
   */
  @Json(name = "additional_guests")
  val additionalGuests: List<BookingGuest>? = null,
  /**
   * Dictionary of additional field keys mapped to values populated by the guest in the booking form.
   */
  @Json(name = "additional_fields")
  val additionalFields: Map<String, String>? = null,
)

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
 * Class representation of a confirm booking request.
 */
data class ConfirmBookingRequest(
  /**
   * The salt extracted from the booking reference embedded in the organizer confirmation link.
   */
  @Json(name = "salt")
  val salt: String,
  /**
   * The action to take on the pending booking.
   */
  @Json(name = "status")
  val status: ConfirmBookingStatus,
  /**
   * The reason the booking is being cancelled.
   */
  @Json(name = "cancellation_reason")
  val cancellationReason: String? = null,
)

/**
 * Class representation of a delete booking request.
 */
data class DeleteBookingRequest(
  /**
   * The reason the booking is being cancelled.
   */
  @Json(name = "cancellation_reason")
  val cancellationReason: String? = null,
)

/**
 * Class representation of a reschedule booking request.
 */
data class RescheduleBookingRequest(
  /**
   * The event's start time, in Unix epoch format.
   */
  @Json(name = "start_time")
  val startTime: Long,
  /**
   * The event's end time, in Unix epoch format.
   */
  @Json(name = "end_time")
  val endTime: Long,
)

/**
 * Enum for additional field types.
 */
enum class AdditionalFieldType {
  @Json(name = "text")
  TEXT,

  @Json(name = "multi_line_text")
  MULTI_LINE_TEXT,

  @Json(name = "email")
  EMAIL,

  @Json(name = "phone_number")
  PHONE_NUMBER,

  @Json(name = "dropdown")
  DROPDOWN,

  @Json(name = "date")
  DATE,

  @Json(name = "checkbox")
  CHECKBOX,

  @Json(name = "radio_button")
  RADIO_BUTTON,
}

/**
 * Enum for additional field options types.
 */
enum class AdditonalFieldOptionsType {
  @Json(name = "text")
  TEXT,

  @Json(name = "email")
  EMAIL,

  @Json(name = "phone_number")
  PHONE_NUMBER,

  @Json(name = "date")
  DATE,

  @Json(name = "checkbox")
  CHECKBOX,

  @Json(name = "radio_button")
  RADIO_BUTTON,
}

/**
 * Enum for booking types.
 */
enum class BookingType {
  @Json(name = "booking")
  BOOKING,

  @Json(name = "organizer-confirmation")
  ORGANIZER_CONFIRMATION,
}

/**
 * Enum for email language options.
 */
enum class EmailLanguage {
  @Json(name = "en")
  EN,

  @Json(name = "es")
  ES,

  @Json(name = "fr")
  FR,

  @Json(name = "de")
  DE,

  @Json(name = "nl")
  NL,

  @Json(name = "sv")
  SV,

  @Json(name = "ja")
  JA,

  @Json(name = "zh")
  ZH,
}

/**
 * Enum for booking statuses.
 */
enum class BookingStatus {
  @Json(name = "pending")
  PENDING,

  @Json(name = "confirmed")
  CONFIRMED,

  @Json(name = "cancelled")
  CANCELLED,
}

/**
 * Enum for confirm booking statuses.
 */
enum class ConfirmBookingStatus {
  @Json(name = "confirm")
  CONFIRM,

  @Json(name = "cancel")
  CANCEL,
}
