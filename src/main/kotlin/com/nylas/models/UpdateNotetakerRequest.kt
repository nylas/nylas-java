package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of the Nylas Notetaker update request.
 */
data class UpdateNotetakerRequest(
  /**
   * When Notetaker should join the meeting, in Unix timestamp format.
   */
  @Json(name = "join_time")
  val joinTime: Long? = null,

  /**
   * The display name for the Notetaker bot.
   */
  @Json(name = "name")
  val name: String? = null,

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
    val videoRecording: Boolean? = null,

    /**
     * When true, Notetaker records the meeting's audio.
     */
    @Json(name = "audio_recording")
    val audioRecording: Boolean? = null,

    /**
     * When true, Notetaker transcribes the meeting's audio.
     */
    @Json(name = "transcription")
    val transcription: Boolean? = null,
  ) {
    /**
     * Builder for [MeetingSettings].
     */
    class Builder {
      private var videoRecording: Boolean? = null
      private var audioRecording: Boolean? = null
      private var transcription: Boolean? = null

      /**
       * Set video recording setting.
       * @param videoRecording Whether to record video.
       * @return The builder.
       */
      fun videoRecording(videoRecording: Boolean) = apply { this.videoRecording = videoRecording }

      /**
       * Set audio recording setting.
       * @param audioRecording Whether to record audio.
       * @return The builder.
       */
      fun audioRecording(audioRecording: Boolean) = apply { this.audioRecording = audioRecording }

      /**
       * Set transcription setting.
       * @param transcription Whether to transcribe audio.
       * @return The builder.
       */
      fun transcription(transcription: Boolean) = apply { this.transcription = transcription }

      /**
       * Build the [MeetingSettings].
       * @return The [MeetingSettings].
       */
      fun build() = MeetingSettings(
        videoRecording = videoRecording,
        audioRecording = audioRecording,
        transcription = transcription,
      )
    }
  }

  /**
   * Builder for [UpdateNotetakerRequest].
   */
  class Builder {
    private var joinTime: Long? = null
    private var name: String? = null
    private var meetingSettings: MeetingSettings? = null

    /**
     * Set join time.
     * @param joinTime When Notetaker should join the meeting.
     * @return The builder.
     */
    fun joinTime(joinTime: Long) = apply { this.joinTime = joinTime }

    /**
     * Set display name.
     * @param name The display name for the Notetaker bot.
     * @return The builder.
     */
    fun name(name: String) = apply { this.name = name }

    /**
     * Set meeting settings.
     * @param meetingSettings The meeting settings.
     * @return The builder.
     */
    fun meetingSettings(meetingSettings: MeetingSettings) = apply { this.meetingSettings = meetingSettings }

    /**
     * Build the [UpdateNotetakerRequest].
     * @return The [UpdateNotetakerRequest].
     */
    fun build() = UpdateNotetakerRequest(
      joinTime = joinTime,
      name = name,
      meetingSettings = meetingSettings,
    )
  }
}
