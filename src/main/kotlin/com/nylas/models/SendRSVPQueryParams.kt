package com.nylas.models

import com.squareup.moshi.Json

/**
 * Enum representing the allowed RSVP status values.
 */
enum class RsvpStatus(val value: String) {
  YES("yes"),
  NO("no"),
  MAYBE("maybe");

  override fun toString(): String {
    return value
  }
}

/**
 * Class representation of the query parameters for sending RSVP.
 */
data class SendRsvpQueryParams(
  /**
   * The RSVP status for the event. Must be yes, no, or maybe
   */
  @Json(name = "status")
  val status: RsvpStatus
) : IQueryParams {

  /**
   * Builder for [SendRsvpQueryParams].
   * @param status The RSVP status for the event. Must be yes, no, or maybe
   */
  data class Builder(private val status: RsvpStatus) {

    /**
     * Builds a [SendRsvpQueryParams] instance.
     * @return The [SendRsvpQueryParams] instance.
     */
    fun build() = SendRsvpQueryParams(status)
  }
}
