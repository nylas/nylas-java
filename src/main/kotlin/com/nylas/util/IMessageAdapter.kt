package com.nylas.util

import com.nylas.models.Draft
import com.nylas.models.IMessage
import com.nylas.models.Message
import com.squareup.moshi.*

/**
 * This class is used to serialize and deserialize the IMessage interface.
 * @suppress Not for public use.
 */
class IMessageAdapter {
  @FromJson
  fun fromJson(
    reader: JsonReader,
    messageAdapter: JsonAdapter<Message>,
    draftAdapter: JsonAdapter<Draft>,
  ): IMessage? {
    val map = reader.readJsonValue() as? Map<*, *> ?: return null
    return when {
      map.containsKey("object") && map["object"]?.equals("drafts") == true -> draftAdapter.fromJsonValue(map)
      else -> messageAdapter.fromJsonValue(map)
    }
  }

  @ToJson
  fun toJson(
    writer: JsonWriter,
    value: IMessage?,
    messageAdapter: JsonAdapter<Message>,
    draftAdapter: JsonAdapter<Draft>,
  ) {
    when (value) {
      is Message -> messageAdapter.toJson(writer, value)
      is Draft -> draftAdapter.toJson(writer, value)
      else -> writer.nullValue()
    }
  }
}
