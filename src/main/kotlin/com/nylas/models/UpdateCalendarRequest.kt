package com.nylas.models

import com.squareup.moshi.Json

data class UpdateCalendarRequest(
  @Json(name = "name")
  val name: String? = null,
  @Json(name = "description")
  val description: String? = null,
  @Json(name = "location")
  val location: String? = null,
  @Json(name = "timezone")
  val timezone: String? = null,
  @Json(name = "metadata")
  val metadata: Map<String, String>? = null,
  @Json(name = "hex_color")
  val hexColor: String? = null,
  @Json(name = "hex_foreground_color")
  val hexForegroundColor: String? = null,
) {
  class Builder() {
    private var name: String? = null
    private var description: String? = null
    private var location: String? = null
    private var timezone: String? = null
    private var metadata: Map<String, String>? = null
    private var hexColor: String? = null
    private var hexForegroundColor: String? = null

    fun name(name: String) = apply { this.name = name }
    fun description(description: String) = apply { this.description = description }
    fun location(location: String) = apply { this.location = location }
    fun timezone(timezone: String) = apply { this.timezone = timezone }
    fun metadata(metadata: Map<String, String>) = apply { this.metadata = metadata }
    fun hexColor(hexColor: String) = apply { this.hexColor = hexColor }
    fun hexForegroundColor(hexForegroundColor: String) = apply { this.hexForegroundColor = hexForegroundColor }

    fun build() = UpdateCalendarRequest(name, description, location, timezone, metadata, hexColor, hexForegroundColor)
  }
}
