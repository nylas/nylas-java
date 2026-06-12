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
) : IQueryParams {
  class Builder {
    private var limit: Int? = null
    private var pageToken: String? = null

    fun limit(limit: Int?) = apply { this.limit = limit }
    fun pageToken(pageToken: String?) = apply { this.pageToken = pageToken }
    fun build() = ListDomainsQueryParams(limit, pageToken)
  }
}
