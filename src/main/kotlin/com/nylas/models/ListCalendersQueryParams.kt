package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of the query parameters for listing calendars.
 */
data class ListCalendersQueryParams(
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
   * Pass in your metadata key and value pair to search for metadata.
   */
  @Json(name = "metadata_pair")
  val metadataPair: Map<String, String>? = null,
) : IQueryParams {
  /**
   * Builder for [ListCalendersQueryParams].
   */
  class Builder {
    private var limit: Int? = null
    private var pageToken: String? = null
    private var metadataPair: Map<String, String>? = null

    /**
     * Sets the maximum number of objects to return.
     * This field defaults to 50. The maximum allowed value is 200.
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
     * Sets the metadata key and value pair to search for metadata.
     * @param metadataPair The metadata key and value pair to search for metadata.
     * @return The builder.
     */
    fun metadataPair(metadataPair: Map<String, String>?) = apply { this.metadataPair = metadataPair }

    /**
     * Builds a [ListCalendersQueryParams] instance.
     * @return The [ListCalendersQueryParams] instance.
     */
    fun build() = ListCalendersQueryParams(
      limit,
      pageToken,
      metadataPair,
    )
  }
}
