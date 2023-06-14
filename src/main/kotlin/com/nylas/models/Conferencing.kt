package com.nylas.models

import com.squareup.moshi.*


sealed class Conferencing {
  data class Autocreate(
    @Json(name = "provider")
    val provider: ConferencingProvider,
    @Json(name = "autocreate")
    val autocreate: Map<String, Any> = emptyMap()
  ): Conferencing()

  data class Details(
    @Json(name = "provider")
    val provider: ConferencingProvider,
    @Json(name = "details")
    val details: Config
  ): Conferencing() {
    data class Config(
      @Json(name = "meeting_code")
      val meetingCode: String? = null,
      @Json(name = "password")
      val password: String? = null,
      @Json(name = "url")
      val url: String? = null,
      @Json(name = "pin")
      val pin: String? = null,
      @Json(name = "phone")
      val phone: List<String>? = null,
    )
  }
}