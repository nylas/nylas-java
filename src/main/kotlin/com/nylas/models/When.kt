package com.nylas.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory

/**
 * This sealed class represents the different types of event time configurations.
 */
sealed class When {
  @Json(name = "object")
  protected open val obj: WhenType? = null

  /**
   * Returns the [WhenType] of this [When] object.
   */
  fun getObject() = obj

  /**
   * Class representation of an entire day spans without specific times.
   * Your birthday and holidays would be represented as date subobjects.
   */
  data class Date(
    /**
     * Date of occurrence in ISO 8601 format.
     * @see <a href="https://en.wikipedia.org/wiki/ISO_8601#Calendar_dates">ISO 8601</a>
     */
    @Json(name = "date")
    val date: String,
  ) : When() {
    @Json(name = "object")
    override val obj: WhenType = WhenType.DATESPAN
  }

  /**
   * Class representation of a specific dates without clock-based start or end times.
   * A business quarter or academic semester would be represented as datespan subobjects.
   */
  data class Datespan(
    /**
     * The start date in ISO 8601 format.
     * @see <a href="https://en.wikipedia.org/wiki/ISO_8601#Calendar_dates">ISO 8601</a>
     */
    @Json(name = "start_date")
    val startDate: String,
    /**
     * The end date in ISO 8601 format.
     * @see <a href="https://en.wikipedia.org/wiki/ISO_8601#Calendar_dates">ISO 8601</a>
     */
    @Json(name = "end_date")
    val endDate: String,
  ) : When() {
    @Json(name = "object")
    override val obj: WhenType = WhenType.DATESPAN
  }

  /**
   * Class representation of a specific point in time.
   * A meeting at 2pm would be represented as a time subobject.
   */
  data class Time(
    /**
     * A UNIX timestamp representing the time of occurrence.
     */
    @Json(name = "time")
    val time: Int,
    /**
     * If timezone is present, then the value for time will be read with timezone.
     * Timezone using IANA formatted string. (e.g. "America/New_York")
     * @see <a href="https://en.wikipedia.org/wiki/List_of_tz_database_time_zones">List of tz database time zones</a>
     */
    @Json(name = "timezone")
    val timezone: String? = null,
  ) : When() {
    @Json(name = "object")
    override val obj: WhenType = WhenType.TIME
  }

  /**
   * Class representation of a time span with start and end times.
   * An hour lunch meeting would be represented as timespan subobjects.
   */
  data class Timespan(
    /**
     * The start time of the event.
     */
    @Json(name = "start_time")
    val startTime: Int,
    /**
     * The end time of the event.
     */
    @Json(name = "end_time")
    val endTime: Int,
    /**
     * The timezone of the start time.
     * Timezone using IANA formatted string. (e.g. "America/New_York")
     * @see <a href="https://en.wikipedia.org/wiki/List_of_tz_database_time_zones">List of tz database time zones</a>
     */
    @Json(name = "start_timezone")
    val startTimezone: String? = null,
    /**
     * The timezone of the end time.
     * Timezone using IANA formatted string. (e.g. "America/New_York")
     * @see <a href="https://en.wikipedia.org/wiki/List_of_tz_database_time_zones">List of tz database time zones</a>
     */
    @Json(name = "end_timezone")
    val endTimezone: String? = null,
  ) : When() {
    @Json(name = "object")
    override val obj: WhenType = WhenType.TIMESPAN
  }

  companion object {
    /**
     * A JsonAdapter factory for the [When] sealed class.
     * @suppress Not for public use.
     */
    @JvmStatic
    val WHEN_JSON_FACTORY: JsonAdapter.Factory = PolymorphicJsonAdapterFactory.of(When::class.java, "object")
      .withSubtype(Time::class.java, WhenType.TIME.value)
      .withSubtype(Timespan::class.java, WhenType.TIMESPAN.value)
      .withSubtype(Date::class.java, WhenType.DATE.value)
      .withSubtype(Datespan::class.java, WhenType.DATESPAN.value)
  }
}
