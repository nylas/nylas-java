package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of the nested list response returned by the Rules API.
 */
data class RulesListResponse(
  @Json(name = "data")
  val data: RulesListData? = null,
  @Json(name = "request_id")
  val requestId: String = "",
) {
  fun toListResponse(): ListResponse<Rule> {
    return ListResponse(data?.items ?: emptyList(), requestId, data?.nextCursor)
  }
}

/**
 * Class representation of the nested Rules API list payload.
 */
data class RulesListData(
  @Json(name = "items")
  val items: List<Rule>? = null,
  @Json(name = "next_cursor")
  val nextCursor: String? = null,
)
