package com.nylas.models

import com.squareup.moshi.Json

data class CreateCalendarRequest(
  @Json(name = "name")
  val name: String,
  @Json(name = "description")
  val description: String? = null,
  @Json(name = "location")
  val location: String? = null,
  @Json(name = "timezone")
  val timezone: String? = null,
  @Json(name = "metadata")
  val metadata: Map<String, String>? = null
) {
  // builder
  data class Builder(
    private val name: String,
  ) {
    private var description: String? = null
    private var location: String? = null
    private var timezone: String? = null
    private var metadata: Map<String, String>? = null

    fun description(description: String) = apply { this.description = description }
    fun location(location: String) = apply { this.location = location }
    fun timezone(timezone: String) = apply { this.timezone = timezone }
    fun metadata(metadata: Map<String, String>) = apply { this.metadata = metadata }

    fun build() = CreateCalendarRequest(name, description, location, timezone, metadata)
  }
}