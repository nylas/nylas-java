package com.nylas.models

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

class ListThreadsQueryParamsTest {

  @Test
  fun `builder inFolder with string creates single item list internally`() {
    val queryParams = ListThreadsQueryParams.Builder()
      .inFolder("test-folder-id")
      .build()

    assertEquals(listOf("test-folder-id"), queryParams.inFolder)
  }

  @Test
  fun `builder inFolder with null string creates null internally`() {
    val queryParams = ListThreadsQueryParams.Builder()
      .inFolder(null as String?)
      .build()

    assertNull(queryParams.inFolder)
  }

  @Test
  fun `builder inFolder with list preserves list as-is`() {
    val folderIds = listOf("folder1", "folder2", "folder3")
    val queryParams = ListThreadsQueryParams.Builder()
      .inFolder(folderIds)
      .build()

    assertEquals(folderIds, queryParams.inFolder)
  }

  @Test
  fun `builder inFolder with empty list preserves empty list`() {
    val queryParams = ListThreadsQueryParams.Builder()
      .inFolder(emptyList<String>())
      .build()

    assertEquals(emptyList<String>(), queryParams.inFolder)
  }

  @Test
  fun `convertToMap uses only first folder ID when multiple are provided`() {
    val queryParams = ListThreadsQueryParams(
      inFolder = listOf("folder1", "folder2", "folder3"),
    )

    val map = queryParams.convertToMap()

    assertEquals("folder1", map["in"])
  }

  @Test
  fun `convertToMap handles single folder ID correctly`() {
    val queryParams = ListThreadsQueryParams(
      inFolder = listOf("single-folder"),
    )

    val map = queryParams.convertToMap()

    assertEquals("single-folder", map["in"])
  }

  @Test
  fun `convertToMap excludes in parameter when list is empty`() {
    val queryParams = ListThreadsQueryParams(
      inFolder = emptyList(),
    )

    val map = queryParams.convertToMap()

    assertFalse(map.containsKey("in"))
  }

  @Test
  fun `convertToMap excludes in parameter when null`() {
    val queryParams = ListThreadsQueryParams(
      inFolder = null,
    )

    val map = queryParams.convertToMap()

    assertFalse(map.containsKey("in"))
  }

  @Test
  fun `convertToMap preserves other parameters while handling inFolder`() {
    val queryParams = ListThreadsQueryParams(
      limit = 10,
      pageToken = "abc-123",
      subject = "Test Subject",
      inFolder = listOf("folder1", "folder2"),
      unread = true,
    )

    val map = queryParams.convertToMap()

    assertEquals(10.0, map["limit"])
    assertEquals("abc-123", map["page_token"])
    assertEquals("Test Subject", map["subject"])
    assertEquals("folder1", map["in"]) // Only first folder ID
    assertEquals(true, map["unread"])
  }

  @Test
  fun `string inFolder parameter through builder creates expected query map`() {
    val queryParams = ListThreadsQueryParams.Builder()
      .inFolder("single-folder")
      .limit(50)
      .unread(false)
      .build()

    val map = queryParams.convertToMap()

    assertEquals("single-folder", map["in"])
    assertEquals(50.0, map["limit"])
    assertEquals(false, map["unread"])
  }

  @Test
  fun `overriding inFolder parameter in builder works correctly`() {
    val queryParams = ListThreadsQueryParams.Builder()
      .inFolder(listOf("folder1", "folder2")) // Set list first
      .inFolder("final-folder") // Override with string
      .build()

    assertEquals(listOf("final-folder"), queryParams.inFolder)
    assertEquals("final-folder", queryParams.convertToMap()["in"])
  }

  @Test
  fun `overriding string inFolder with list in builder works correctly`() {
    val queryParams = ListThreadsQueryParams.Builder()
      .inFolder("initial-folder") // Set string first
      .inFolder(listOf("folder1", "folder2")) // Override with list
      .build()

    assertEquals(listOf("folder1", "folder2"), queryParams.inFolder)
    assertEquals("folder1", queryParams.convertToMap()["in"])
  }
}
