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
) : IQueryParams {
  /**
   * Builder for [FindEventQueryParams].
   * @property calendarId Calendar ID to find the event in. "primary" is a supported value indicating the user's primary calendar.
   */
  data class Builder(
    private val calendarId: String,
  ) {
    /**
     * Builds a new [FindEventQueryParams] instance.
     * @return [FindEventQueryParams]
     */
    fun build() = FindEventQueryParams(
      calendarId,
    )
  }
}
