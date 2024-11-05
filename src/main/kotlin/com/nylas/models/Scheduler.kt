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
  val participants: List<ConfigParticipant>,
  /**
   * Rules that determine available time slots for the event.
   */
  @Json(name = "availability")
  val availability: Availability,
  /**
   * Booking data for the event.
   */
  @Json(name = "event_booking")
  val eventBooking: EventBooking,
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
  val scheduler: SchedulerSettings? = null,
  /**
   * Appearance settings for the Scheduler UI.
   */
  @Json(name = "appearance")
  val appearance: Map<String, String>? = null,
)

/**
 * Class representation of a booking participant.
 */
data class ConfigParticipant(
  /**
   * Participant's email address.
   */
  @Json(name = "email")
  val email: String,
  /**
   * Availability data for the participant.
   */
  @Json(name = "availability")
  val availability: ParticipantAvailability,
  /**
   * Booking data for the participant.
   */
  @Json(name = "booking")
  val booking: ParticipantBooking,
  /**
   * Participant's name.
   */
  @Json(name = "name")
  val name: String? = null,
  /**
   * Whether the participant is the organizer of the event.
   */
  @Json(name = "is_organizer")
  val isOrganizer: Boolean? = null,
  /**
   * The participant's timezone.
   */
  @Json(name = "timezone")
  val timezone: String? = null,
)

/**
 * Class representation of availability settings.
 */
data class Availability(
  /**
   * The total number of minutes the event should last.
   */
  @Json(name = "duration_minutes")
  val durationMinutes: Int,
  /**
   * The interval between meetings in minutes.
   */
  @Json(name = "interval_minutes")
  val intervalMinutes: Int? = null,
  /**
   * Nylas rounds each time slot to the nearest multiple of this number of minutes.
   */
  @Json(name = "round_to")
  val roundTo: Int? = null,
  /**
   * Availability rules for scheduling configuration.
   */
  @Json(name = "availability_rules")
  val availabilityRules: AvailabilityRules? = null,
)

/**
 * Class representation of participant availability.
 */
data class ParticipantAvailability(
  /**
   * List of calendar IDs associated with the participant's email address.
   */
  @Json(name = "calendar_ids")
  val calendarIds: List<String>,
  /**
   * Open hours for this participant. The endpoint searches for free time slots during these open hours.
   */
  @Json(name = "open_hours")
  val openHours: List<OpenHours>? = null,
)

/**
 * Class representation of a participant booking.
 */
data class ParticipantBooking(
  /**
   * The calendar ID that the event is created in.
   */
  @Json(name = "calendar_id")
  val calendarId: String,
)

/**
 * Class representation of an event booking.
 */
data class EventBooking(
  /**
   * The title of the event.
   */
  @Json(name = "title")
  val title: String,
  /**
   * The description of the event.
   */
  @Json(name = "description")
  val description: String? = null,
  /**
   * The location of the event.
   */
  @Json(name = "location")
  val location: String? = null,
  /**
   * The timezone for displaying times in confirmation email messages and reminders.
   */
  @Json(name = "timezone")
  val timezone: String? = null,
  /**
   * The type of booking.
   */
  @Json(name = "booking_type")
  val bookingType: BookingType? = null,
  /**
   * Conference details for the event.
   */
  @Json(name = "conferencing")
  val conferencing: Conferencing? = null,
  /**
   * Whether Nylas sends email messages when an event is booked, cancelled, or rescheduled.
   */
  @Json(name = "disable_emails")
  val disableEmails: Boolean? = null,
  /**
   * The list of reminders to send to participants before the event starts.
   */
  @Json(name = "reminders")
  val reminders: List<BookingReminder>? = null,
)

/**
 * Class representation of scheduler settings.
 */
