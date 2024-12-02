package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of an event booking.
 */
data class ConfigurationEventBooking(
  /**
   * The title of the event.
   */
  @Json(name = "title")
  val title: String? = null,
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
  /**
   * Whether to hide participants from the event booking.
   */
  @Json(name = "hide_participants")
  val hideParticipants: Boolean? = null,
) {
  /**
   * Builder for [ConfigurationEventBooking].
   */
  class Builder {
    private var title: String? = null
    private var description: String? = null
    private var location: String? = null
    private var timezone: String? = null
    private var bookingType: BookingType? = null
    private var conferencing: Conferencing? = null
    private var disableEmails: Boolean? = null
    private var reminders: List<BookingReminder>? = null
    private var hideParticipants: Boolean? = null

    /**
     * Set the title of the event.
     * @param title The title of the event.
     * @return The builder.
     */
    fun title(title: String?) = apply { this.title = title }

    /**
     * Sets the description of the event.
     * @param description The description of the event.
     * @return The builder.
     */
    fun description(description: String?) = apply { this.description = description }

    /**
     * Sets the location of the event.
     * @param location The location of the event.
     * @return The builder.
     */
    fun location(location: String?) = apply { this.location = location }

    /**
     * Sets the timezone for displaying times in confirmation email messages and reminders.
     * @param timezone The timezone for displaying times in confirmation email messages and reminders.
     * @return The builder.
     */
    fun timezone(timezone: String?) = apply { this.timezone = timezone }

    /**
     * Sets the type of booking.
     * @param bookingType The type of booking.
     * @return The builder.
     */
    fun bookingType(bookingType: BookingType?) = apply { this.bookingType = bookingType }

    /**
     * Sets the conference details for the event.
     * @param conferencing Conference details for the event.
     * @return The builder.
     */
    fun conferencing(conferencing: Conferencing?) = apply { this.conferencing = conferencing }

    /**
     * Sets whether Nylas sends email messages when an event is booked, cancelled, or rescheduled.
     * @param disableEmails Whether Nylas sends email messages when an event is booked, cancelled, or rescheduled.
     * @return The builder.
     */
    fun disableEmails(disableEmails: Boolean?) = apply { this.disableEmails = disableEmails }

    /**
     * Sets the list of reminders to send to participants before the event starts.
     * @param reminders The list of reminders to send to participants before the event starts.
     * @return
     */
    fun reminders(reminders: List<BookingReminder>?) = apply { this.reminders = reminders }

    /**
     * Sets whether to hide participants from the event booking.
     * @param hideParticipants Whether to hide participants from the event booking.
     * @return The builder.
     */
    fun hideParticipants(hideParticipants: Boolean?) = apply { this.hideParticipants = hideParticipants }

    /**
     * Builds an [ConfigurationEventBooking] instance.
     * @return The [ConfigurationEventBooking] instance.
     */
    fun build() = ConfigurationEventBooking(
      title,
      description,
      location,
      timezone,
      bookingType,
      conferencing,
      disableEmails,
      reminders,
      hideParticipants,
    )
  }
}
