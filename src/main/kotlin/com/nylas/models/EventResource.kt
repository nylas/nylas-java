package com.nylas.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EventResource(
  @Json(name = "email")
  val email: String,
  @Json(name = "name")
  val name: String? = null,
)
