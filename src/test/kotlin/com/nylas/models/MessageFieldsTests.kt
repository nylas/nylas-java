package com.nylas.models

import com.nylas.util.JsonHelper
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MessageFieldsTests {
  @Test
  fun `MessageFields enum values serialize correctly`() {
    val adapter = JsonHelper.moshi().adapter(MessageFields::class.java)

    assertEquals("\"standard\"", adapter.toJson(MessageFields.STANDARD))
    assertEquals("\"include_headers\"", adapter.toJson(MessageFields.INCLUDE_HEADERS))
    assertEquals("\"include_tracking_options\"", adapter.toJson(MessageFields.INCLUDE_TRACKING_OPTIONS))
    assertEquals("\"raw_mime\"", adapter.toJson(MessageFields.RAW_MIME))
  }

  @Test
  fun `MessageFields enum values deserialize correctly`() {
    val adapter = JsonHelper.moshi().adapter(MessageFields::class.java)

    assertEquals(MessageFields.STANDARD, adapter.fromJson("\"standard\""))
    assertEquals(MessageFields.INCLUDE_HEADERS, adapter.fromJson("\"include_headers\""))
    assertEquals(MessageFields.INCLUDE_TRACKING_OPTIONS, adapter.fromJson("\"include_tracking_options\""))
    assertEquals(MessageFields.RAW_MIME, adapter.fromJson("\"raw_mime\""))
  }
}
