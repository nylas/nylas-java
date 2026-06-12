package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas workspace manual assignment response.
 */
data class WorkspaceManualAssignResponse(
  @Json(name = "application_id")
  val applicationId: String,
  @Json(name = "workspace_id")
  val workspaceId: String,
  @Json(name = "domain")
  val domain: String,
  @Json(name = "grants_assigned")
  val grantsAssigned: List<String>? = null,
  @Json(name = "grants_removed")
  val grantsRemoved: List<String>? = null,
)
