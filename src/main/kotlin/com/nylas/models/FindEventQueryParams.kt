package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of the query parameters for finding an event.
 */
data class FindEventQueryParams(
  /**
   * Calendar ID to find the event in. "primary" is a supported value indicating the user's primary calendar.
   */
  @Json(name = "calendar_id")
  val calendarId: String,
  /**
   * Specify fields that you want Nylas to return, as a comma-separated list (for example, select=id,updated_at).
   * This allows you to receive only the portion of object data that you're interested in.
   * You can use select to optimize response size and reduce latency by limiting queries to only the information that you need
   */
  @Json(name = "select")
  var select: String? = null,
) : IQueryParams {
  /**
   * Builder for [FindEventQueryParams].
   * @property calendarId Calendar ID to find the event in. "primary" is a supported value indicating the user's primary calendar.
   */
  data class Builder(
    private val calendarId: String,
  ) {
    private var select: String? = null

    /**
     * Sets the fields to select.
     * @param select The fields to select.
     * @return The builder.
     */
    fun select(select: String?) = apply { this.select = select }

    /**
     * Builds a new [FindEventQueryParams] instance.
     * @return [FindEventQueryParams]
     */
    fun build() = FindEventQueryParams(
      calendarId,
      select,
    )
  }
}
