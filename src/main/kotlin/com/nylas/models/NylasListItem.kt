package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a single item in a Nylas list.
 */
data class NylasListItem(
  /**
   * Globally unique object identifier.
   */
  @Json(name = "id")
  val id: String = "",
  /**
   * The ID of the list this item belongs to.
   */
  @Json(name = "list_id")
  val listId: String = "",
  /**
   * The value stored in this item (domain, TLD, or address depending on list type).
   */
  @Json(name = "value")
  val value: String = "",
  /**
   * Unix timestamp when the item was created.
   */
  @Json(name = "created_at")
  val createdAt: Long = 0,
)
