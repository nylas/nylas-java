package com.nylas.models

import com.squareup.moshi.Json

data class TokenParams(
  /**
   * Token to be revoked.
   */
  @Json(name = "token")
  val token: String,
) : IQueryParams {
  /**
   * Builder for [TokenParams].
   * @property token Token to be revoked.
   */
  data class Builder(
    private val token: String,
  ) {

    /**
     * Build the [TokenParams] object
     * @return The [TokenParams] object
     */
    fun build() = TokenParams(
      token,
    )
  }
}
