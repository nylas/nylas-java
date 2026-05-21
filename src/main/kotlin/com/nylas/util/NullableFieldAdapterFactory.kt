package com.nylas.util

import com.nylas.models.NullableField
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Moshi adapter factory for [NullableField].
 * - Kotlin null field → field omitted from JSON (handled by KotlinJsonAdapterFactory before us)
 * - [NullableField.Clear] → `"field": null`
 * - [NullableField.Value] → `"field": <value>`
 * @suppress Not for public use.
 */
class NullableFieldAdapterFactory : JsonAdapter.Factory {
  override fun create(type: Type, annotations: Set<Annotation>, moshi: Moshi): JsonAdapter<*>? {
    if (Types.getRawType(type) != NullableField::class.java) return null
    val innerType = (type as ParameterizedType).actualTypeArguments[0]

    @Suppress("UNCHECKED_CAST")
    val innerAdapter = moshi.adapter<Any>(innerType) as JsonAdapter<Any>
    return NullableFieldAdapter(innerAdapter)
  }

  private class NullableFieldAdapter<T : Any>(
    private val innerAdapter: JsonAdapter<T>,
  ) : JsonAdapter<NullableField<T>>() {
    override fun toJson(writer: JsonWriter, value: NullableField<T>?) {
      when (value) {
        is NullableField.Clear -> {
          // JsonWriter drops nullValue() silently when serializeNulls=false.
          // Force it on just for this write so the explicit null reaches the wire.
          val prev = writer.serializeNulls
          writer.serializeNulls = true
          writer.nullValue()
          writer.serializeNulls = prev
        }
        is NullableField.Value -> innerAdapter.toJson(writer, value.v)
        null -> writer.nullValue()
      }
    }

    override fun fromJson(reader: JsonReader): NullableField<T>? {
      return if (reader.peek() == JsonReader.Token.NULL) {
        reader.nextNull<Any>()
        NullableField.Clear
      } else {
        @Suppress("UNCHECKED_CAST")
        NullableField.Value(innerAdapter.fromJson(reader) as T)
      }
    }
  }
}
