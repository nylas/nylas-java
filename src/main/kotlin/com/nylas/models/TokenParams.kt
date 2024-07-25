package com.nylas.models

import com.squareup.moshi.Json

data class TokenParams(
  /**
   * Token to be revoked.
   */
  @Json(name = "token")
  val token: String,
) : IQueryParams
