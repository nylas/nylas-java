package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas update calendar request
 */
data class UpdateCalendarRequest(
  /**
   * Name of the Calendar.
   */
  @Json(name = "name")
  val name: String? = null,
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
   * A list of key-value pairs storing additional data.
   */
  @Json(name = "metadata")
  val metadata: Map<String, String>? = null,
  /**
   * The background color of the calendar in the hexadecimal format (e.g. #0099EE).
   * Empty indicates default color.
   */
  @Json(name = "hex_color")
  val hexColor: String? = null,
  /**
   * The background color of the calendar in the hexadecimal format (e.g. #0099EE).
   * Empty indicates default color. (Google only)
   */
  @Json(name = "hex_foreground_color")
  val hexForegroundColor: String? = null,
) {
  class Builder {
    private var name: String? = null
    private var description: String? = null
    private var location: String? = null
    private var timezone: String? = null
    private var metadata: Map<String, String>? = null
    private var hexColor: String? = null
    private var hexForegroundColor: String? = null

    /**
     * Set the name of the Calendar.
     * @param name Name of the Calendar.
     */
    fun name(name: String) = apply { this.name = name }

    /**
     * Set the description of the calendar.
     * @param description Description of the calendar.
     */
    fun description(description: String) = apply { this.description = description }

    /**
     * Set the geographic location of the calendar as free-form text.
     * @param location Geographic location of the calendar as free-form text.
     */
    fun location(location: String) = apply { this.location = location }

    /**
     * Set the IANA time zone database formatted string (e.g. America/New_York).
     * @param timezone IANA time zone database formatted string (e.g. America/New_York).
     * @see <a href="https://en.wikipedia.org/wiki/List_of_tz_database_time_zones">List of tz database time zones</a>
     */
    fun timezone(timezone: String) = apply { this.timezone = timezone }

    /**
     * Set a list of key-value pairs storing additional data.
     * @param metadata A list of key-value pairs storing additional data.
     */
    fun metadata(metadata: Map<String, String>) = apply { this.metadata = metadata }

    /**
     * Set the background color of the calendar in the hexadecimal format (e.g. #0099EE).
     * Empty indicates default color.
     * @param hexColor The background color of the calendar in the hexadecimal format (e.g. #0099EE).
     */
    fun hexColor(hexColor: String) = apply { this.hexColor = hexColor }

    /**
     * Set the background color of the calendar in the hexadecimal format (e.g. #0099EE).
     * Empty indicates default color. (Google only)
     * @param hexForegroundColor The background color of the calendar in the hexadecimal format (e.g. #0099EE).
     */
    fun hexForegroundColor(hexForegroundColor: String) = apply { this.hexForegroundColor = hexForegroundColor }

    /**
     * Build the UpdateCalendarRequest object.
     */
    fun build() = UpdateCalendarRequest(name, description, location, timezone, metadata, hexColor, hexForegroundColor)
  }
}
