package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of the query parameters for listing domains.
 */
data class ListDomainsQueryParams(
  @Json(name = "limit")
  val limit: Int? = null,
  @Json(name = "page_token")
  val pageToken: String? = null,
  @Json(name = "domain")
  val domain: String? = null,
  @Json(name = "region")
  val region: Region? = null,
) : IQueryParams {
  class Builder {
    private var limit: Int? = null
    private var pageToken: String? = null
    private var domain: String? = null
    private var region: Region? = null

    fun limit(limit: Int?) = apply { this.limit = limit }
    fun pageToken(pageToken: String?) = apply { this.pageToken = pageToken }
    fun domain(domain: String?) = apply { this.domain = domain }
    fun region(region: Region?) = apply { this.region = region }
    fun build() = ListDomainsQueryParams(limit, pageToken, domain, region)
  }
}
