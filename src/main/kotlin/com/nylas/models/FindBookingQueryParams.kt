package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of the query parameters for finding a booking.
 */
data class FindBookingQueryParams(
  /**
   * The ID of the Configuration object whose settings are used for calculating availability.
   * If you're using session authentication (requires_session_auth is set to true), configuration_id is not required
   */
  @Json(name = "configuration_id")
  val configurationId: String? = null,
  /**
   * The slug of the Configuration object whose settings are used for calculating availability.
   * If you're using session authentication (requires_session_auth is set to true), or using configuration_id, slug is not required
   */
  @Json(name = "slug")
  val slug: String? = null,
  /**
   * The client ID that was used to create the Configuration object.
   *  client_id is required only if using slug.
   */
  @Json(name = "client_id")
  val clientId: String? = null,
) : IQueryParams {
  /**
   * Builder for [FindBookingQueryParams].
   */
  class Builder {
    private var configurationId: String? = null
    private var slug: String? = null
    private var clientId: String? = null

    /**
     * Sets the ID of the Configuration object whose settings are used for calculating availability.
     * @param configurationId The ID of the Configuration object whose settings are used for calculating availability.
     * @return The builder.
     */
    fun configurationId(configurationId: String) = apply { this.configurationId = configurationId }

    /**
     * Sets the slug of the Configuration object whose settings are used for calculating availability.
     * @param slug The slug of the Configuration object whose settings are used for calculating availability.
     * @return The builder.
     */
    fun slug(slug: String) = apply { this.slug = slug }

    /**
     * Sets the client ID that was used to create the Configuration object.
     * @param clientId The client ID that was used to create the Configuration object.
     * @return The builder.
     */
    fun clientId(clientId: String) = apply { this.clientId = clientId }

    /**
     * Builds a [FindBookingQueryParams] instance.
     * @return The [FindBookingQueryParams] instance.
     */
    fun build() = FindBookingQueryParams(configurationId, slug, clientId)
  }
}
