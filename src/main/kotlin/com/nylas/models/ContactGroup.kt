package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing a contact group.
 */
data class ContactGroup(
  @Json(name = "id")
  val id: String,
  @Json(name = "grant_id")
  val grantId: String? = null,
  @Json(name = "group_type")
  val groupType: GroupType? = null,
  @Json(name = "name")
  val name: String? = null,
  @Json(name = "path")
  val path: String? = null,
  @Json(name = "object")
  private val obj: String = "contact_group",
) {
  fun getObject() = obj
}
