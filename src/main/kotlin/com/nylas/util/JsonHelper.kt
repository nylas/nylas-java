package com.nylas.util

import com.nylas.models.GetFreeBusyResponse.Companion.FREE_BUSY_JSON_FACTORY
import com.nylas.models.When.Companion.WHEN_JSON_FACTORY
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.IOException
import java.lang.reflect.Type
import java.util.*

/**
 * This class is used to serialize and deserialize JSON objects.
 */
class JsonHelper {
  companion object {
    private val moshi: Moshi = Moshi.Builder()
      // Date adapters
      .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
      // Custom adapters
      .add(ConferencingAdapter())
      .add(CreateConferencingAdapter())
      .add(UpdateConferencingAdapter())
      .add(CreateWhenAdapter())
      .add(UpdateWhenAdapter())
      .add(CreateWhenAdapter())
      // Polymorphic adapters
      .add(WHEN_JSON_FACTORY)
      .add(FREE_BUSY_JSON_FACTORY)
      .add(KotlinJsonAdapterFactory())
      .build()

    /**
     * Converts a Java object to a JSON string.
     * Recommended for use with Nylas SDK models.
     * @param obj The object to convert.
     * @return The JSON string.
     */
    @JvmStatic
    fun objectToJson(obj: Any): String {
      return adapter<Any>(obj.javaClass).toJson(obj)
    }

    /**
     * Converts a map to a JSON string.
     * @param map The map to convert.
     * @return The JSON string.
     */
    @JvmStatic
    fun mapToJson(map: Map<String, Any>): String {
      return mapAdapter.toJson(map)
    }

    /**
     * Converts a list of objects.
     * @param list The list to convert.
     * @return The JSON string.
     */
    @JvmStatic
    fun listToJson(list: List<Any>): String {
      return listAdapter.toJson(list)
    }

    /**
     * Get an instance of Moshi
     * @return The Moshi instance.
     * @suppress Not for public use.
     */
    @JvmStatic
    fun moshi(): Moshi {
      return moshi
    }

    /**
     * Get a map adapter parameterized with the specified type.
     * @param type The type(s) to parameterize the adapter with.
     * @return The map adapter.
     * @suppress Not for public use.
     */
    @JvmStatic
    fun mapTypeOf(vararg type: Type): Type {
      return Types.newParameterizedType(MutableMap::class.java, *type)
    }

    /**
     * Get a list adapter parameterized with the specified type.
     * @param type The type to parameterize the adapter with.
     * @return The list adapter.
     * @suppress Not for public use.
     */
    @JvmStatic
    fun listTypeOf(type: Type): Type {
      return Types.newParameterizedType(MutableList::class.java, type)
    }

    /**
     * Get a JSON adapter for the specified type.
     * @param type The type to parameterize the adapter with.
     * @return The JSON adapter.
     * @suppress Not for public use.
     */
    @JvmStatic
    fun <T> adapter(type: Type): JsonAdapter<T> {
      return moshi.adapter<Any>(type).indent("  ") as JsonAdapter<T>
    }

    /**
     * Deserialize a JSON string to a Java object.
     * @param adapter The JSON adapter to use.
     * @param json The JSON string to deserialize.
     * @return The deserialized object.
     * @throws IOException if there is an error deserializing the JSON string.
     * @suppress Not for public use.
     */
    @JvmStatic
    fun <T> fromJsonUnchecked(adapter: JsonAdapter<T>, json: String): T? {
      return try {
        adapter.fromJson(json)
      } catch (e: IOException) {
        throw RuntimeException(e)
      }
    }

    /**
     * Get the JSON adapter for a generic map.
     * @return The JSON adapter.
     * @suppress Not for public use.
     */
    @JvmStatic
    val mapAdapter = moshi.adapter<Map<String, Any>>(
      MutableMap::class.java,
    ).indent("  ")

    /**
     * Get the JSON adapter for a generic map with String values.
     * @return The JSON adapter.
     * @suppress Not for public use.
     */
    @JvmStatic
    val jsonMapAdapter = moshi.adapter<Map<String, String>>(
      mapTypeOf(String::class.java, String::class.java),
    ).indent("  ")

    /**
     * Get the JSON adapter for a generic list.
     * @return The JSON adapter.
     * @suppress Not for public use.
     */
    @JvmStatic
    val listAdapter = moshi.adapter<List<Any>>(
      MutableList::class.java,
    ).indent("  ")

    /**
     * Deserialize a JSON string to a map.
     * @param json The JSON string to deserialize.
     * @return The deserialized map.
     * @throws IOException if there is an error deserializing the JSON string.
     * @suppress Not for public use.
     */
    @JvmStatic
    fun jsonToMap(json: String): Map<String, Any> {
      return fromJsonUnchecked(mapAdapter, json)!!
    }

    /**
     * Deserialize a JSON string to a map with String values.
     * @param jsonReader The JSON reader to deserialize from.
     * @return The deserialized map.
     * @throws IOException if there is an error deserializing the JSON string.
     * @suppress Not for public use.
     */
    @Throws(IOException::class)
    @JvmStatic
    fun jsonToMap(jsonReader: JsonReader): Map<String, Any?> {
      val json: MutableMap<String, Any?> = HashMap()
      jsonReader.beginObject()
      while (jsonReader.hasNext()) {
        json[jsonReader.nextName()] = jsonReader.readJsonValue()
      }
      jsonReader.endObject()
      return json
    }

    private val jsonType = MediaType.parse("application/json; charset=utf-8")

    /**
     * Get the JSON media type.
     * @return The JSON media type.
     * @suppress Not for public use.
     */
    @JvmStatic
    fun jsonType(): MediaType? {
      return jsonType
    }

    /**
     * Get a JSON request body for the specified object.
     * @param params The object to serialize.
     * @return The request body.
     * @suppress Not for public use.
     */
    @JvmStatic
    fun jsonRequestBody(params: Map<String, Any>): RequestBody {
      val json = mapToJson(params)
      return jsonRequestBody(json)
    }

    /**
     * Get a JSON request body for the specified object.
     * @param json The string to serialize.
     * @return The request body.
     * @suppress Not for public use.
     */
    @JvmStatic
    fun jsonRequestBody(json: String): RequestBody {
      return RequestBody.create(jsonType(), json)
    }
  }
}
