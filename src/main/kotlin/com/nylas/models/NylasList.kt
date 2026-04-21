package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas list object.
 * Lists are typed collections of domains, TLDs, or addresses referenced by rule conditions.
 */
data class NylasList(
  /**
   * Globally unique object identifier.
   */
  @Json(name = "id")
  val id: String = "",
  /**
   * Name of the list.
   */
  @Json(name = "name")
  val name: String = "",
  /**
   * Optional description of the list.
   */
  @Json(name = "description")
  val description: String? = null,
  /**
   * The type of values stored in this list. Immutable after creation.
   */
  @Json(name = "type")
  val type: NylasListType = NylasListType.DOMAIN,
  /**
   * The number of items currently in the list.
   */
  @Json(name = "items_count")
  val itemsCount: Int = 0,
  /**
   * The ID of the Nylas application this list belongs to.
   */
  @Json(name = "application_id")
  val applicationId: String = "",
  /**
   * The ID of the organization this list belongs to.
   */
  @Json(name = "organization_id")
  val organizationId: String = "",
  /**
   * Unix timestamp when the list was created.
   */
  @Json(name = "created_at")
  val createdAt: Long = 0,
  /**
   * Unix timestamp when the list was last updated.
   */
  @Json(name = "updated_at")
  val updatedAt: Long = 0,
)
