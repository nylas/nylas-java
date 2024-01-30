package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of the query parameters for listing credentials.
 */
data class ListCredentialsQueryParams(
  /**
   * Limit the number of results
   */
  @Json(name = "limit")
  val limit: Int? = null,
  /**
   * Offset the results by this number
   */
  @Json(name = "offset")
  val offset: Int? = null,
  /**
   * Sort the results by field name
   */
  @Json(name = "sort_by")
  val sortBy: SortBy? = null,
  /**
   * Order the results by ascending or descending
   */
  @Json(name = "order_by")
  val orderBy: OrderBy? = null,
) : IQueryParams {
  /**
   * Builder for [ListCredentialsQueryParams].
   */
  class Builder {
    private var limit: Int? = null
    private var offset: Int? = null
    private var sortBy: SortBy? = null
    private var orderBy: OrderBy? = null

    /**
     * Sets the maximum number of objects to return.
     * This field defaults to 10. The maximum allowed value is 200.
     * @param limit The maximum number of objects to return.
     * @return The builder.
     */
    fun limit(limit: Int?) = apply { this.limit = limit }

    /**
     * Sets the offset grant results by this number.
     * @param offset The offset grant results by this number.
     * @return The builder.
     */
    fun offset(offset: Int?) = apply { this.offset = offset }

    /**
     * Sets the sort entries by field name.
     * @param sortBy The sort entries by field name.
     * @return The builder.
     */
    fun sortBy(sortBy: SortBy?) = apply { this.sortBy = sortBy }

    /**
     * Sets the specify ascending or descending order.
     * @param orderBy The specify ascending or descending order.
     * @return The builder.
     */
    fun orderBy(orderBy: OrderBy?) = apply { this.orderBy = orderBy }

    /**
     * Builds a [ListCredentialsQueryParams] instance.
     * @return The [ListCredentialsQueryParams] instance.
     */
    fun build() = ListCredentialsQueryParams(limit, offset, sortBy, orderBy)
  }
}