data class SchedulerSettings(
  /**
   * Definitions for additional fields to be displayed in the Scheduler UI.
   */
  @Json(name = "additional_fields")
  val additionalFields: Map<String, AdditionalField>? = null,
  /**
   * Number of days in the future that Scheduler is available for scheduling events.
   */
  @Json(name = "available_days_in_future")
  val availableDaysInFuture: Int? = null,
  /**
   * Minimum number of minutes in the future that a user can make a new booking.
   */
  @Json(name = "min_booking_notice")
  val minBookingNotice: Int? = null,
  /**
   * Minimum number of minutes before a booking can be cancelled.
   */
  @Json(name = "min_cancellation_notice")
  val minCancellationNotice: Int? = null,
  /**
   * A message about the cancellation policy to display when booking an event.
   */
  @Json(name = "cancellation_policy")
  val cancellationPolicy: String? = null,
  /**
   * The URL used to reschedule bookings.
   */
  @Json(name = "rescheduling_url")
  val reschedulingUrl: String? = null,
  /**
   * The URL used to cancel bookings.
   */
  @Json(name = "cancellation_url")
  val cancellationUrl: String? = null,
  /**
   * The URL used to confirm or cancel pending bookings.
   */
  @Json(name = "organizer_confirmation_url")
  val organizerConfirmationUrl: String? = null,
  /**
   * The custom URL to redirect to once the booking is confirmed.
   */
  @Json(name = "confirmation_redirect_url")
  val confirmationRedirectUrl: String? = null,
  /**
   * Whether the option to reschedule an event is hidden in booking confirmations and notifications.
   */
  @Json(name = "hide_rescheduling_options")
  val hideReschedulingOptions: Boolean? = null,
  /**
   * Whether the option to cancel an event is hidden in booking confirmations and notifications.
   */
  @Json(name = "hide_cancellation_options")
  val hideCancellationOptions: Boolean? = null,
  /**
   * Whether to hide the additional guests field on the scheduling page.
   */
  @Json(name = "hide_additional_guests")
  val hideAdditionalGuests: Boolean? = null,
  /**
   * Configurable settings for booking emails.
   */
  @Json(name = "email_template")
  val emailTemplate: EmailTemplate? = null,
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
 * Class representation of a booking participant.
 */
data class BookingParticipant(
  /**
   * The email address of the participant to include in the booking.
   */
  @Json(name = "email")
  val email: String,
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
  val participants: List<BookingParticipant>? = null,
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
  @Json(name = "text") TEXT,
  @Json(name = "multi_line_text") MULTI_LINE_TEXT,
  @Json(name = "email") EMAIL,
  @Json(name = "phone_number") PHONE_NUMBER,
  @Json(name = "dropdown") DROPDOWN,
  @Json(name = "date") DATE,
  @Json(name = "checkbox") CHECKBOX,
  @Json(name = "radio_button") RADIO_BUTTON,
}

/**
 * Enum for additional field options types.
 */
enum class AdditonalFieldOptionsType {
  @Json(name = "text") TEXT,
  @Json(name = "email") EMAIL,
  @Json(name = "phone_number") PHONE_NUMBER,
  @Json(name = "date") DATE,
  @Json(name = "checkbox") CHECKBOX,
  @Json(name = "radio_button") RADIO_BUTTON,
}

/**
 * Enum for booking types.
 */
enum class BookingType {
  @Json(name = "booking") BOOKING,
  @Json(name = "organizer-confirmation") ORGANIZER_CONFIRMATION,
}

/**
 * Enum for email language options.
 */
enum class EmailLanguage {
  @Json(name = "en") EN,
  @Json(name = "es") ES,
  @Json(name = "fr") FR,
  @Json(name = "de") DE,
  @Json(name = "nl") NL,
  @Json(name = "sv") SV,
  @Json(name = "ja") JA,
  @Json(name = "zh") ZH,
}

/**
 * Enum for booking statuses.
 */
enum class BookingStatus {
  @Json(name = "pending") PENDING,
  @Json(name = "confirmed") CONFIRMED,
  @Json(name = "cancelled") CANCELLED,
}

/**
 * Enum for confirm booking statuses.
 */
enum class ConfirmBookingStatus {
  @Json(name = "confirm") CONFIRM,
  @Json(name = "cancel") CANCEL,
}
