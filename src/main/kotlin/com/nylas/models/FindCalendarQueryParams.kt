package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing the query parameters for finding a calendar.
 */
data class FindCalendarQueryParams(
  /**
   * Specify fields that you want Nylas to return, as a comma-separated list (for example, select=id,updated_at).
   * This allows you to receive only the portion of object data that you're interested in.
   * You can use select to optimize response size and reduce latency by limiting queries to only the information that you need
   */
  @Json(name = "select")
  var select: String? = null,
) : IQueryParams {
  /**
   * Builder for [FindCalendarQueryParams].
   */
  class Builder {
    private var select: String? = null

    /**
     * Sets the fields to select.
     * @param select The fields to select.
     * @return The builder.
     */
    fun select(select: String?) = apply { this.select = select }

    /**
     * Builds the [FindCalendarQueryParams] object.
     * @return The [FindCalendarQueryParams] object.
     */
    fun build() = FindCalendarQueryParams(
      select = select,
    )
  }
}
