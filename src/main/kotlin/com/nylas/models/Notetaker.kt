package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of the Nylas Notetaker response.
 */
data class Notetaker(
  /**
   * The Notetaker ID.
   */
  @Json(name = "id")
  val id: String,

  /**
   * The display name for the Notetaker bot.
   */
  @Json(name = "name")
  val name: String? = "Nylas Notetaker",

  /**
   * When Notetaker joined the meeting, in Unix timestamp format.
   */
  @Json(name = "join_time")
  val joinTime: Long? = null,

  /**
   * The meeting link.
   */
  @Json(name = "meeting_link")
  val meetingLink: String? = null,

  /**
   * The meeting provider.
   */
  @Json(name = "meeting_provider")
  val meetingProvider: MeetingProvider? = null,

  /**
   * The current state of the Notetaker bot.
   */
  @Json(name = "state")
  val state: NotetakerState? = null,

  /**
   * Notetaker Meeting Settings
   */
  @Json(name = "meeting_settings")
  val meetingSettings: MeetingSettings? = null,

  /**
   * The type of object.
   */
  @Json(name = "object")
  val obj: String = "notetaker",
) {
  /**
   * Enum for meeting providers
   */
  enum class MeetingProvider {
    @Json(name = "Google Meet")
    GOOGLE_MEET,

    @Json(name = "Zoom Meeting")
    ZOOM_MEETING,

    @Json(name = "Microsoft Teams")
    MICROSOFT_TEAMS,
  }

  /**
   * Enum for Notetaker states
   */
  enum class NotetakerState {
    @Json(name = "scheduled")
    SCHEDULED,

    @Json(name = "connecting")
    CONNECTING,

    @Json(name = "waiting_for_entry")
    WAITING_FOR_ENTRY,

    @Json(name = "failed_entry")
    FAILED_ENTRY,

    @Json(name = "attending")
    ATTENDING,

    @Json(name = "media_processing")
    MEDIA_PROCESSING,

    @Json(name = "media_available")
    MEDIA_AVAILABLE,

    @Json(name = "media_error")
    MEDIA_ERROR,

    @Json(name = "media_deleted")
    MEDIA_DELETED,
  }

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
   * Get the type of object.
   * @return The type of object.
   */
  fun getObject() = obj
}

/**
 * Response for leaving a Notetaker
 */
data class LeaveNotetakerResponse(
  /**
   * The Notetaker ID
   */
  @Json(name = "id")
  val id: String,

  /**
   * A message describing the API response
   */
  @Json(name = "message")
  val message: String,
)

/**
 * Response for downloading Notetaker media
 */
data class NotetakerMediaResponse(
  /**
   * The meeting recording
   */
  @Json(name = "recording")
  val recording: Recording? = null,

  /**
   * The meeting transcript
   */
  @Json(name = "transcript")
  val transcript: Transcript? = null,
) {
  /**
   * Recording details
   */
  data class Recording(
    /**
     * A link to the meeting recording
     */
    @Json(name = "url")
    val url: String,

    /**
     * The size of the file, in bytes
     */
    @Json(name = "size")
    val size: Int,

    /**
     * The file name
     */
    @Json(name = "name")
    val name: String,

    /**
     * The file type/MIME type
     */
    @Json(name = "type")
    val type: String,

    /**
     * When the file was created (Unix timestamp)
     */
    @Json(name = "created_at")
    val createdAt: Long,

    /**
     * When the file will expire (Unix timestamp)
     */
    @Json(name = "expires_at")
    val expiresAt: Long,

    /**
     * Time-to-live in seconds until the file will be deleted off Nylas' storage server
     */
    @Json(name = "ttl")
    val ttl: Int,
  )

  /**
   * Transcript details
   */
  data class Transcript(
    /**
     * A link to the meeting transcript
     */
    @Json(name = "url")
    val url: String,

    /**
     * The size of the file, in bytes
     */
    @Json(name = "size")
    val size: Int,

    /**
     * The file name
     */
    @Json(name = "name")
    val name: String,

    /**
     * The file type/MIME type
     */
    @Json(name = "type")
    val type: String,

    /**
     * When the file was created (Unix timestamp)
     */
    @Json(name = "created_at")
    val createdAt: Long,

    /**
     * When the file will expire (Unix timestamp)
     */
    @Json(name = "expires_at")
    val expiresAt: Long,

    /**
     * Time-to-live in seconds until the file will be deleted off Nylas' storage server
     */
    @Json(name = "ttl")
    val ttl: Int,
  )
}
