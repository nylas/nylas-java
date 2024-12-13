package com.nylas.models

import com.squareup.moshi.Json

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
