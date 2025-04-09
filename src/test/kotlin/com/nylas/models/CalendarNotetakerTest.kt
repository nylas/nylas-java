package com.nylas.models

import com.nylas.util.JsonHelper
import okio.Buffer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class CalendarNotetakerTest {
  @Test
  fun `CalendarNotetaker serializes properly`() {
    val adapter = JsonHelper.moshi().adapter(CalendarNotetaker::class.java)
    val jsonBuffer = Buffer().writeUtf8(
      """
        {
          "name": "Custom Notetaker",
          "meeting_settings": {
            "video_recording": true,
            "audio_recording": false,
            "transcription": true
          },
          "rules": {
            "event_selection": ["internal", "external"],
            "participant_filter": {
              "participants_gte": 3,
              "participants_lte": 20
            }
          }
        }
      """.trimIndent(),
    )

    val notetaker = adapter.fromJson(jsonBuffer)!!
    assertIs<CalendarNotetaker>(notetaker)
    assertEquals("Custom Notetaker", notetaker.name)
    assertEquals(true, notetaker.meetingSettings?.videoRecording)
    assertEquals(false, notetaker.meetingSettings?.audioRecording)
    assertEquals(true, notetaker.meetingSettings?.transcription)
    assertEquals(2, notetaker.rules?.eventSelection?.size)
    assertEquals(CalendarNotetaker.EventSelectionType.INTERNAL, notetaker.rules?.eventSelection?.get(0))
    assertEquals(CalendarNotetaker.EventSelectionType.EXTERNAL, notetaker.rules?.eventSelection?.get(1))
    assertEquals(3, notetaker.rules?.participantFilter?.participantsGte)
    assertEquals(20, notetaker.rules?.participantFilter?.participantsLte)
  }

  @Test
  fun `CalendarNotetaker deserializes with minimal fields`() {
    val adapter = JsonHelper.moshi().adapter(CalendarNotetaker::class.java)
    val jsonBuffer = Buffer().writeUtf8(
      """
        {
          "name": "Minimal Notetaker"
        }
      """.trimIndent(),
    )

    val notetaker = adapter.fromJson(jsonBuffer)!!
    assertIs<CalendarNotetaker>(notetaker)
    assertEquals("Minimal Notetaker", notetaker.name)
    assertNull(notetaker.meetingSettings)
    assertNull(notetaker.rules)
  }
}
