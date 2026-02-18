package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a specific date and time range when a participant is available.
 * This can override the open_hours configurations for a specific date and time range.
 */
data class SpecificTimeAvailability(
  /**
   * The date in YYYY-MM-DD format.
   */
  @Json(name = "date")
  val date: String,
  /**
   * The start time in HH:MM format.
   */
  @Json(name = "start")
  val start: String,
  /**
   * The end time in HH:MM format.
   */
  @Json(name = "end")
  val end: String,
) {
  /**
   * A builder for creating a [SpecificTimeAvailability].
   * @param date The date in YYYY-MM-DD format.
   * @param start The start time in HH:MM format.
   * @param end The end time in HH:MM format.
   */
  data class Builder(
    private val date: String,
    private val start: String,
    private val end: String,
  ) {
    /**
     * Build the [SpecificTimeAvailability].
     * @return The [SpecificTimeAvailability].
     */
    fun build() = SpecificTimeAvailability(
      date,
      start,
      end,
    )
  }
}
