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

  class Builder(
    private val id: String,
  ) {
    private var grantId: String? = null
    private var groupType: GroupType? = null
    private var name: String? = null
    private var path: String? = null

    fun grantId(grantId: String) = apply { this.grantId = grantId }
    fun groupType(groupType: GroupType) = apply { this.groupType = groupType }
    fun name(name: String) = apply { this.name = name }
    fun path(path: String) = apply { this.path = path }

    fun build() = ContactGroup(
      id = id,
      grantId = grantId,
      groupType = groupType,
      name = name,
      path = path,
    )
  }
}