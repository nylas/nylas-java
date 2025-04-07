package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing the query parameters for listing Notetakers.
 */
data class ListNotetakersQueryParams(
  /**
   * Filter for Notetaker bots with the specified meeting state.
   */
  @Json(name = "state")
  val state: Notetaker.NotetakerState? = null,

  /**
   * Filter for Notetaker bots that are scheduled to join meetings after the specified time.
   */
  @Json(name = "join_time_from")
  val joinTimeFrom: Long? = null,

  /**
   * Filter for Notetaker bots that are scheduled to join meetings until the specified time.
   */
  @Json(name = "join_time_until")
  val joinTimeUntil: Long? = null,

  /**
   * The maximum number of objects to return.
   * This field defaults to 50. The maximum allowed value is 200.
   */
  @Json(name = "limit")
  val limit: Int? = null,

  /**
   * An identifier that specifies which page of data to return.
   * This value should be taken from the [ListResponse.nextCursor] response field.
   */
  @Json(name = "page_token")
  val pageToken: String? = null,

  /**
   * An identifier that specifies which page of data to return.
   * This value should be taken from the [ListResponse.prevCursor] response field.
   */
  @Json(name = "prev_page_token")
  val prevPageToken: String? = null,

  /**
   * Specify fields that you want Nylas to return, as a comma-separated list.
   * This allows you to receive only the portion of object data that you're interested in.
   */
  @Json(name = "select")
  var select: String? = null,
) : IQueryParams {
  /**
   * Builder for [ListNotetakersQueryParams].
   */
  class Builder {
    private var state: Notetaker.NotetakerState? = null
    private var joinTimeFrom: Long? = null
    private var joinTimeUntil: Long? = null
    private var limit: Int? = null
    private var pageToken: String? = null
    private var prevPageToken: String? = null
    private var select: String? = null

    /**
     * Sets the state filter for Notetakers.
     * @param state The meeting state to filter by.
     * @return The builder.
     */
    fun state(state: Notetaker.NotetakerState?) = apply { this.state = state }

    /**
     * Sets the join time from filter.
     * @param joinTimeFrom Unix timestamp to filter Notetakers joining after this time.
     * @return The builder.
     */
    fun joinTimeFrom(joinTimeFrom: Long?) = apply { this.joinTimeFrom = joinTimeFrom }

    /**
     * Sets the join time until filter.
     * @param joinTimeUntil Unix timestamp to filter Notetakers joining until this time.
     * @return The builder.
     */
    fun joinTimeUntil(joinTimeUntil: Long?) = apply { this.joinTimeUntil = joinTimeUntil }

    /**
     * Sets the maximum number of objects to return.
     * @param limit The maximum number of objects to return.
     * @return The builder.
     */
    fun limit(limit: Int?) = apply { this.limit = limit }

    /**
     * Sets the page token for pagination.
     * @param pageToken The page token for the next page of results.
     * @return The builder.
     */
    fun pageToken(pageToken: String?) = apply { this.pageToken = pageToken }

    /**
     * Sets the previous page token for pagination.
     * @param prevPageToken The page token for the previous page of results.
     * @return The builder.
     */
    fun prevPageToken(prevPageToken: String?) = apply { this.prevPageToken = prevPageToken }

    /**
     * Sets the fields to return in the response.
     * @param select List of field names to return (e.g. "id,updated_at")
     * @return The builder.
     */
    fun select(select: String?) = apply { this.select = select }

    /**
     * Builds the [ListNotetakersQueryParams] object.
     * @return The [ListNotetakersQueryParams] object.
     */
    fun build() = ListNotetakersQueryParams(
      state = state,
      joinTimeFrom = joinTimeFrom,
      joinTimeUntil = joinTimeUntil,
      limit = limit,
      pageToken = pageToken,
      prevPageToken = prevPageToken,
      select = select,
    )
  }
}
