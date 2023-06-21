package com.nylas.models

import com.squareup.moshi.Json

data class ListCalendersQueryParams(
  @Json(name = "limit")
  val limit: Int? = null,
  @Json(name = "page_token")
  val pageToken: String? = null,
  @Json(name = "metadata_pair")
  val metadataPair: Map<String, String>? = null,
) : IQueryParams {
  class Builder {
    private var limit: Int? = null
    private var pageToken: String? = null
    private var metadataPair: Map<String, String>? = null

    fun limit(limit: Int?) = apply { this.limit = limit }
    fun pageToken(pageToken: String?) = apply { this.pageToken = pageToken }
    fun metadataPair(metadataPair: Map<String, String>?) = apply { this.metadataPair = metadataPair }

    fun build() = ListCalendersQueryParams(
      limit,
      pageToken,
      metadataPair,
    )
  }
}
