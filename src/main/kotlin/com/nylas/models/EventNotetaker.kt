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

    /**
     * When true, Notetaker generates action items from the meeting.
     */
    @Json(name = "action_items")
    val actionItems: Boolean? = null,

    /**
     * Settings for action item generation.
     */
    @Json(name = "action_items_settings")
    val actionItemsSettings: ActionItemsSettings? = null,

    /**
     * When true, Notetaker generates a summary of the meeting.
     */
    @Json(name = "summary")
    val summary: Boolean? = null,

    /**
     * Settings for summary generation.
     */
    @Json(name = "summary_settings")
    val summarySettings: SummarySettings? = null,

    /**
     * Number of seconds of silence after which Notetaker leaves the meeting.
     */
    @Json(name = "leave_after_silence_seconds")
    val leaveAfterSilenceSeconds: Int? = null,

    /**
     * Settings for transcription.
     */
    @Json(name = "transcription_settings")
    val transcriptionSettings: TranscriptionSettings? = null,
  ) {
    data class ActionItemsSettings(
      @Json(name = "custom_instructions")
      val customInstructions: String? = null,
    )

    data class SummarySettings(
      @Json(name = "custom_instructions")
      val customInstructions: String? = null,
    )

    data class TranscriptionSettings(
      @Json(name = "expected_languages")
      val expectedLanguages: List<String>? = null,
      @Json(name = "fallback_language")
      val fallbackLanguage: String? = null,
    )
  }
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

    /**
     * When true, Notetaker generates action items from the meeting.
     */
    @Json(name = "action_items")
    val actionItems: Boolean? = null,

    /**
     * Settings for action item generation.
     */
    @Json(name = "action_items_settings")
    val actionItemsSettings: ActionItemsSettings? = null,

    /**
     * When true, Notetaker generates a summary of the meeting.
     */
    @Json(name = "summary")
    val summary: Boolean? = null,

    /**
     * Settings for summary generation.
     */
    @Json(name = "summary_settings")
    val summarySettings: SummarySettings? = null,

    /**
     * Number of seconds of silence after which Notetaker leaves the meeting.
     */
    @Json(name = "leave_after_silence_seconds")
    val leaveAfterSilenceSeconds: Int? = null,

    /**
     * Settings for transcription.
     */
    @Json(name = "transcription_settings")
    val transcriptionSettings: TranscriptionSettings? = null,
  ) {
    data class ActionItemsSettings(
      @Json(name = "custom_instructions")
      val customInstructions: String? = null,
    )

    data class SummarySettings(
      @Json(name = "custom_instructions")
      val customInstructions: String? = null,
    )

    data class TranscriptionSettings(
      @Json(name = "expected_languages")
      val expectedLanguages: List<String>? = null,
      @Json(name = "fallback_language")
      val fallbackLanguage: String? = null,
    )
  }
}
