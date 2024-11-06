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
