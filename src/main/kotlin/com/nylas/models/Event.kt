package com.nylas.models

import com.nylas.util.JsonHelper
import com.squareup.moshi.Json
data class Event(
  @Json(name = "id")
  val id: String = "",
  @Json(name = "grant_id")
  val grantId: String = "",
  @Json(name = "when")
  private val whenObj: When = When.Time(0),
  @Json(name = "busy")
  val busy: Boolean = false,
  @Json(name = "calendar_id")
  val calendarId: String = "",
  @Json(name = "hide_participants")
  val hideParticipants: Boolean = false,
  @Json(name = "object")
  private val obj: String = "event",
  @Json(name = "participants")
  val participants: List<Participant> = emptyList(),
  @Json(name = "read_only")
  val readOnly: Boolean = false,
  @Json(name = "created_at")
  val createdAt: Long = 0,
  @Json(name = "updated_at")
  val updatedAt: Long = 0,
  @Json(name = "conferencing")
  val conferencing: Conferencing? = null,
  @Json(name = "description")
  val description: String? = null,
  @Json(name = "ical_uid")
  val icalUid: String? = null,
  @Json(name = "location")
  val location: String? = null,
  @Json(name = "message_id")
  val messageId: String? = null,
  @Json(name = "metadata")
  val metadata: Map<String, String>? = null,
  @Json(name = "owner")
  val owner: String? = null,
  @Json(name = "organizer")
  val organizer: EmailName? = null,
  @Json(name = "recurrence")
  val recurrence: Recurrence? = null,
  @Json(name = "reminders")
  val reminders: Reminders? = null,
  @Json(name = "status")
  val status: String? = null,
  @Json(name = "title")
  val title: String? = null,
  @Json(name = "visibility")
  val visibility: EvenVisibility? = null,
  @Json(name = "creator")
  val creator: EmailName? = null,
  @Json(name = "html_link")
  val htmlLink: String? = null,
) {
  fun getObject(): String = obj

  fun getWhen(): When = whenObj

  fun toJSON() = JsonHelper.objectToJson(Event::class.java, this)
}
