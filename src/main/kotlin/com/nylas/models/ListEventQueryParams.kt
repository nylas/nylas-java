package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of the query parameters for listing events.
 */
data class ListEventQueryParams(
  /**
   * Specify calendar ID of the event. "primary" is a supported value indicating the user's primary calendar.
   */
  @Json(name = "calendar_id")
  val calendarId: String,
  /**
   * The maximum number of objects to return.
   * This field defaults to 50. The maximum allowed value is 200.
   */
  @Json(name = "limit")
  val limit: Int? = null,
  /**
   * An identifier that specifies which page of data to return.
   * This value should be taken from the [ListResponse.nextCursor] response field.
   */
  @Json(name = "page_token")
  val pageToken: String? = null,
  /**
   * Return events that have a status of cancelled.
   * If an event is recurring, then it returns no matter the value set.
   * Different providers have different semantics for cancelled events.
   */
  @Json(name = "show_cancelled")
  val showCancelled: Boolean? = null,
  /**
   * Return events matching the specified title.
   */
  @Json(name = "title")
  val title: String? = null,
  /**
   * Return events matching the specified description.
   * Graph: NOT supported
   */
  @Json(name = "description")
  val description: String? = null,
  /**
   * Return events matching the specified location.
   */
  @Json(name = "location")
  val location: String? = null,
  /**
   * Return events ending before the specified unix timestamp.
   * Defaults to a month from now. Not respected by metadata filtering.
   */
  @Json(name = "end")
  val end: String? = null,
  /**
   * Return events starting after the specified unix timestamp.
   * Defaults to the current timestamp. Not respected by metadata filtering.
   */
  @Json(name = "start")
  val start: String? = null,
  /**
   * Pass in your metadata key and value pair to search for metadata.
   */
  @Json(name = "metadata_pair")
  val metadataPair: Map<String, String>? = null,
  /**
   * If true, the response will include an event for each occurrence of a recurring event within the requested time range.
   * If false, only a single primary event will be returned for each recurring event.
   * Cannot be used when filtering on metadata.
   * Defaults to false.
   */
  @Json(name = "expand_recurring")
  val expandRecurring: Boolean? = null,
  /**
   * Returns events with a busy status of true.
   */
  @Json(name = "busy")
  val busy: Boolean? = null,
  /**
   * Order results by the specified field.
   * Currently only start is supported.
   */
  @Json(name = "participants")
  val orderBy: String? = null,
) : IQueryParams {
  /**
   * Builder for [ListEventQueryParams].
   */
  data class Builder(
    /**
     * Specify calendar ID of the event. "primary" is a supported value indicating the user's primary calendar.
     */
    private val calendarId: String,
  ) {
    private var limit: Int? = null
    private var pageToken: String? = null
    private var showCancelled: Boolean? = null
    private var title: String? = null
    private var description: String? = null
    private var location: String? = null
    private var end: String? = null
    private var start: String? = null
    private var metadataPair: Map<String, String>? = null
    private var expandRecurring: Boolean? = null
    private var busy: Boolean? = null
    private var orderBy: String? = null

    /**
     * Sets the maximum number of objects to return.
     * This field defaults to 50. The maximum allowed value is 200.
     * @param limit The maximum number of objects to return.
     * @return The builder.
     */
    fun limit(limit: Int?) = apply { this.limit = limit }

    /**
     * Sets the identifier that specifies which page of data to return.
     * This value should be taken from the [ListResponse.nextCursor] response field.
     * @param pageToken The identifier that specifies which page of data to return.
     * @return The builder.
     */
    fun pageToken(pageToken: String?) = apply { this.pageToken = pageToken }

    /**
     * Sets whether to return events that have a status of cancelled.
     * If an event is recurring, then it returns no matter the value set.
     * Different providers have different semantics for cancelled events.
     * @param showCancelled Whether to return events that have a status of cancelled.
     * @return The builder.
     */
    fun showCancelled(showCancelled: Boolean?) = apply { this.showCancelled = showCancelled }

    /**
     * Sets the title of the event.
     * @param title The title of the event.
     * @return The builder.
     */
    fun title(title: String?) = apply { this.title = title }

    /**
     * Sets the description of the event.
     * @param description The description of the event.
     * @return The builder.
     */
    fun description(description: String?) = apply { this.description = description }

    /**
     * Sets the location of the event.
     * @param location The location of the event.
     * @return The builder.
     */
    fun location(location: String?) = apply { this.location = location }

    /**
     * Sets the end time of the event.
     * @param end The end time of the event.
     * @return The builder.
     */
    fun end(end: String?) = apply { this.end = end }

    /**
     * Sets the start time of the event.
     * @param start The start time of the event.
     * @return The builder.
     */
    fun start(start: String?) = apply { this.start = start }

    /**
     * Sets the metadata key and value pair to search for metadata.
     * @param metadataPair The metadata key and value pair to search for metadata.
     * @return The builder.
     */
    fun metadataPair(metadataPair: Map<String, String>?) = apply { this.metadataPair = metadataPair }

    /**
     * Sets whether to return all occurrences of recurring events within the requested time range.
     * If false, only a single primary event will be returned for each recurring event.
     * Cannot be used when filtering on metadata.
     * Defaults to false.
     * @param expandRecurring Whether to return all occurrences of recurring events within the requested time range.
     * @return The builder.
     */
    fun expandRecurring(expandRecurring: Boolean?) = apply { this.expandRecurring = expandRecurring }

    /**
     * Sets whether to return events with a busy status of true.
     * @param busy Whether to return events with a busy status of true.
     * @return The builder.
     */
    fun busy(busy: Boolean?) = apply { this.busy = busy }

    /**
     * Sets the field to order results by.
     * Currently only start is supported.
     * @param orderBy The field to order results by.
     * @return The builder.
     */
    fun orderBy(orderBy: String?) = apply { this.orderBy = orderBy }

    /**
     * Builds a [ListEventQueryParams] instance.
     * @return The [ListEventQueryParams] instance.
     */
    fun build() = ListEventQueryParams(
      calendarId,
      limit,
      pageToken,
      showCancelled,
      title,
      description,
      location,
      end,
      start,
      metadataPair,
      expandRecurring,
      busy,
      orderBy,
    )
  }
}
