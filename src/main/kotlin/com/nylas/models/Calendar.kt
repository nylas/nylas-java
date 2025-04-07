package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas calendar object
 */
data class Calendar(
  /**
   * Globally unique object identifier.
   */
  @Json(name = "id")
  val id: String = "",
  /**
   * Grant ID of the Nylas account.
   */
  @Json(name = "grant_id")
  val grantId: String = "",
  /**
   * Name of the Calendar.
   */
  @Json(name = "name")
  val name: String = "",
  /**
   * The type of object.
   */
  @Json(name = "object")
  private val obj: String = "calendar",
  /**
   * IANA time zone database formatted string (e.g. America/New_York).
   * @see <a href="https://en.wikipedia.org/wiki/List_of_tz_database_time_zones">List of tz database time zones</a>
   */
  @Json(name = "timezone")
  val timezone: String = "",
  /**
   * If the event participants are able to edit the event.
   */
  @Json(name = "read_only")
  val readOnly: Boolean = false,
  /**
   * If the calendar is owned by the user account.
   */
  @Json(name = "is_owned_by_user")
  val isOwnedByUser: Boolean = false,
  /**
   * If the calendar is the primary calendar.
   */
  @Json(name = "is_primary")
  val isPrimary: Boolean = false,
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
  /**
   * A list of key-value pairs storing additional data.
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
   * Get the type of object.
   * @return The type of object.
   */
  fun getObject(): String = obj
}
