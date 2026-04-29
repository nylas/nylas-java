package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a request to add or remove items in a Nylas list.
 */
data class ListItemsRequest(
  /**
   * The values to add or remove. Max 1000 items per request.
   */
  @Json(name = "items")
  val items: List<String>,
) {
  /**
   * Builder for [ListItemsRequest].
   */
  class Builder {
    private var items: List<String> = emptyList()

    /**
     * Set the items to add or remove.
     * @param items The values to add or remove. Max 1000 items per request.
     * @return The builder.
     */
    fun items(items: List<String>) = apply { this.items = items }

    /**
     * Build the [ListItemsRequest].
     * @return A [ListItemsRequest] with the provided values.
     */
    fun build() = ListItemsRequest(items)
  }
}
