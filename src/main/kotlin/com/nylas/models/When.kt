package com.nylas.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory

sealed class When {
  @Json(name = "object")
  protected open val obj: WhenType? = null

  fun getObject() = obj

  data class Date(
    @Json(name = "date")
    val date: String,
  ) : When() {
    @Json(name = "object")
    override val obj: WhenType = WhenType.DATESPAN
  }

  data class Datespan(
    @Json(name = "start_date")
    val startDate: String,
    @Json(name = "end_date")
    val endDate: String,
  ) : When() {
    @Json(name = "object")
    override val obj: WhenType = WhenType.DATESPAN
  }

  data class Time(
    @Json(name = "time")
    val time: Int,
    @Json(name = "timezone")
    val timezone: String? = null,
  ) : When() {
    @Json(name = "object")
    override val obj: WhenType = WhenType.TIME
  }

  data class Timespan(
    @Json(name = "start_time")
    val startTime: Int,
    @Json(name = "end_time")
    val endTime: Int,
    @Json(name = "start_timezone")
    val startTimezone: String? = null,
    @Json(name = "end_timezone")
    val endTimezone: String? = null,
  ) : When() {
    @Json(name = "object")
    override val obj: WhenType = WhenType.TIMESPAN
  }

  companion object {
    @JvmStatic
    val WHEN_JSON_FACTORY: JsonAdapter.Factory = PolymorphicJsonAdapterFactory.of(When::class.java, "object")
      .withSubtype(Time::class.java, WhenType.TIME.value)
      .withSubtype(Timespan::class.java, WhenType.TIMESPAN.value)
      .withSubtype(Date::class.java, WhenType.DATE.value)
      .withSubtype(Datespan::class.java, WhenType.DATESPAN.value)
  }
}
