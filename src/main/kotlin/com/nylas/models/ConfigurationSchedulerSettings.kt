package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of scheduler settings.
 */
data class ConfigurationSchedulerSettings(
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
) {
  class Builder {
    private var additionalFields: Map<String, AdditionalField>? = null
    private var availableDaysInFuture: Int? = null
    private var minBookingNotice: Int? = null
    private var minCancellationNotice: Int? = null
    private var cancellationPolicy: String? = null
    private var reschedulingUrl: String? = null
    private var cancellationUrl: String? = null
    private var organizerConfirmationUrl: String? = null
    private var confirmationRedirectUrl: String? = null
    private var hideReschedulingOptions: Boolean? = null
    private var hideCancellationOptions: Boolean? = null
    private var hideAdditionalGuests: Boolean? = null
    private var emailTemplate: EmailTemplate? = null

    /**
     * Set the definitions for additional fields to be displayed in the Scheduler UI.
     *
     * @param additionalFields Definitions for additional fields to be displayed in the Scheduler UI.
     * @return The builder.
     */
    fun additionalFields(additionalFields: Map<String, AdditionalField>) = apply { this.additionalFields = additionalFields }

    /**
     * Set the number of days in the future that Scheduler is available for scheduling events.
     *
     * @param availableDaysInFuture Number of days in the future that Scheduler is available for scheduling events.
     * @return The builder.
     */
    fun availableDaysInFuture(availableDaysInFuture: Int) = apply { this.availableDaysInFuture = availableDaysInFuture }

    /**
     * Set the minimum number of minutes in the future that a user can make a new booking.
     *
     * @param minBookingNotice Minimum number of minutes in the future that a user can make a new booking.
     * @return The builder.
     */
    fun minBookingNotice(minBookingNotice: Int) = apply { this.minBookingNotice = minBookingNotice }

    /**
     * Set the minimum number of minutes before a booking can be cancelled.
     *
     * @param minCancellationNotice Minimum number of minutes before a booking can be cancelled.
     * @return The builder.
     */
    fun minCancellationNotice(minCancellationNotice: Int) = apply { this.minCancellationNotice = minCancellationNotice }

    /**
     * Set a message about the cancellation policy to display when booking an event.
     *
     * @param cancellationPolicy A message about the cancellation policy to display when booking an event.
     * @return The builder.
     */
    fun cancellationPolicy(cancellationPolicy: String) = apply { this.cancellationPolicy = cancellationPolicy }

    /**
     * Set the URL used to reschedule bookings.
     *
     * @param reschedulingUrl The URL used to reschedule bookings.
     * @return The builder.
     */
    fun reschedulingUrl(reschedulingUrl: String) = apply { this.reschedulingUrl = reschedulingUrl }

    /**
     * Set the URL used to cancel bookings.
     *
     * @param cancellationUrl The URL used to cancel bookings.
     * @return The builder.
     */
    fun cancellationUrl(cancellationUrl: String) = apply { this.cancellationUrl = cancellationUrl }

    /**
     * Set the URL used to confirm or cancel pending bookings.
     *
     * @param organizerConfirmationUrl The URL used to confirm or cancel pending bookings.
     * @return The builder.
     */
    fun organizerConfirmationUrl(organizerConfirmationUrl: String) = apply { this.organizerConfirmationUrl = organizerConfirmationUrl }

    /**
     * Set the custom URL to redirect to once the booking is confirmed.
     *
     * @param confirmationRedirectUrl The custom URL to redirect to once the booking is confirmed.
     * @return The builder.
     */
    fun confirmationRedirectUrl(confirmationRedirectUrl: String) = apply { this.confirmationRedirectUrl = confirmationRedirectUrl }

    /**
     * Set whether the option to reschedule an event is hidden in booking confirmations and notifications.
     *
     * @param hideReschedulingOptions Whether the option to reschedule an event is hidden in booking confirmations and notifications.
     * @return The builder.
     */
    fun hideReschedulingOptions(hideReschedulingOptions: Boolean) = apply { this.hideReschedulingOptions = hideReschedulingOptions }

    /**
     * Set whether the option to cancel an event is hidden in booking confirmations and notifications.
     *
     * @param hideCancellationOptions Whether the option to cancel an event is hidden in booking confirmations and notifications.
     * @return The builder.
     */
    fun hideCancellationOptions(hideCancellationOptions: Boolean) = apply { this.hideCancellationOptions = hideCancellationOptions }

    /**
     * Set whether to hide the additional guests field on the scheduling page.
     *
     * @param hideAdditionalGuests Whether to hide the additional guests field on the scheduling page.
     * @return The builder.
     */
    fun hideAdditionalGuests(hideAdditionalGuests: Boolean) = apply { this.hideAdditionalGuests = hideAdditionalGuests }

    /**
     * Set the configurable settings for booking emails.
     *
     * @param emailTemplate Configurable settings for booking emails.
     * @return The builder.
     */
    fun emailTemplate(emailTemplate: EmailTemplate) = apply { this.emailTemplate = emailTemplate }

    /**
     * Build the [ConfigurationSchedulerSettings].
     *
     * @return The [ConfigurationSchedulerSettings]
     */
    fun build() = ConfigurationSchedulerSettings(
      additionalFields,
      availableDaysInFuture,
      minBookingNotice,
      minCancellationNotice,
      cancellationPolicy,
      reschedulingUrl,
      cancellationUrl,
      organizerConfirmationUrl,
      confirmationRedirectUrl,
      hideReschedulingOptions,
      hideCancellationOptions,
      hideAdditionalGuests,
      emailTemplate,
    )
  }
}

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
  /**
   * URL of a custom logo to appear in booking emails.
   */
  @Json(name = "logo")
  val logo: String? = null,
  /**
   * Boolean flag to toggle Nylas branding visibility.
   */
  @Json(name = "show_nylas_branding")
  val showNylasBranding: Boolean? = null,
) {
  /**
   * Builder for [EmailTemplate].
   */
  class Builder {
    private var bookingConfirmed: BookingConfirmedTemplate? = null
    private var logo: String? = null
    private var showNylasBranding: Boolean? = null

    /**
     * Set the configurable settings specifically for booking confirmed emails.
     *
     * @param bookingConfirmed Configurable settings specifically for booking confirmed emails.
     * @return The builder.
     */
    fun bookingConfirmed(bookingConfirmed: BookingConfirmedTemplate) = apply { this.bookingConfirmed = bookingConfirmed }

    /**
     * Set the URL of a custom logo to appear in booking emails.
     *
     * @param logo URL of a custom logo to appear in booking emails.
     * @return The builder.
     */
    fun logo(logo: String) = apply { this.logo = logo }

    /**
     * Set the boolean flag to toggle Nylas branding visibility.
     *
     * @param showNylasBranding Boolean flag to toggle Nylas branding visibility.
     * @return The builder.
     */
    fun showNylasBranding(showNylasBranding: Boolean) = apply { this.showNylasBranding = showNylasBranding }

    /**
     * Build the [EmailTemplate].
     *
     * @return The [EmailTemplate]
     */
    fun build() = EmailTemplate(
      bookingConfirmed,
      logo,
      showNylasBranding,
    )
  }
}

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
