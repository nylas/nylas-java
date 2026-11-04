package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas events batch response.
 * Returned for both 200 (all operations succeeded) and 207 (at least one operation failed) responses.
 */
data class BatchEventsResponse(
  /**
   * The results, one per operation, in the same order as the request operations.
   */
  @Json(name = "data")
  val data: List<BatchEventResult> = emptyList(),
  /**
   * The request ID.
   */
  @Json(name = "request_id")
  val requestId: String = "",
)

/**
 * Class representation of the result of a single operation in an events batch request.
 */
data class BatchEventResult(
  /**
   * The HTTP status of this operation.
   */
  @Json(name = "status")
  val status: Int,
  /**
   * The created or updated event. Null for delete operations and failed operations.
   */
  @Json(name = "data")
  val data: Event? = null,
  /**
   * The error, if this operation failed.
   * Only [NylasApiError.type], [NylasApiError.message] and [NylasApiError.providerError] are set;
   * use [status] for this operation's HTTP status and [BatchEventsResponse.requestId] for the request ID.
   */
  @Json(name = "error")
  val error: NylasApiError? = null,
  /**
   * Seconds to wait before retrying, if the provider throttled this operation.
   */
  @Json(name = "retry_after")
  val retryAfter: Double? = null,
  /**
   * True if the provider created the event before a later step failed.
   * Retrying this operation would create a duplicate event.
   */
  @Json(name = "provider_event_created")
  val providerEventCreated: Boolean? = null,
)
