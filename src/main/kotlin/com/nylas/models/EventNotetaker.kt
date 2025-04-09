package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of Notetaker meeting bot settings for an event.
 * This is used for responses and contains the id field.
 */
data class EventNotetaker(
  /**
   * The Notetaker ID.
   */
  @Json(name = "id")
  val id: String? = null,

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
}

/**
 * Class representation of Notetaker meeting bot settings for an event creation request.
 * This version omits the id field as it should not be part of the request.
 */
data class EventNotetakerRequest(
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
}
