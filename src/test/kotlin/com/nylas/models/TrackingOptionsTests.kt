package com.nylas.models

import com.nylas.util.JsonHelper
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TrackingOptionsTests {
  private val adapter = JsonHelper.moshi().adapter(TrackingOptions::class.java)

  @Test
  fun `builder exposes custom tracking hostname`() {
    val options =
      TrackingOptions.Builder()
        .links(true)
        .opens(true)
        .domainName("tracking.example.com")
        .build()

    assertEquals("tracking.example.com", options.domainName)
  }

  @Test
  fun `custom tracking hostname serializes as domain_name`() {
    val options = TrackingOptions(links = true, opens = true, domainName = "tracking.example.com")

    val json = adapter.toJson(options)

    assertEquals("""{"links":true,"opens":true,"domain_name":"tracking.example.com"}""", json)
    assertFalse(json.contains("domainName"))
  }

  @Test
  fun `omitted custom tracking hostname leaves existing JSON unchanged`() {
    val options = TrackingOptions(links = true, opens = false)

    val json = adapter.toJson(options)

    assertEquals("""{"links":true,"opens":false}""", json)
    assertFalse(json.contains("domain_name"))
  }

  @Test
  fun `send request nests custom hostname under tracking_options`() {
    val request =
      SendMessageRequest.Builder(listOf(EmailName("recipient@example.com")))
        .trackingOptions(TrackingOptions(links = true, domainName = "tracking.example.com"))
        .build()

    val json = JsonHelper.moshi().adapter(SendMessageRequest::class.java).toJson(request)

    assertTrue(json.contains(""""tracking_options":{"links":true,"domain_name":"tracking.example.com"}"""))
    assertFalse(json.contains("domainName"))
  }
}
