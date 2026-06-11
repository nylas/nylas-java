package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas create workspace request.
 */
data class CreateWorkspaceRequest(
  @Json(name = "name")
  val name: String,
  @Json(name = "domain")
  val domain: String? = null,
  @Json(name = "auto_group")
  val autoGroup: Boolean? = null,
  @Json(name = "policy_id")
  val policyId: String? = null,
  @Json(name = "rules_ids")
  val rulesIds: List<String>? = null,
) {
  data class Builder(private val name: String) {
    private var domain: String? = null
    private var autoGroup: Boolean? = null
    private var policyId: String? = null
    private var rulesIds: List<String>? = null

    fun domain(domain: String?) = apply { this.domain = domain }
    fun autoGroup(autoGroup: Boolean?) = apply { this.autoGroup = autoGroup }
    fun policyId(policyId: String?) = apply { this.policyId = policyId }
    fun rulesIds(rulesIds: List<String>?) = apply { this.rulesIds = rulesIds }
    fun build() = CreateWorkspaceRequest(name, domain, autoGroup, policyId, rulesIds)
  }
}
