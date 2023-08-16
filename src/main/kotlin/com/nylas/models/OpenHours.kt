package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a participant's open hours.
 */
data class OpenHours(
  /**
   * The days of the week that the open hour settings will be applied to.
   * Sunday corresponds to 0 and Saturday corresponds to 6.
   */
  @Json(name = "days")
  val days: List<Int>? = null,
  /**
   * IANA time zone database formatted string (e.g. America/New_York).
   * @see <a href="https://en.wikipedia.org/wiki/List_of_tz_database_time_zones">List of tz database time zones</a>
   */
  @Json(name = "timezone")
  val timezone: String? = null,
  /**
   * Start time in 24-hour time format. Leading 0's are left off.
   */
  @Json(name = "start")
  val start: String? = null,
  /**
   * End time in 24-hour time format. Leading 0's are left off.
   */
  @Json(name = "end")
  val end: String? = null,
  /**
   * A list of dates that will be excluded from the open hours.
   * Dates should be formatted as YYYY-MM-DD.
   */
  @Json(name = "exdates")
  val exdates: List<String>? = null,
) {
  /**
   * Builder for [OpenHours].
   */
  class Builder {
    private var days: List<Int>? = null
    private var timezone: String? = null
    private var start: String? = null
    private var end: String? = null
    private var exdates: List<String>? = null

    /**
     * Sets the days of the week that the open hour settings will be applied to.
     * Sunday corresponds to 0 and Saturday corresponds to 6.
     * @param days The days of the week that the open hour settings will be applied to.
     * @return The builder.
     */
    fun days(days: List<Int>?) = apply { this.days = days }

    /**
     * Sets the IANA time zone database formatted string (e.g. America/New_York).
     * @see <a href="https://en.wikipedia.org/wiki/List_of_tz_database_time_zones">List of tz database time zones</a>
     * @param timezone The IANA time zone database formatted string.
     * @return The builder.
     */
    fun timezone(timezone: String?) = apply { this.timezone = timezone }

    /**
     * Sets the start time in 24-hour time format. Leading 0's are left off.
     * @param start The start time in 24-hour time format.
     * @return The builder.
     */
    fun start(start: String?) = apply { this.start = start }

    /**
     * Sets the end time in 24-hour time format. Leading 0's are left off.
     * @param end The end time in 24-hour time format.
     * @return The builder.
     */
    fun end(end: String?) = apply { this.end = end }

    /**
     * Sets the list of dates that will be excluded from the open hours.
     * Dates should be formatted as YYYY-MM-DD.
     * @param exdates The list of dates that will be excluded from the open hours.
     * @return The builder.
     */
    fun exdates(exdates: List<String>?) = apply { this.exdates = exdates }

    /**
     * Builds an [OpenHours] instance.
     * @return The [OpenHours] instance.
     */
    fun build() = OpenHours(
      days,
      timezone,
      start,
      end,
      exdates,
    )
  }
}
