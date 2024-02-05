package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation for web pages in a contact.
 */
data class WebPage(
  @Json(name = "url")
  val url: String? = null,
  @Json(name = "type")
  val type: String? = null,
) {
  class Builder {
    private var url: String? = null
    private var type: String? = null

    fun url(url: String) = apply { this.url = url }

    fun type(type: String) = apply { this.type = type }

    fun build() = WebPage(url, type)
  }
}
