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
   * Comma-separated list of fields to return in the response.
   */
  @Json(name = "select")
  val select: String? = null,
  /**
   * When true, tentative events are counted as busy.
   */
  @Json(name = "tentative_as_busy")
  val tentativeAsBusy: Boolean? = null,
) : IQueryParams {
  /**
   * Builder for [FindEventQueryParams].
   * @property calendarId Calendar ID to find the event in. "primary" is a supported value indicating the user's primary calendar.
   */
  data class Builder(
    private val calendarId: String,
  ) {
    private var select: String? = null
    private var tentativeAsBusy: Boolean? = null

    /**
     * Sets the comma-separated list of fields to return in the response.
     * @param select The fields to return.
     * @return The builder.
     */
    fun select(select: String?) = apply { this.select = select }

    /**
     * Sets whether tentative events are counted as busy.
     * @param tentativeAsBusy Whether tentative events are counted as busy.
     * @return The builder.
     */
    fun tentativeAsBusy(tentativeAsBusy: Boolean?) = apply { this.tentativeAsBusy = tentativeAsBusy }

    /**
     * Builds a new [FindEventQueryParams] instance.
     * @return [FindEventQueryParams]
     */
    fun build() = FindEventQueryParams(
      calendarId,
      select,
      tentativeAsBusy,
    )
  }
}
