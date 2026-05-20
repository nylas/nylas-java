package com.nylas.util

import com.nylas.models.NullableField
import com.squareup.moshi.Types
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class NullableFieldAdapterFactoryTest {
  private val moshi = JsonHelper.moshi()
  private val adapter = moshi.adapter<NullableField<String>>(
    Types.newParameterizedType(NullableField::class.java, String::class.java),
  )

  @Test
  fun `create returns null for non-NullableField type`() {
    val result = NullableFieldAdapterFactory().create(String::class.java, emptySet(), moshi)
    assertNull(result)
  }

  @Test
  fun `toJson with null NullableField writes JSON null`() {
    assertEquals("null", adapter.toJson(null))
  }

  @Test
  fun `fromJson with JSON null returns NullableField Clear`() {
    assertIs<NullableField.Clear>(adapter.fromJson("null"))
  }

  @Test
  fun `fromJson with JSON string returns NullableField Value`() {
    val result = adapter.fromJson("\"hello\"")
    assertIs<NullableField.Value<String>>(result)
    assertEquals("hello", result.v)
  }
}
