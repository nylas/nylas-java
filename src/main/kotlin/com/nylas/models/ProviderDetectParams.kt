package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing the object used to set parameters for detecting a provider.
 */
data class ProviderDetectParams(
  /**
   * Email address to detect the provider for.
   */
  @Json(name = "email")
  val email: String,
  /**
   * Search by all providers regardless of created integrations. If unset, defaults to false.
   */
  @Json(name = "all_provider_types")
  val allProviderTypes: Boolean? = null,
) : IQueryParams {
  /**
   * Builder for [ProviderDetectParams].
   * @property email Email address to detect the provider for.
   */
  data class Builder(
    private val email: String,
  ) {
    private var allProviderTypes: Boolean? = null

    /**
     * Search by all providers regardless of created integrations
     * If unset, defaults to false
     * @param allProviderTypes Search by all providers regardless of created integrations
     * @return The builder
     */
    fun allProviderTypes(allProviderTypes: Boolean?) = apply { this.allProviderTypes = allProviderTypes }

    /**
     * Build the [ProviderDetectParams] object
     * @return The [ProviderDetectParams] object
     */
    fun build() = ProviderDetectParams(
      email,
      allProviderTypes,
    )
  }
}
