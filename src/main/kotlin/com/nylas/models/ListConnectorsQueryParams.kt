package com.nylas.models

import com.squareup.moshi.Json

data class ListConnectorsQueryParams(
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
) : IQueryParams {
  /**
   * Builder for [ListConnectorsQueryParams].
   */
  class Builder {
    private var limit: Int? = null
    private var pageToken: String? = null

    /**
     * Set the maximum number of objects to return.
     * This field defaults to 50. The maximum allowed value is 200.
     * @param limit The maximum number of objects to return.
     * @return The builder.
     */
    fun limit(limit: Int?) = apply { this.limit = limit }

    /**
     * Set the identifier that specifies which page of data to return.
     * This value should be taken from the [ListResponse.nextCursor] response field.
     * @param pageToken The identifier that specifies which page of data to return.
     * @return The builder.
     */
    fun pageToken(pageToken: String?) = apply { this.pageToken = pageToken }

    /**
     * Build the [ListConnectorsQueryParams] object.
     * @return The [ListConnectorsQueryParams] object.
     */
    fun build() = ListConnectorsQueryParams(limit, pageToken)
  }
}
