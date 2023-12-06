package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of the query parameters for sending RSVP.
 */
data class SendRsvpQueryParams(
  /**
   * The RSVP status for the event. Must be yes, no, or maybe
   */
  @Json(name = "status")
  val status: String
) : IQueryParams {

  /**
   * Builder for [SendRsvpQueryParams].
   * @param status The RSVP status for the event. Must be yes, no, or maybe
   */
  data class Builder(private val status: String) {

    /**
     * Builds a [SendRsvpQueryParams] instance.
     * @return The [SendRsvpQueryParams] instance.
     */
    fun build() = SendRsvpQueryParams(status)
  }
}
