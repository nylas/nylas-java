package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas workspace manual assignment request.
 */
data class WorkspaceManualAssignRequest(
  @Json(name = "assign_grants")
  val assignGrants: List<String>? = null,
  @Json(name = "remove_grants")
  val removeGrants: List<String>? = null,
) {
  class Builder {
    private var assignGrants: List<String>? = null
    private var removeGrants: List<String>? = null

    fun assignGrants(assignGrants: List<String>?) = apply { this.assignGrants = assignGrants }
    fun removeGrants(removeGrants: List<String>?) = apply { this.removeGrants = removeGrants }
    fun build() = WorkspaceManualAssignRequest(assignGrants, removeGrants)
  }
}
