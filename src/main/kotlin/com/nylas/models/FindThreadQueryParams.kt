package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing the query parameters for finding a thread.
 */
data class FindThreadQueryParams(
  /**
   * Specify fields that you want Nylas to return, as a comma-separated list (for example, select=id,updated_at).
   * This allows you to receive only the portion of object data that you're interested in.
   * You can use select to optimize response size and reduce latency by limiting queries to only the information that you need
   */
  @Json(name = "select")
  var select: String? = null,
) : IQueryParams {
  /**
   * Builder for [FindThreadQueryParams].
   */
  class Builder {
    private var select: String? = null

    /**
     * Sets the fields to select.
     * @param select The fields to select.
     * @return The builder.
     */
    fun select(select: String?) = apply { this.select = select }

    /**
     * Builds the [FindThreadQueryParams] object.
     * @return The [FindThreadQueryParams] object.
     */
    fun build() = FindThreadQueryParams(
      select = select,
    )
  }
}
