package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas update workspace request.
 */
data class UpdateWorkspaceRequest(
  @Json(name = "name")
  val name: String? = null,
  @Json(name = "domain")
  val domain: String? = null,
  @Json(name = "auto_group")
  val autoGroup: Boolean? = null,
  @Json(name = "policy_id")
  val policyId: String? = null,
  @Json(name = "rule_ids")
  val ruleIds: List<String>? = null,
) {
  class Builder {
    private var name: String? = null
    private var domain: String? = null
    private var autoGroup: Boolean? = null
    private var policyId: String? = null
    private var ruleIds: List<String>? = null

    fun name(name: String?) = apply { this.name = name }
    fun domain(domain: String?) = apply { this.domain = domain }
    fun autoGroup(autoGroup: Boolean?) = apply { this.autoGroup = autoGroup }
    fun policyId(policyId: String?) = apply { this.policyId = policyId }
    fun ruleIds(ruleIds: List<String>?) = apply { this.ruleIds = ruleIds }
    fun build() = UpdateWorkspaceRequest(name, domain, autoGroup, policyId, ruleIds)
  }
}
