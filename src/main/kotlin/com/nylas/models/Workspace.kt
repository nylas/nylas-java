package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas workspace.
 */
data class Workspace(
  @Json(name = "workspace_id")
  val workspaceId: String,
  @Json(name = "application_id")
  val applicationId: String,
  @Json(name = "name")
  val name: String,
  @Json(name = "domain")
  val domain: String,
  @Json(name = "auto_group")
  val autoGroup: Boolean,
  @Json(name = "policy_id")
  val policyId: String? = null,
  @Json(name = "rules_ids")
  val rulesIds: List<String>? = null,
  @Json(name = "created_at")
  val createdAt: Long,
  @Json(name = "updated_at")
  val updatedAt: Long,
)
