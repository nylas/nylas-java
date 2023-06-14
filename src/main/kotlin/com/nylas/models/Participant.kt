package com.nylas.models

import com.squareup.moshi.Json

data class Participant(
  @Json(name = "email")
  val email: String = "",
  @Json(name = "status")
  val status: ParticipantStatus = ParticipantStatus.NOREPLY,
  @Json(name = "name")
  val name: String? = null,
  @Json(name = "phone_number")
  val phoneNumber: String? = null,
  @Json(name = "comment")
  val comment: String? = null,
)
