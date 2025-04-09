package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of Notetaker meeting bot settings for a calendar.
 */
data class CalendarNotetaker(
  /**
   * The display name for the Notetaker bot.
   */
  @Json(name = "name")
  val name: String? = "Nylas Notetaker",

  /**
   * Notetaker Meeting Settings
   */
  @Json(name = "meeting_settings")
  val meetingSettings: MeetingSettings? = null,

  /**
   * Rules for when the Notetaker should join a meeting.
   */
  @Json(name = "rules")
  val rules: Rules? = null,
) {
  /**
   * Data class for Notetaker Meeting Settings
   */
  data class MeetingSettings(
    /**
     * When true, Notetaker records the meeting's video.
     */
    @Json(name = "video_recording")
    val videoRecording: Boolean? = true,

    /**
     * When true, Notetaker records the meeting's audio.
     */
    @Json(name = "audio_recording")
    val audioRecording: Boolean? = true,

    /**
     * When true, Notetaker transcribes the meeting's audio.
     */
    @Json(name = "transcription")
    val transcription: Boolean? = true,
  )

  /**
   * Data class for Notetaker event selection rules
   */
  data class Rules(
    /**
     * Types of events to include for notetaking.
     */
    @Json(name = "event_selection")
    val eventSelection: List<EventSelectionType>? = null,

    /**
     * Filters to apply based on the number of participants.
     */
    @Json(name = "participant_filter")
    val participantFilter: ParticipantFilter? = null,
  )

  /**
   * Event selection types for Notetaker.
   */
  enum class EventSelectionType {
    @Json(name = "internal")
    INTERNAL,

    @Json(name = "external")
    EXTERNAL,

    @Json(name = "own_events")
    OWN_EVENTS,

    @Json(name = "participant_only")
    PARTICIPANT_ONLY,

    @Json(name = "all")
    ALL,
  }

  /**
   * Participant filters for Notetaker.
   */
  data class ParticipantFilter(
    /**
     * Only have meeting bot join meetings with greater than or equal to this number of participants.
     */
    @Json(name = "participants_gte")
    val participantsGte: Int? = null,

    /**
     * Only have meeting bot join meetings with less than or equal to this number of participants.
     */
    @Json(name = "participants_lte")
    val participantsLte: Int? = null,
  )
}
