package com.nylas.util

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
      // Polymorphic adapters
      .add(WHEN_JSON_FACTORY)
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

    @JvmStatic
    fun <T> adapter(type: Type): JsonAdapter<T> {
      return moshi.adapter<Any>(type).indent("  ") as JsonAdapter<T>
    }

    @JvmStatic
    fun <T> fromJsonUnchecked(adapter: JsonAdapter<T>, json: String): T? {
      return try {
        adapter.fromJson(json)
      } catch (e: IOException) {
        throw RuntimeException(e)
      }
    }

    @JvmStatic
    val mapAdapter = moshi.adapter<Map<String, Any>>(
      MutableMap::class.java,
    ).indent("  ")

    @JvmStatic
    val jsonMapAdapter = moshi.adapter<Map<String, String>>(
      mapTypeOf(String::class.java, String::class.java),
    ).indent("  ")

    @JvmStatic
    val listAdapter = moshi.adapter<List<Any>>(
      MutableList::class.java,
    ).indent("  ")

    @JvmStatic
    fun jsonToMap(json: String): Map<String, Any> {
      return fromJsonUnchecked(mapAdapter, json)!!
    }

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

    @JvmStatic
    fun jsonType(): MediaType? {
      return jsonType
    }

    @JvmStatic
    fun jsonRequestBody(params: Map<String, Any>): RequestBody {
      val json = mapToJson(params)
      return jsonRequestBody(json)
    }

    @JvmStatic
    fun jsonRequestBody(json: String): RequestBody {
      return RequestBody.create(jsonType(), json)
    }
  }
}
