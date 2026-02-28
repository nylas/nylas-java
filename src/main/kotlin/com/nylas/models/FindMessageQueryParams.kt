package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing the query parameters for finding a message.
 */
data class FindMessageQueryParams(
  /**
   * Allows you to specify to the message with headers included.
   */
  @Json(name = "fields")
  val fields: MessageFields? = null,
  /**
   * Specify fields that you want Nylas to return, as a comma-separated list (for example, select=id,updated_at).
   * This allows you to receive only the portion of object data that you're interested in.
   * You can use select to optimize response size and reduce latency by limiting queries to only the information that you need
   */
  @Json(name = "select")
  var select: String? = null,
) : IQueryParams {
  /**
   * Builder for [FindMessageQueryParams].
   */
  class Builder {
    private var fields: MessageFields? = null
    private var select: String? = null

    /**
     * Set the fields to include in the response.
     * @param fields The fields to include in the response.
     * @return The builder.
     */
    fun fields(fields: MessageFields?) = apply { this.fields = fields }

    /**
     * Set the fields to return in the response.
     * @param select List of field names to return (e.g. "id,grant_id,subject")
     * @return The builder.
     */
    fun select(select: String?) = apply { this.select = select }

    /**
     * Builds the [FindMessageQueryParams] object.
     * @return The [FindMessageQueryParams] object.
     */
    fun build() = FindMessageQueryParams(
      fields = fields,
      select = select,
    )
  }
}
