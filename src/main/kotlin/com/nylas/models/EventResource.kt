package com.nylas.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EventResource(
  @Json(name = "email")
  val email: String,
  @Json(name = "name")
  val name: String? = null,
  @Json(name = "capacity")
  val capacity: Int? = null,
  @Json(name = "building")
  val building: String? = null,
  @Json(name = "floor_name")
  val floorName: String? = null,
  @Json(name = "floor_section")
  val floorSection: String? = null,
  @Json(name = "floor_number")
  val floorNumber: Int? = null,
)
