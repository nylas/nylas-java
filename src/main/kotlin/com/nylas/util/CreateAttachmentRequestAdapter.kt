package com.nylas.util

import com.nylas.models.CreateAttachmentRequest
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson

/**
 * This class is used to serialize and deserialize the CreateAttachmentRequest class.
 * @suppress Not for public use.
 */
class CreateAttachmentRequestAdapter {
  @ToJson
  @Throws(UnsupportedOperationException::class)
  fun toJson(writer: JsonWriter, value: CreateAttachmentRequest?) {
    writer.beginObject()
    writer.name("filename").value(value?.filename)
    writer.name("content_type").value(value?.contentType)
    writer.name("size").value(value?.size)
    value?.isInline?.let { writer.name("is_inline").value(it) }
    value?.contentId?.let { writer.name("content_id").value(it) }
    value?.contentDisposition?.let { writer.name("content_disposition").value(it) }

    // Encode the file stream to base64
    value?.content?.let {
      val base64Content = it.use { stream ->
        FileUtils.encodeStreamToBase64(stream)
      }
      writer.name("content").value(base64Content)
    }

    writer.endObject()
  }

  @FromJson
  @Throws(UnsupportedOperationException::class)
  fun fromJson(reader: com.squareup.moshi.JsonReader): CreateAttachmentRequest? {
    throw UnsupportedOperationException("Deserialization not supported")
  }
}
