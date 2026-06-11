package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas create domain request.
 */
data class CreateDomainRequest(
  @Json(name = "name")
  val name: String,
  @Json(name = "domain_address")
  val domainAddress: String,
)
