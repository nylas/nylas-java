package com.nylas.models

import com.squareup.moshi.Json

data class RescheduleBookingRequest(
  /**
   * The event's start time, in Unix epoch format.
   */
  @Json(name = "start_time")
  val startTime: Int? = null,
  /**
   * The event's end time, in Unix epoch format.
   */
  @Json(name = "end_time")
  val endTime: Int? = null,
) {
  /**
   * Builder for [RescheduleBookingRequest].
   */
  class Builder {
    private var startTime: Int? = null
    private var endTime: Int? = null

    /**
     * Set the start time of the booking.
     * @param startTime The start time of the booking.
     * @return The builder.
     */
    fun startTime(startTime: Int) = apply { this.startTime = startTime }

    /**
     * Set the end time of the booking.
     * @param endTime The end time of the booking.
     * @return The builder.
     */
    fun endTime(endTime: Int) = apply { this.endTime = endTime }

    /**
     * Builds a [RescheduleBookingRequest] instance.
     * @return The [RescheduleBookingRequest] instance.
     */
    fun build() = RescheduleBookingRequest(startTime, endTime)
  }
}
