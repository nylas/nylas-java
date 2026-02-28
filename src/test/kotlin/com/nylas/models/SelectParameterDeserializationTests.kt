package com.nylas.models

import com.nylas.util.JsonHelper
import okio.Buffer
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

/**
 * Tests to verify that models can be deserialized properly when using the select parameter,
 * which means some fields may be missing from the response JSON.
 */
class SelectParameterDeserializationTests {

  @Test
  fun `Folder deserializes properly with minimal select fields (id only)`() {
    val adapter = JsonHelper.moshi().adapter(Folder::class.java)
    val jsonBuffer = Buffer().writeUtf8(
      """
      {
        "id": "folder-123",
        "object": "folder"
      }
      """.trimIndent(),
    )

    val folder = adapter.fromJson(jsonBuffer)!!
    assertIs<Folder>(folder)
    assertEquals("folder-123", folder.id)
    assertEquals("folder", folder.obj)
    assertNull(folder.grantId) // Should be null when not included in select
    assertNull(folder.name)
  }

  @Test
  fun `Folder deserializes properly with select id,name (no grant_id)`() {
    val adapter = JsonHelper.moshi().adapter(Folder::class.java)
    val jsonBuffer = Buffer().writeUtf8(
      """
      {
        "id": "folder-123",
        "name": "Inbox",
        "object": "folder"
      }
      """.trimIndent(),
    )

    val folder = adapter.fromJson(jsonBuffer)!!
    assertIs<Folder>(folder)
    assertEquals("folder-123", folder.id)
    assertEquals("Inbox", folder.name)
    assertEquals("folder", folder.obj)
    assertNull(folder.grantId) // Should be null when not included in select
  }

  @Test
  fun `Contact deserializes properly with minimal select fields (id only)`() {
    val adapter = JsonHelper.moshi().adapter(Contact::class.java)
    val jsonBuffer = Buffer().writeUtf8(
      """
      {
        "id": "contact-123",
        "object": "contact"
      }
      """.trimIndent(),
    )

    val contact = adapter.fromJson(jsonBuffer)!!
    assertIs<Contact>(contact)
    assertEquals("contact-123", contact.id)
    assertNull(contact.grantId) // Should be null when not included in select
    assertNull(contact.displayName)
  }

  @Test
  fun `Message deserializes properly with minimal select fields (id only)`() {
    val adapter = JsonHelper.moshi().adapter(Message::class.java)
    val jsonBuffer = Buffer().writeUtf8(
      """
      {
        "id": "message-123",
        "object": "message"
      }
      """.trimIndent(),
    )

    val message = adapter.fromJson(jsonBuffer)!!
    assertIs<Message>(message)
    assertEquals("message-123", message.id)
    assertNull(message.grantId) // Should be null when not included in select
    assertNull(message.subject)
  }

  @Test
  fun `Thread deserializes properly with minimal select fields (id only)`() {
    val adapter = JsonHelper.moshi().adapter(Thread::class.java)
    val jsonBuffer = Buffer().writeUtf8(
      """
      {
        "id": "thread-123",
        "object": "thread"
      }
      """.trimIndent(),
    )

    val thread = adapter.fromJson(jsonBuffer)!!
    assertIs<Thread>(thread)
    assertEquals("thread-123", thread.id)
    assertNull(thread.grantId) // Should be null when not included in select
    assertNull(thread.subject)
  }

  @Test
  fun `Event deserializes properly with minimal select fields (id only)`() {
    val adapter = JsonHelper.moshi().adapter(Event::class.java)
    val jsonBuffer = Buffer().writeUtf8(
      """
      {
        "id": "event-123",
        "object": "event"
      }
      """.trimIndent(),
    )

    val event = adapter.fromJson(jsonBuffer)!!
    assertIs<Event>(event)
    assertEquals("event-123", event.id)
    assertNull(event.grantId) // Should be null when not included in select
    assertNull(event.title)
  }

  @Test
  fun `Calendar deserializes properly with minimal select fields (id only)`() {
    val adapter = JsonHelper.moshi().adapter(Calendar::class.java)
    val jsonBuffer = Buffer().writeUtf8(
      """
      {
        "id": "calendar-123",
        "object": "calendar"
      }
      """.trimIndent(),
    )

    val calendar = adapter.fromJson(jsonBuffer)!!
    assertIs<Calendar>(calendar)
    assertEquals("calendar-123", calendar.id)
    assertNull(calendar.grantId) // Should be null when not included in select
    assertNull(calendar.name)
  }

  @Test
  fun `Attachment deserializes properly with minimal select fields (id only)`() {
    val adapter = JsonHelper.moshi().adapter(Attachment::class.java)
    val jsonBuffer = Buffer().writeUtf8(
      """
      {
        "id": "attachment-123",
        "object": "attachment"
      }
      """.trimIndent(),
    )

    val attachment = adapter.fromJson(jsonBuffer)!!
    assertIs<Attachment>(attachment)
    assertEquals("attachment-123", attachment.id)
    assertNull(attachment.grantId) // Should be null when not included in select
    assertNull(attachment.filename)
  }

  @Test
  fun `ListResponse with Folder array deserializes properly with select fields`() {
    val listResponseType = com.squareup.moshi.Types.newParameterizedType(
      ListResponse::class.java,
      Folder::class.java,
    )
    val adapter = JsonHelper.moshi().adapter<ListResponse<Folder>>(listResponseType)
    val jsonBuffer = Buffer().writeUtf8(
      """
      {
        "data": [
          {
            "id": "folder-1",
            "name": "Inbox",
            "object": "folder"
          },
          {
            "id": "folder-2",
            "name": "Sent",
            "object": "folder"
          }
        ],
        "next_cursor": "abc123"
      }
      """.trimIndent(),
    )

    val response = adapter.fromJson(jsonBuffer)!!
    assertIs<ListResponse<Folder>>(response)
    assertEquals(2, response.data.size)

    val folder1 = response.data[0]
    assertEquals("folder-1", folder1.id)
    assertEquals("Inbox", folder1.name)
    assertNull(folder1.grantId) // Should be null when not in select

    val folder2 = response.data[1]
    assertEquals("folder-2", folder2.id)
    assertEquals("Sent", folder2.name)
    assertNull(folder2.grantId) // Should be null when not in select
  }
}
