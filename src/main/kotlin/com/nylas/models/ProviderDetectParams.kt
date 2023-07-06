package com.nylas.models

import com.squareup.moshi.Json

data class ProviderDetectParams(
  @Json(name = "email")
  val email: String,
  @Json(name = "all_provider_types")
  val allProviderTypes: Boolean?,
  @Json(name = "client_id")
  var clientId: String?,
) : IQueryParams {
  data class Builder(
    private val email: String,
  ) {
    private var allProviderTypes: Boolean? = null
    private var clientId: String? = null

    fun allProviderTypes(allProviderTypes: Boolean?) = apply { this.allProviderTypes = allProviderTypes }
    fun clientId(clientId: String?) = apply { this.clientId = clientId }

    fun build() = ProviderDetectParams(
      email,
      allProviderTypes,
      clientId,
    )
  }
}
