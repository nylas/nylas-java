package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of the query parameters for listing items in a Nylas list.
 */
data class ListNylasListItemsQueryParams(
  /**
   * The maximum number of objects to return.
   */
  @Json(name = "limit")
  val limit: Int? = null,
  /**
   * Cursor for pagination. Pass the value of [ListResponse.nextCursor] to get the next page.
   */
  @Json(name = "page_token")
  val pageToken: String? = null,
) : IQueryParams {
  /**
   * Builder for [ListNylasListItemsQueryParams].
   */
  class Builder {
    private var limit: Int? = null
    private var pageToken: String? = null

    fun limit(limit: Int) = apply { this.limit = limit }
    fun pageToken(pageToken: String) = apply { this.pageToken = pageToken }

    fun build() = ListNylasListItemsQueryParams(limit, pageToken)
  }
}
