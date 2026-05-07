package com.nylas.util

import kotlin.test.Test
import kotlin.test.assertEquals

class PathEncoderTest {
  @Test
  fun `plain ASCII strings are returned unchanged`() {
    assertEquals("thread-123", PathEncoder.encode("thread-123"))
  }

  @Test
  fun `forward slash is encoded as %2F`() {
    assertEquals("INBOX%2Fthread-123", PathEncoder.encode("INBOX/thread-123"))
  }

  @Test
  fun `multiple slashes are all encoded`() {
    assertEquals("a%2Fb%2Fc", PathEncoder.encode("a/b/c"))
  }

  @Test
  fun `space is encoded as %20 not plus`() {
    assertEquals("hello%20world", PathEncoder.encode("hello world"))
  }

  @Test
  fun `at sign is encoded as %40`() {
    assertEquals("user%40example.com", PathEncoder.encode("user@example.com"))
  }

  @Test
  fun `plus sign is encoded as %2B`() {
    assertEquals("a%2Bb", PathEncoder.encode("a+b"))
  }

  @Test
  fun `empty string returns empty string`() {
    assertEquals("", PathEncoder.encode(""))
  }

  @Test
  fun `UUID-style IDs are returned unchanged`() {
    val uuid = "ca8f1733-6063-40cc-a2e3-ec7274abef11"
    assertEquals(uuid, PathEncoder.encode(uuid))
  }
}
