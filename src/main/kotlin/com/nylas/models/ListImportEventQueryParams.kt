package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of the query parameters for listing import events.
 */
data class ListImportEventQueryParams(
  /**
   * Filter for the specified calendar ID.
   * (Not supported for iCloud) You can use primary to query the end user's primary calendar.
   * This is a required parameter.
   */
  @Json(name = "calendar_id")
  val calendarId: String,
  /**
   * The maximum number of objects to return.
   * This field defaults to 50. The maximum allowed value is 500.
   */
  @Json(name = "max_results")
  val maxResults: Int? = null,
  /**
   * An identifier that specifies which page of data to return.
   * This value should be taken from the [ListResponse.nextCursor] response field.
   */
  @Json(name = "page_token")
  val pageToken: String? = null,
  /**
   * Filter for events that start at or after the specified time, in Unix timestamp format.
   * Defaults to the time that you make the request.
   */
  @Json(name = "start")
  val start: Int? = null,
  /**
   * Filter for events that end at or before the specified time, in Unix timestamp format.
   * Defaults to one month from the time you make the request.
   */
  @Json(name = "end")
  val end: Int? = null,
  /**
   * Specify fields that you want Nylas to return, as a comma-separated list.
   * This allows you to receive only the portion of object data that you're interested in.
   */
  @Json(name = "select")
  val select: String? = null,
) : IQueryParams {
  /**
   * Builder for [ListImportEventQueryParams].
   */
  data class Builder(
    /**
     * Filter for the specified calendar ID.
     * (Not supported for iCloud) You can use primary to query the end user's primary calendar.
     * This is a required parameter.
     */
    private val calendarId: String,
  ) {
    private var maxResults: Int? = null
    private var pageToken: String? = null
    private var start: Int? = null
    private var end: Int? = null
    private var select: String? = null

    /**
     * Sets the maximum number of objects to return.
     * This field defaults to 50. The maximum allowed value is 500.
     * @param maxResults The maximum number of objects to return.
     * @return The builder.
     */
    fun maxResults(maxResults: Int?) = apply { this.maxResults = maxResults }

    /**
     * Sets the identifier that specifies which page of data to return.
     * This value should be taken from the [ListResponse.nextCursor] response field.
     * @param pageToken The identifier that specifies which page of data to return.
     * @return The builder.
     */
    fun pageToken(pageToken: String?) = apply { this.pageToken = pageToken }

    /**
     * Sets the start time to filter events by.
     * Filter for events that start at or after the specified time, in Unix timestamp format.
     * @param start The start time to filter events by.
     * @return The builder.
     */
    fun start(start: Int?) = apply { this.start = start }

    /**
     * Sets the end time to filter events by.
     * Filter for events that end at or before the specified time, in Unix timestamp format.
     * @param end The end time to filter events by.
     * @return The builder.
     */
    fun end(end: Int?) = apply { this.end = end }

    /**
     * Sets the fields to return in the response.
     * Specify fields that you want Nylas to return, as a comma-separated list.
     * @param select The fields to return in the response.
     * @return The builder.
     */
    fun select(select: String?) = apply { this.select = select }

    /**
     * Builds a [ListImportEventQueryParams] instance.
     * @return The [ListImportEventQueryParams] instance.
     */
    fun build() = ListImportEventQueryParams(
      calendarId,
      maxResults,
      pageToken,
      start,
      end,
      select,
    )
  }
} 