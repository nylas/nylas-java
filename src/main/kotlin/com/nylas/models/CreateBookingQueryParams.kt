package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of the query parameters for creating a booking.
 */
data class CreateBookingQueryParams(
  /**
   * The ID of the Configuration object whose settings are used for calculating availability.
   */
  @Json(name = "configuration_id")
  val configurationId: String? = null,
  /**
   * The slug of the Configuration object whose settings are used for calculating availability.
   */
  @Json(name = "slug")
  val slug: String? = null,
  /**
   * The client ID that was used to create the Configuration object.
   */
  @Json(name = "client_id")
  val clientId: String? = null,
) : IQueryParams {
  /**
   * Builder for [CreateBookingQueryParams].
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
     * Builds a [CreateBookingQueryParams] instance.
     * @return The [CreateBookingQueryParams] instance.
     */
    fun build() = CreateBookingQueryParams(configurationId, slug, clientId)
  }
}
