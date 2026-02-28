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
  /**
   * Specify fields that you want Nylas to return, as a comma-separated list (for example, select=id,updated_at).
   * This allows you to receive only the portion of object data that you're interested in.
   * You can use select to optimize response size and reduce latency by limiting queries to only the information that you need
   */
  @Json(name = "select")
  var select: String? = null,
) : IQueryParams {
  /**
   * Builder for [ListCalendersQueryParams].
   */
  class Builder {
    private var limit: Int? = null
    private var pageToken: String? = null
    private var metadataPair: Map<String, String>? = null
    private var select: String? = null

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
     * Set the fields to return in the response.
     * @param select List of field names to return (e.g. "id,grant_id,name")
     * @return The builder.
     */
    fun select(select: String?) = apply { this.select = select }

    /**
     * Builds a [ListCalendersQueryParams] instance.
     * @return The [ListCalendersQueryParams] instance.
     */
    fun build() = ListCalendersQueryParams(
      limit,
      pageToken,
      metadataPair,
      select,
    )
  }
}
