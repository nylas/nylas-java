package com.nylas.models

import com.nylas.util.JsonHelper

/**
 * Interface for query parameters.
 */
interface IQueryParams {
  /**
   * Convert the query parameters to a json-formatted map.
   * @return Map of query parameters
   */
  fun convertToMap(): Map<String, Any> {
    val json = JsonHelper.moshi()
      .adapter(this.javaClass)
      .toJson(this)

    if (json.isEmpty()) {
      return emptyMap()
    }

    return JsonHelper.jsonMapAdapter.fromJson(json)!!
  }
}
