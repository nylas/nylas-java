package com.nylas.models

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class FindMessageQueryParamsTests {
  @Test
  fun `FindMessageQueryParams with fields builds correctly`() {
    val queryParams = FindMessageQueryParams(fields = MessageFields.INCLUDE_TRACKING_OPTIONS)

    assertEquals(MessageFields.INCLUDE_TRACKING_OPTIONS, queryParams.fields)
  }

  @Test
  fun `FindMessageQueryParams builder pattern works correctly`() {
    val queryParams = FindMessageQueryParams.Builder()
      .fields(MessageFields.RAW_MIME)
      .build()

    assertEquals(MessageFields.RAW_MIME, queryParams.fields)
  }

  @Test
  fun `FindMessageQueryParams builder with null fields builds correctly`() {
    val queryParams = FindMessageQueryParams.Builder()
      .fields(null)
      .build()

    assertEquals(null, queryParams.fields)
  }
}
