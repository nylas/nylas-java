package com.nylas.models

import com.nylas.util.JsonHelper

interface IQueryParams {
  fun convertToMap(): Map<String, String> {
    val json = JsonHelper.moshi()
      .adapter(this.javaClass)
      .toJson(this)

    if (json.isEmpty()) {
      return emptyMap()
    }

    return JsonHelper.jsonMapAdapter.fromJson(json)!!
  }
}
