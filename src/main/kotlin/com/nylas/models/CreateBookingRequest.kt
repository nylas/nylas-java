package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a confirm booking request.
 */
data class CreateBookingRequest(
  /**
   * The event's start time, in Unix epoch format.
   */
  @Json(name = "start_time")
  val startTime: String? = null,
  /**
   * The event's end time, in Unix epoch format.
   */
  @Json(name = "end_time")
  val endTime: String? = null,
  /**
   * An array of objects that include a list of participant
   * email addresses from the Configuration object to include in the booking.
   * If not provided, Nylas includes all participants from the Configuration object.
   */
  @Json(name = "participants")
  val participants: List<BookingParticipant>? = null,
  /**
   * Details about the guest that is creating the booking. The guest name and email are required.
   */
  @Json(name = "guest")
  val guest: BookingGuest? = null,
  /**
   * The guest's timezone that is used in email notifications.
   */
  @Json(name = "timezone")
  val timezone: String? = null,
  /**
   * The language of the guest's email notifications.
   */
  @Json(name = "email_language")
  val emailLanguage: EmailLanguage? = null,
  /**
   * An array of objects that include a list of additional guest email addresses to include in the booking.
   */
  @Json(name = "additional_guests")
  val additionalGuests: List<BookingGuest>? = null,
  /**
   * A dictionary of additional field keys mapped to the values populated by the guest in the booking form.
   */
  @Json(name = "additional_fields")
  val additionalFields: Map<String, String>? = null,
) {
  /**
   * Builder for [CreateBookingRequest].
   */
  class Builder {
    private var startTime: String? = null
    private var endTime: String? = null
    private var participants: List<BookingParticipant>? = null
    private var guest: BookingGuest? = null
    private var timezone: String? = null
    private var emailLanguage: EmailLanguage? = null
    private var additionalGuests: List<BookingGuest>? = null
    private var additionalFields: Map<String, String>? = null

    /**
     * Set the start time of the booking.
     * @param startTime The start time of the booking.
     * @return The builder.
     */
    fun startTime(startTime: String) = apply { this.startTime = startTime }

    /**
     * Set the end time of the booking.
     * @param endTime The end time of the booking.
     * @return The builder.
     */
    fun endTime(endTime: String) = apply { this.endTime = endTime }

    /**
     * Set the participants of the booking.
     * @param participants The participants of the booking.
     * @return The builder.
     */
    fun participants(participants: List<BookingParticipant>) = apply { this.participants = participants }

    /**
     * Set the guests of the booking.
     * @param guests The guests of the booking.
     * @return The builder.
     */
    fun guest(guest: BookingGuest) = apply { this.guest = guest }

    /**
     * Set the timezone of the booking.
     * @param timezone The timezone of the booking.
     * @return The builder.
     */
    fun timezone(timezone: String) = apply { this.timezone = timezone }

    /**
     * Set the email language of the booking.
     * @param emailLanguage The email language of the booking.
     * @return The builder.
     */
    fun emailLanguage(emailLanguage: EmailLanguage) = apply { this.emailLanguage = emailLanguage }

    /**
     * Set the additional guests of the booking.
     * @param additionalGuests The additional guests of the booking.
     * @return The builder.
     */
    fun additionalGuests(additionalGuests: List<BookingGuest>) = apply { this.additionalGuests = additionalGuests }

    /**
     * Set the additional fields of the booking.
     * @param additionalFields The additional fields of the booking.
     * @return The builder.
     */
    fun additionalFields(additionalFields: Map<String, String>) = apply { this.additionalFields = additionalFields }

    /**
     * Builds a [CreateBookingRequest] instance.
     * @return The [CreateBookingRequest] instance.
     */
    fun build() = CreateBookingRequest(startTime, endTime, participants, guest, timezone, emailLanguage, additionalGuests, additionalFields)
  }
}
