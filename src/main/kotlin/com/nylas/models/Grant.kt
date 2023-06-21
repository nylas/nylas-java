package com.nylas.models

import com.squareup.moshi.Json

data class Grant(
  @Json(name = "id")
  val id: String,
  @Json(name = "provider")
  val provider: String,
  @Json(name = "scope")
  val scope: List<String>,
  @Json(name = "created_at")
  val createdAt: Long,
  @Json(name = "grant_status")
  val grantStatus: String?,
  @Json(name = "email")
  val email: String?,
  @Json(name = "user_agent")
  val userAgent: String?,
  @Json(name = "ip")
  val ip: String?,
  @Json(name = "state")
  val state: String?,
  @Json(name = "updated_at")
  val updatedAt: Long?,
  @Json(name = "provider_user_id")
  val providerUserId: String?,
  @Json(name = "settings")
  val settings: Map<String, Any>?
)
