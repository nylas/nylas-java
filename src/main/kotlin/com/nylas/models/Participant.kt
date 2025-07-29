package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing an Event participant.
 */
data class Participant(
  /**
   * Participant's email address.
   */
  @Json(name = "email")
  val email: String? = null,
  /**
   * Participant's status.
   */
  @Json(name = "status")
  val status: ParticipantStatus = ParticipantStatus.NOREPLY,
  /**
   * Participant's name.
   */
  @Json(name = "name")
  val name: String? = null,
  /**
   * Participant's phone number.
   */
  @Json(name = "phone_number")
  val phoneNumber: String? = null,
  /**
   * Comment by the participant.
   */
  @Json(name = "comment")
  val comment: String? = null,
)
