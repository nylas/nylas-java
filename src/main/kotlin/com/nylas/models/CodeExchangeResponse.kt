package com.nylas.models

import com.squareup.moshi.Json

data class CodeExchangeResponse(
  @Json(name = "access_token")
  val accessToken: String,
  @Json(name = "grant_id")
  val grantId: String,
  @Json(name = "expires_in")
  val expiresIn: Int,
  @Json(name = "scope")
  val scope: String,
  @Json(name = "refresh_token")
  val refreshToken: String?,
  @Json(name = "id_token")
  val idToken: String?,
  @Json(name = "token_type")
  val tokenType: String?,
)
