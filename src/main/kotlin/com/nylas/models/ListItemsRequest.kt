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
)
