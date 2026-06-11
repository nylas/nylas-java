package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of the query parameters for listing domains.
 */
data class ListDomainsQueryParams(
  @Json(name = "domain")
  val domain: String? = null,
  @Json(name = "region")
  val region: String? = null,
  @Json(name = "limit")
  val limit: Int? = null,
  @Json(name = "page_token")
  val pageToken: String? = null,
) : IQueryParams {
  class Builder {
    private var domain: String? = null
    private var region: String? = null
    private var limit: Int? = null
    private var pageToken: String? = null

    fun domain(domain: String?) = apply { this.domain = domain }
    fun region(region: String?) = apply { this.region = region }
    fun limit(limit: Int?) = apply { this.limit = limit }
    fun pageToken(pageToken: String?) = apply { this.pageToken = pageToken }
    fun build() = ListDomainsQueryParams(domain, region, limit, pageToken)
  }
}
