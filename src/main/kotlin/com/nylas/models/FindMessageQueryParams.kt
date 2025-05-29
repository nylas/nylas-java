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
) : IQueryParams {
  /**
   * Builder for [FindMessageQueryParams].
   */
  class Builder {
    private var fields: MessageFields? = null

    /**
     * Set the fields to include in the response.
     * @param fields The fields to include in the response.
     * @return The builder.
     */
    fun fields(fields: MessageFields?) = apply { this.fields = fields }

    /**
     * Builds the [FindMessageQueryParams] object.
     * @return The [FindMessageQueryParams] object.
     */
    fun build() = FindMessageQueryParams(
      fields = fields,
    )
  }
}
