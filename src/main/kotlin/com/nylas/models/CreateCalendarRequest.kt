package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas create calendar request
 */
data class CreateCalendarRequest(
  /**
   * Name of the Calendar.
   */
  @Json(name = "name")
  val name: String,
  /**
   * Description of the calendar.
   */
  @Json(name = "description")
  val description: String? = null,
  /**
   * Geographic location of the calendar as free-form text.
   */
  @Json(name = "location")
  val location: String? = null,
  /**
   * IANA time zone database formatted string (e.g. America/New_York).
   * @see <a href="https://en.wikipedia.org/wiki/List_of_tz_database_time_zones">List of tz database time zones</a>
   */
  @Json(name = "timezone")
  val timezone: String? = null,
  /**
   *  A list of key-value pairs storing additional data.
   */
  @Json(name = "metadata")
  val metadata: Map<String, String>? = null,
  /**
   * Notetaker meeting bot settings for this calendar.
   */
  @Json(name = "notetaker")
  val notetaker: CalendarNotetaker? = null,
) {
  /**
   * A builder for creating a [CreateCalendarRequest].
   *
   * @param name Name of the Calendar.
   */
  data class Builder(
    private val name: String,
  ) {
    private var description: String? = null
    private var location: String? = null
    private var timezone: String? = null
    private var metadata: Map<String, String>? = null
    private var notetaker: CalendarNotetaker? = null

    /**
     * Set the description of the calendar.
     * @param description Description of the calendar.
     * @return The builder.
     */
    fun description(description: String) = apply { this.description = description }

    /**
     * Set the geographic location of the calendar as free-form text.
     * @param location Geographic location of the calendar as free-form text.
     * @return The builder.
     */
    fun location(location: String) = apply { this.location = location }

    /**
     * Set the IANA time zone database formatted string
     * @param timezone IANA time zone database formatted string (e.g. America/New_York).
     * @return The builder.
     * @see <a href="https://en.wikipedia.org/wiki/List_of_tz_database_time_zones">List of tz database time zones</a>
     */
    fun timezone(timezone: String) = apply { this.timezone = timezone }

    /**
     * Set a list of key-value pairs storing additional data.
     * @param metadata A list of key-value pairs storing additional data.
     * @return The builder.
     */
    fun metadata(metadata: Map<String, String>) = apply { this.metadata = metadata }

    /**
     * Set Notetaker meeting bot settings for this calendar.
     * @param notetaker Notetaker meeting bot settings.
     * @return The builder.
     */
    fun notetaker(notetaker: CalendarNotetaker) = apply { this.notetaker = notetaker }

    /**
     * Build the [CreateCalendarRequest].
     * @return A [CreateCalendarRequest] with the provided values.
     */
    fun build() = CreateCalendarRequest(name, description, location, timezone, metadata, notetaker)
  }
}
