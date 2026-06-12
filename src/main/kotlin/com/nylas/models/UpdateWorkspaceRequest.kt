package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas update workspace request.
 */
data class UpdateWorkspaceRequest(
  @Json(name = "name")
  val name: String? = null,
  @Json(name = "auto_group")
  val autoGroup: Boolean? = null,
  @Json(name = "policy_id")
  val policyId: NullableField<String>? = null,
  @Json(name = "rule_ids")
  val ruleIds: List<String>? = null,
) {
  class Builder {
    private var name: String? = null
    private var autoGroup: Boolean? = null
    private var policyId: NullableField<String>? = null
    private var ruleIds: List<String>? = null

    fun name(name: String?) = apply { this.name = name }
    fun autoGroup(autoGroup: Boolean?) = apply { this.autoGroup = autoGroup }
    fun policyId(policyId: String) = apply { this.policyId = NullableField.Value(policyId) }
    fun clearPolicyId() = apply { this.policyId = NullableField.Clear }
    fun ruleIds(ruleIds: List<String>?) = apply { this.ruleIds = ruleIds }
    fun build() = UpdateWorkspaceRequest(name, autoGroup, policyId, ruleIds)
  }
}
