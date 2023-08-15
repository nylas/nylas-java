package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing the response for getting a list of webhook ip addresses.
 */
data class WebhookIpAddressesResponse(
  /**
   * The IP addresses that Nylas send you webhook from.
   */
  @Json(name = "ip_addresses")
  val ipAddress: List<String>,
  /**
   * UNIX timestamp when Nylas updated the list of IP addresses.
   */
  @Json(name = "updated_at")
  val updatedAt: Long,
)
