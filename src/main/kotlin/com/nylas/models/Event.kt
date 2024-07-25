package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing a Nylas Event object.
 */
data class Event(
  /**
   * Globally unique object identifier.
   */
  @Json(name = "id")
  val id: String = "",
  /**
   * Grant ID of the Nylas account.
   */
  @Json(name = "grant_id")
  val grantId: String = "",
  /**
   * Representation of time and duration for events. When object can be in one of four formats (sub-objects):
   * - [When.Date]
   * - [When.Datespan]
   * - [When.Time]
   * - [When.Timespan]
   */
  @Json(name = "when")
  private val whenObj: When = When.Time(0),
  /**
   * This value determines whether to show this event's time block as available on shared or public calendars.
   */
  @Json(name = "busy")
  val busy: Boolean = false,
  /**
   * Calendar ID of the event.
   */
  @Json(name = "calendar_id")
  val calendarId: String = "",
  /**
   * Whether participants of the event should be hidden.
   */
  @Json(name = "hide_participants")
  val hideParticipants: Boolean = false,
  /**
   * The type of object.
   */
  @Json(name = "object")
  private val obj: String = "event",
  /**
   * List of participants invited to the event. Participants may also be rooms or resources.
   */
  @Json(name = "participants")
  val participants: List<Participant> = emptyList(),
  /**
   * If the event participants are able to edit the event.
   */
  @Json(name = "read_only")
  val readOnly: Boolean = false,
  /**
   * Visibility of the event, if the event is private or public.
   */
  @Json(name = "visibility")
  val visibility: EventVisibility = EventVisibility.DEFAULT,
  /**
   * Representation of conferencing details for events. Conferencing object can be in one of two formats (sub-objects):
   * - [Conferencing.Autocreate]
   * - [Conferencing.Details]
   */
  @Json(name = "conferencing")
  val conferencing: Conferencing? = null,
  /**
   * Description of the event.
   */
  @Json(name = "description")
  val description: String? = null,
  /**
   * Unique id for iCalendar standard, for identifying events across calendaring systems.
   * Recurring events may share the same value. Can be null for events synced before the year 2020.
   */
  @Json(name = "ical_uid")
  val icalUid: String? = null,
  /**
   * Location of the event, such as a physical address or meeting room name.
   */
  @Json(name = "location")
  val location: String? = null,
  /**
   * List of key-value pairs storing additional data.
   */
  @Json(name = "metadata")
  val metadata: Map<String, String>? = null,
  /**
   * Organizer of the event.
   */
  @Json(name = "organizer")
  val organizer: EmailName? = null,
  /**
   * Organizer of the event.
   */
  @Json(name = "master_event_id")
  val masterEventId: String? = null,
  /**
   * A list of RRULE and EXDATE strings.
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc5545#section-3.8.5">RFC-5545</a>
   */
  @Json(name = "recurrence")
  val recurrence: List<String>? = null,
  /**
   * List of reminders for the event.
   */
  @Json(name = "reminders")
  val reminders: Reminders? = null,
  /**
   * Status of the event.
   */
  @Json(name = "status")
  val status: EventStatus? = null,
  /**
   * Title of the event.
   */
  @Json(name = "title")
  val title: String? = null,
  /**
   * User who created the event.
   * Not supported for all providers.
   */
  @Json(name = "creator")
  val creator: EmailName? = null,
  /**
   * A link to this event in the provider's UI
   */
  @Json(name = "html_link")
  val htmlLink: String? = null,
  /**
   * The maximum number of participants that may attend the event
   */
  @Json(name = "capacity")
  val capacity: Int? = null,
  /**
   * Unix timestamp when the event was created.
   */
  @Json(name = "created_at")
  val createdAt: Long? = null,
  /**
   * Unix timestamp when the event was last updated.
   */
  @Json(name = "updated_at")
  val updatedAt: Long? = null,
) {
  /**
   * Get the type of object.
   * @return The type of object.
   */
  fun getObject(): String = obj

  /**
   * Get the representation of time and duration for events.
   * @return The representation of time and duration for events.
   */
  fun getWhen(): When = whenObj
}
