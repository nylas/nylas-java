package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas workspace auto-group request.
 */
data class WorkspaceAutoGroupRequest(
  /**
   * Only group grants created at or after this Unix timestamp.
   */
  @Json(name = "after_created_at")
  val afterCreatedAt: Long? = null,
  /**
   * When true, includes invalid grants in the grouping pass. Defaults to false.
   */
  @Json(name = "invalid_also")
  val invalidAlso: Boolean? = null,
  /**
   * Only group grants whose email domain matches this domain.
   */
  @Json(name = "specific_domain")
  val specificDomain: String? = null,
) {
  class Builder {
    private var afterCreatedAt: Long? = null
    private var invalidAlso: Boolean? = null
    private var specificDomain: String? = null

    fun afterCreatedAt(afterCreatedAt: Long?) = apply { this.afterCreatedAt = afterCreatedAt }
    fun invalidAlso(invalidAlso: Boolean?) = apply { this.invalidAlso = invalidAlso }
    fun specificDomain(specificDomain: String?) = apply { this.specificDomain = specificDomain }
    fun build() = WorkspaceAutoGroupRequest(afterCreatedAt, invalidAlso, specificDomain)
  }
}
