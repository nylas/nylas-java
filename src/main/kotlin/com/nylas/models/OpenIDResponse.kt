package com.nylas.models

import com.squareup.moshi.Json

data class OpenIDResponse(
  @Json(name = "iss")
  val iss: String, // Issuer
  @Json(name = "aud")
  val aud: String, // Application Slug
  @Json(name = "iat")
  val iat: Int, // Issued At
  @Json(name = "exp")
  val exp: Int, // Expires At
  @Json(name = "sub")
  val sub: String?, // ID
  @Json(name = "email")
  val email: String?,
  @Json(name = "email_verified")
  val emailVerified: Boolean?,
  @Json(name = "at_hash")
  val atHash: String?,
  // Profile
  @Json(name = "name")
  val name: String?,
  @Json(name = "given_name")
  val givenName: String?,
  @Json(name = "family_name")
  val familyName: String?,
  @Json(name = "nick_name")
  val nickName: String?,
  @Json(name = "picture_url")
  val pictureURL: String?,
  @Json(name = "gender")
  val gender: String?,
  @Json(name = "locale")
  val locale: String?,
)
