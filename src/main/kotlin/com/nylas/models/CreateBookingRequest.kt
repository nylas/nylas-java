package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a confirm booking request.
 */
data class CreateBookingRequest(
  /**
   * The salt extracted from the booking reference embedded in the organizer confirmation link.
   */
  @Json(name = "salt")
  val salt: String,
  /**
   * The action to take on the pending booking.
   */
  @Json(name = "status")
  val status: ConfirmBookingStatus? = null,
  /**
   * The reason the booking is being cancelled.
   */
  @Json(name = "cancellation_reason")
  val cancellationReason: String? = null,
) {
  /**
   * Builder for [CreateBookingRequest].
   */
  data class Builder(
    private val salt: String,
  ) {
    private var status: ConfirmBookingStatus? = null
    private var cancellationReason: String? = null

    /**
     * Sets the status of the booking.
     * @param status The status of the booking.
     * @return The builder.
     */
    fun status(status: ConfirmBookingStatus) = apply { this.status = status }

    /**
     * Sets the cancellation reason of the booking.
     * @param cancellationReason The cancellation reason of the booking.
     * @return The builder.
     */
    fun cancellationReason(cancellationReason: String) = apply { this.cancellationReason = cancellationReason }

    /**
     * Builds a [CreateBookingRequest] instance.
     * @return The [CreateBookingRequest] instance.
     */
    fun build() = CreateBookingRequest(salt, status, cancellationReason)
  }
}
