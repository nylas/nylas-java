package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing the query parameters for listing messages.
 */
data class ListFoldersQueryParams(
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
   * (Microsoft and EWS only.) Use the ID of a folder to find all child folders it contains.
   */
  @Json(name = " parent_id")
  val parentId: String? = null,
) : IQueryParams {
  class Builder {
    private var limit: Int? = null
    private var pageToken: String? = null
    private var parentId: String? = null

    /**
     * Sets the maximum number of objects to return.
     * This field defaults to 10. The maximum allowed value is 200.
     * @param limit The maximum number of objects to return.
     * @return The builder.
     */
    fun limit(limit: Int?) = apply { this.limit = limit }

    /**
     * Sets the identifier that specifies which page of data to return.
     * This value should be taken from the next_cursor response field.
     * @param pageToken The identifier that specifies which page of data to return.
     * @return The builder.
     */
    fun pageToken(pageToken: String?) = apply { this.pageToken = pageToken }

    /**
     * Sets the parent id of the folders to return.
     * @param parentId The parent id of the folder to return.
     * @return The builder.
     */
    fun parentId(parentId: String?) = apply { this.parentId = parentId }

    /**
     * Builds the [ListFoldersQueryParams] object.
     * @return The [ListFoldersQueryParams] object.
     */
    fun build() = ListFoldersQueryParams(
      limit = limit,
      pageToken = pageToken,
      parentId = parentId,
    )
  }
}
