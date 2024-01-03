package com.nylas.models

/**
 * Class representing a contact group.
 */
data class ContactGroup(
  val id: String,
  val grantId: String? = null,
  val groupType: GroupType? = null,
  val name: String? = null,
  val path: String? = null,
  private val obj: String = "contact_group",
) {
  fun getObject() = obj
}