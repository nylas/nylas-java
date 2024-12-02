package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of the query parameters for confirming a booking.
 */
data class ConfirmBookingQueryParams(
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
   * Builder for [ConfirmBookingQueryParams].
   */
  class Builder {
    private var configurationId: String? = null
    private var slug: String? = null
    private var clientId: String? = null

    /**
     * Set the configuration ID of the booking.
     * @param configurationId The configuration ID of the booking.
     * @return The builder.
     */
    fun configurationId(configurationId: String) = apply { this.configurationId = configurationId }

    /**
     * Set the slug of the booking.
     * @param slug The slug of the booking.
     * @return The builder.
     */
    fun slug(slug: String) = apply { this.slug = slug }

    /**
     * Set the client ID of the booking.
     * @param clientId The client ID of the booking.
     * @return The builder.
     */
    fun clientId(clientId: String) = apply { this.clientId = clientId }

    /**
     * Builds a [ConfirmBookingQueryParams] instance.
     * @return The [ConfirmBookingQueryParams] instance.
     */
    fun build() = ConfirmBookingQueryParams(configurationId, slug, clientId)
  }
}
