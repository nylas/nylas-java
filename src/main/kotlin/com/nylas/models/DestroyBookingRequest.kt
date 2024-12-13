package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a request to destroy a booking.
 */
data class DestroyBookingRequest(
  /**
   * The reason for the cancellation of the booking.
   */
  @Json(name = "cancellation_reason")
  val cancellationReason: String,
) {
  /**
   * Builder for [DestroyBookingRequest].
   */
  data class Builder(
    private val cancellationReason: String,
  ) {
    /**
     * Builds a [DestroyBookingRequest] instance.
     * @return The [DestroyBookingRequest] instance.
     */
    fun build() = DestroyBookingRequest(cancellationReason)
  }
}
