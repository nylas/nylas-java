package com.nylas.models

import com.nylas.util.JsonHelper
import okio.Buffer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class EventNotetakerTest {
  @Test
  fun `EventNotetaker serializes properly`() {
    val adapter = JsonHelper.moshi().adapter(EventNotetaker::class.java)
    val jsonBuffer = Buffer().writeUtf8(
      """
        {
          "id": "notetaker-123",
          "name": "Custom Event Notetaker",
          "meeting_settings": {
            "video_recording": false,
            "audio_recording": true,
            "transcription": true
          }
        }
      """.trimIndent(),
    )

    val notetaker = adapter.fromJson(jsonBuffer)!!
    assertIs<EventNotetaker>(notetaker)
    assertEquals("notetaker-123", notetaker.id)
    assertEquals("Custom Event Notetaker", notetaker.name)
    assertEquals(false, notetaker.meetingSettings?.videoRecording)
    assertEquals(true, notetaker.meetingSettings?.audioRecording)
    assertEquals(true, notetaker.meetingSettings?.transcription)
  }

  @Test
  fun `EventNotetaker deserializes with minimal fields`() {
    val adapter = JsonHelper.moshi().adapter(EventNotetaker::class.java)
    val jsonBuffer = Buffer().writeUtf8(
      """
        {
          "id": "notetaker-456"
        }
      """.trimIndent(),
    )

    val notetaker = adapter.fromJson(jsonBuffer)!!
    assertIs<EventNotetaker>(notetaker)
    assertEquals("notetaker-456", notetaker.id)
    assertEquals("Nylas Notetaker", notetaker.name) // Default value
    assertNull(notetaker.meetingSettings)
  }
}
