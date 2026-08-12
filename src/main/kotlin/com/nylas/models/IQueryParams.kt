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

/**
 * Rewrite the named `List` query parameters, which the API does not accept as repeated
 * parameters. An empty list drops the parameter entirely; values that are not lists, and keys
 * that are absent, are left untouched.
 *
 * Each query parameter class declares its own affected keys, rather than [com.nylas.NylasClient]
 * holding a central list: parameter names are only meaningful per endpoint, and several of them
 * (`to`, `from`, `cc`, `bcc`) are also request *body* field names, where repeated values are
 * correct and collapsing them would silently drop recipients.
 */
private fun Map<String, Any>.rewriteListParams(
  keys: Array<out String>,
  rewrite: (List<*>) -> String,
): Map<String, Any> {
  val rewritten = toMutableMap()
  for (key in keys) {
    val value = rewritten[key]
    if (value is List<*>) {
      if (value.isEmpty()) {
        rewritten.remove(key)
      } else {
        rewritten[key] = rewrite(value)
      }
    }
  }
  return rewritten
}

/**
 * Join query parameters that the API parses as a single comma-delimited string. Repeating them
 * would make the API keep only one value and silently drop the rest.
 */
internal fun Map<String, Any>.joinCommaDelimitedParams(vararg keys: String): Map<String, Any> =
  rewriteListParams(keys) { list -> list.joinToString(",") }

/**
 * Send only the first entry of query parameters that the API accepts as a single value, but which
 * this SDK still types as `List<String>` for backwards compatibility.
 *
 * The API keeps exactly one value for these regardless; sending one parameter makes which value
 * wins deterministic instead of dependent on the API parser's repeated-parameter behavior.
 */
internal fun Map<String, Any>.collapseSingleValueParams(vararg keys: String): Map<String, Any> =
  rewriteListParams(keys) { list -> list.first().toString() }
