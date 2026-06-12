package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of the query parameters for listing workspaces.
 */
data class ListWorkspacesQueryParams(
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
   * Builder for [ListWorkspacesQueryParams].
   */
  class Builder {
    private var limit: Int? = null
    private var pageToken: String? = null

    /**
     * Set the maximum number of objects to return.
     * @param limit The maximum number of objects to return.
     * @return The builder.
     */
    fun limit(limit: Int) = apply { this.limit = limit }

    /**
     * Set the pagination cursor.
     * @param pageToken Cursor for pagination. Pass the value of [ListResponse.nextCursor].
     * @return The builder.
     */
    fun pageToken(pageToken: String) = apply { this.pageToken = pageToken }

    /**
     * Build the [ListWorkspacesQueryParams].
     * @return A [ListWorkspacesQueryParams] with the provided values.
     */
    fun build() = ListWorkspacesQueryParams(limit, pageToken)
  }
}
