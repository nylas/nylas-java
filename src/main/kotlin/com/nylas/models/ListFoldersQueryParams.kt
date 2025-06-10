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
  @Json(name = "parent_id")
  val parentId: String? = null,
  /**
   * Specify fields that you want Nylas to return, as a comma-separated list (for example, select=id,updated_at).
   * This allows you to receive only the portion of object data that you're interested in.
   * You can use select to optimize response size and reduce latency by limiting queries to only the information that you need
   */
  @Json(name = "select")
  var select: String? = null,
  /**
   * (Microsoft only) If true, retrieves folders from a single-level hierarchy only. 
   * If false, retrieves folders across a multi-level hierarchy.
   * Defaults to false.
   */
  @Json(name = "single_level")
  val singleLevel: Boolean? = null,
) : IQueryParams {
  class Builder {
    private var limit: Int? = null
    private var pageToken: String? = null
    private var parentId: String? = null
    private var select: String? = null
    private var singleLevel: Boolean? = null

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
     * Sets the fields to return in the response.
     * @param select List of field names to return (e.g. "id,updated_at")
     * @return The builder.
     */
    fun select(select: String?) = apply { this.select = select }

    /**
     * Sets whether to retrieve folders from a single-level hierarchy only. (Microsoft only)
     * @param singleLevel If true, retrieves folders from a single-level hierarchy only. 
     *                   If false, retrieves folders across a multi-level hierarchy.
     * @return The builder.
     */
    fun singleLevel(singleLevel: Boolean?) = apply { this.singleLevel = singleLevel }

    /**
     * Builds the [ListFoldersQueryParams] object.
     * @return The [ListFoldersQueryParams] object.
     */
    fun build() = ListFoldersQueryParams(
      limit = limit,
      pageToken = pageToken,
      parentId = parentId,
      select = select,
      singleLevel = singleLevel,
    )
  }
}
