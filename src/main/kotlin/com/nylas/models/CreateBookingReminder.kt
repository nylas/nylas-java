package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a request to create a booking reminder.
 */
data class CreateBookingReminder(
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
) {
  /**
   * Builder for [CreateBookingReminder].
   */
  data class Builder(
    private val type: String,
    private val minutesBeforeEvent: Int,
  ) {
    private var recipient: String? = null
    private var emailSubject: String? = null

    /**
     * Set the recipient of the reminder.
     * @param recipient The recipient of the reminder.
     * @return The builder.
     */
    fun recipient(recipient: String) = apply { this.recipient = recipient }

    /**
     * Set the subject of the email reminder.
     * @param emailSubject The subject of the email reminder.
     * @return The builder.
     */
    fun emailSubject(emailSubject: String) = apply { this.emailSubject = emailSubject }

    /**
     * Builds a [CreateBookingReminder] instance.
     * @return The [CreateBookingReminder] instance.
     */
    fun build() = CreateBookingReminder(type, minutesBeforeEvent, recipient, emailSubject)
  }
}
