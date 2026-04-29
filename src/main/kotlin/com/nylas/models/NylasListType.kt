package com.nylas.models

import com.squareup.moshi.Json

/**
 * The type of values stored in a Nylas list.
 */
enum class NylasListType {
  /**
   * The list contains domain names (e.g. example.com).
   */
  @Json(name = "domain")
  DOMAIN,

  /**
   * The list contains top-level domains (e.g. xyz, com).
   */
  @Json(name = "tld")
  TLD,

  /**
   * The list contains email addresses (e.g. user@example.com).
   */
  @Json(name = "address")
  ADDRESS,
}
