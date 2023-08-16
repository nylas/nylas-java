package com.nylas.models

import com.squareup.moshi.Json

/**
 * Enum representing the available webhook triggers.
 */
enum class WebhookTriggers {
  @Json(name = "calendar.created")
  CALENDAR_CREATED,

  @Json(name = "calendar.updated")
  CALENDAR_UPDATED,

  @Json(name = "calendar.deleted")
  CALENDAR_DELETED,

  @Json(name = "event.created")
  EVENT_CREATED,

  @Json(name = "event.updated")
  EVENT_UPDATED,

  @Json(name = "event.deleted")
  EVENT_DELETED,

  @Json(name = "grant.created")
  GRANT_CREATED,

  @Json(name = "grant.updated")
  GRANT_UPDATED,

  @Json(name = "grant.deleted")
  GRANT_DELETED,

  @Json(name = "grant.expired")
  GRANT_EXPIRED,

  @Json(name = "message.send_success")
  MESSAGE_SEND_SUCCESS,

  @Json(name = "message.send_failed")
  MESSAGE_SEND_FAILED,
}
