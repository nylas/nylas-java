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
  /**
   * IANA time zone database formatted string (e.g. America/Toronto).
   * @see <a href="https://en.wikipedia.org/wiki/List_of_tz_database_time_zones">List of tz database time zones</a>
   */
  @Json(name = "timezone")
  val timezone: String,
) {
  /**
   * A builder for creating a [SpecificTimeAvailability].
   * @param date The date in YYYY-MM-DD format.
   * @param start The start time in HH:MM format.
   * @param end The end time in HH:MM format.
   * @param timezone IANA time zone database formatted string (e.g. America/Toronto).
   */
  data class Builder(
    private val date: String,
    private val start: String,
    private val end: String,
    private val timezone: String,
  ) {
    /**
     * Build the [SpecificTimeAvailability].
     * @return The [SpecificTimeAvailability].
     */
    fun build() = SpecificTimeAvailability(
      date,
      start,
      end,
      timezone,
    )
  }
}
