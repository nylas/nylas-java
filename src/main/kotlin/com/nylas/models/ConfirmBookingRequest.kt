package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of the request body for confirming a booking.
 */
data class ConfirmBookingRequest(
  @Json(name = "status")
  val status: String? = null,
  /**
   * The salt extracted from the booking reference embedded in the organizer confirmation link.
   */
  @Json(name = "salt")
  val salt: String? = null,
  /**
   * The reason the booking is being cancelled.
   */
  @Json(name = "cancellation_reason")
  val cancellationReason: String? = null,
) {
  /**
   * Builder for [ConfirmBookingRequest].
   */
  class Builder {
    private var status: String? = null
    private var salt: String? = null
    private var cancellationReason: String? = null

    /**
     * Set the status of the booking.
     * @param status The status of the booking.
     * @return The builder.
     */
    fun status(status: String) = apply { this.status = status }

    /**
     * Set the salt of the booking.
     * @param salt The salt of the booking.
     * @return The builder.
     */
    fun salt(salt: String) = apply { this.salt = salt }

    /**
     * Set the cancellation reason of the booking.
     * @param cancellationReason The cancellation reason of the booking.
     * @return The builder.
     */
    fun cancellationReason(cancellationReason: String) = apply { this.cancellationReason = cancellationReason }

    /**
     * Builds a [ConfirmBookingRequest] instance.
     * @return The [ConfirmBookingRequest] instance.
     */
    fun build() = ConfirmBookingRequest(status, salt, cancellationReason)
  }
}
