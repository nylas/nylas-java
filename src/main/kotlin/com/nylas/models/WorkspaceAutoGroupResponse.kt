package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas workspace auto-group response.
 */
data class WorkspaceAutoGroupResponse(
  @Json(name = "job_id")
  val jobId: String,
  @Json(name = "message")
  val message: String,
)
