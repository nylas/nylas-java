package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas events batch request.
 */
data class BatchEventsRequest(
  /**
   * The operations to run. Results are returned in the same order.
   */
  @Json(name = "operations")
  val operations: List<BatchEventOperation>,
)
