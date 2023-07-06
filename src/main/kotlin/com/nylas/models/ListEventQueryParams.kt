package com.nylas.models

import com.squareup.moshi.Json

data class ListEventQueryParams(
  @Json(name = "calendar_id")
  val calendarId: String,
  @Json(name = "limit")
  val limit: Int? = null,
  @Json(name = "page_token")
  val pageToken: String? = null,
  @Json(name = "show_cancelled")
  val showCancelled: Boolean? = null,
  @Json(name = "event_id")
  val eventId: String? = null,
  @Json(name = "title")
  val title: String? = null,
  @Json(name = "description")
  val description: String? = null,
  @Json(name = "location")
  val location: String? = null,
  @Json(name = "end")
  val end: String? = null,
  @Json(name = "start")
  val start: String? = null,
  @Json(name = "metadata_pair")
  val metadataPair: Map<String, String>? = null,
  @Json(name = "expand_recurring")
  val expandRecurring: Boolean? = null,
  @Json(name = "busy")
  val busy: Boolean? = null,
  @Json(name = "participants")
  val participants: String? = null,
) : IQueryParams {
  data class Builder(
    private val calendarId: String,
  ) {
    private var limit: Int? = null
    private var pageToken: String? = null
    private var showCancelled: Boolean? = null
    private var eventId: String? = null
    private var title: String? = null
    private var description: String? = null
    private var location: String? = null
    private var end: String? = null
    private var start: String? = null
    private var metadataPair: Map<String, String>? = null
    private var expandRecurring: Boolean? = null
    private var busy: Boolean? = null
    private var participants: String? = null

    fun limit(limit: Int?) = apply { this.limit = limit }
    fun pageToken(pageToken: String?) = apply { this.pageToken = pageToken }
    fun showCancelled(showCancelled: Boolean?) = apply { this.showCancelled = showCancelled }
    fun eventId(eventId: String?) = apply { this.eventId = eventId }
    fun title(title: String?) = apply { this.title = title }
    fun description(description: String?) = apply { this.description = description }
    fun location(location: String?) = apply { this.location = location }
    fun end(end: String?) = apply { this.end = end }
    fun start(start: String?) = apply { this.start = start }
    fun metadataPair(metadataPair: Map<String, String>?) = apply { this.metadataPair = metadataPair }
    fun expandRecurring(expandRecurring: Boolean?) = apply { this.expandRecurring = expandRecurring }
    fun busy(busy: Boolean?) = apply { this.busy = busy }
    fun participants(participants: String?) = apply { this.participants = participants }

    fun build() = ListEventQueryParams(
      calendarId,
      limit,
      pageToken,
      showCancelled,
      eventId,
      title,
      description,
      location,
      end,
      start,
      metadataPair,
      expandRecurring,
      busy,
      participants,
    )
  }
}
