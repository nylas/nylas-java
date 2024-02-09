package com.nylas.models

import com.squareup.moshi.Json

data class TokenInfoResponse(
  /**
   * The issuer of the token.
   */
  @Json(name = "iss")
  val iss: String,
  /**
   * The token's audience.
   */
  @Json(name = "aud")
  val aud: String,
  /**
   * The time that the token was issued.
   */
  @Json(name = "iat")
  val iat: Int,
  /**
   * The time that the token expires.
   */
  @Json(name = "exp")
  val exp: Int,
  /**
   * The token's subject.
   */
  @Json(name = "sub")
  val sub: String? = null,
  /**
   * The email address of the Grant belonging to the user's token.
   */
  @Json(name = "email")
  val email: String? = null,
)
