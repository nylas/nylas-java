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
   * Filter for Notetaker bots that have join times that start at or after a specific time, in Unix timestamp format.
   */
  @Json(name = "join_time_start")
  val joinTimeStart: Long? = null,

  /**
   * Filter for Notetaker bots that have join times that end at or are before a specific time, in Unix timestamp format.
   */
  @Json(name = "join_time_end")
  val joinTimeEnd: Long? = null,

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

  /**
   * Sort the data Nylas returns in ascending (asc) or descending (desc) order.
   */
  @Json(name = "order_direction")
  val orderDirection: Notetaker.OrderDirection? = null,

  /**
   * Sort the data Nylas returns by the selected field.
   * Defaults to created_at.
   */
  @Json(name = "order_field")
  val orderField: Notetaker.OrderField? = null,
) : IQueryParams {
  /**
   * Builder for [ListNotetakersQueryParams].
   */
  class Builder {
    private var state: Notetaker.NotetakerState? = null
    private var joinTimeStart: Long? = null
    private var joinTimeEnd: Long? = null
    private var limit: Int? = null
    private var pageToken: String? = null
    private var prevPageToken: String? = null
    private var select: String? = null
    private var orderDirection: Notetaker.OrderDirection? = null
    private var orderField: Notetaker.OrderField? = null

    /**
     * Sets the state filter for Notetakers.
     * @param state The meeting state to filter by.
     * @return The builder.
     */
    fun state(state: Notetaker.NotetakerState?) = apply { this.state = state }

    /**
     * Sets the join time start filter.
     * @param joinTimeStart Unix timestamp to filter Notetakers joining after this time.
     * @return The builder.
     */
    fun joinTimeStart(joinTimeStart: Long?) = apply { this.joinTimeStart = joinTimeStart }

    /**
     * Sets the join time end filter.
     * @param joinTimeEnd Unix timestamp to filter Notetakers joining until this time.
     * @return The builder.
     */
    fun joinTimeEnd(joinTimeEnd: Long?) = apply { this.joinTimeEnd = joinTimeEnd }

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
     * Sets the order direction for sorting results.
     * @param orderDirection The direction to sort results in (asc or desc)
     * @return The builder.
     */
    fun orderDirection(orderDirection: Notetaker.OrderDirection?) = apply { this.orderDirection = orderDirection }

    /**
     * Sets the field to sort results by.
     * @param orderField The field to sort results by
     * @return The builder.
     */
    fun orderField(orderField: Notetaker.OrderField?) = apply { this.orderField = orderField }

    /**
     * Builds the [ListNotetakersQueryParams] object.
     * @return The [ListNotetakersQueryParams] object.
     */
    fun build() = ListNotetakersQueryParams(
      state = state,
      joinTimeStart = joinTimeStart,
      joinTimeEnd = joinTimeEnd,
      limit = limit,
      pageToken = pageToken,
      prevPageToken = prevPageToken,
      select = select,
      orderDirection = orderDirection,
      orderField = orderField,
    )
  }
}
