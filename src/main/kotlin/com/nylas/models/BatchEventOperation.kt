package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a single operation in an events batch request.
 *
 * Use the [create], [update] and [delete] factory methods to build an operation,
 * so that each operation always carries the fields its method requires.
 */
class BatchEventOperation private constructor(
  /**
   * The operation to run.
   */
  @Json(name = "method")
  val method: BatchEventMethod,
  /**
   * The ID of the event to update or delete. Null for create operations.
   */
  @Json(name = "event_id")
  val eventId: String? = null,
  /**
   * The request body: a [CreateEventRequest] for create operations,
   * an [UpdateEventRequest] for update operations, and null for delete operations.
   * Typed as Any? so one field holds either request type; the factory methods keep it type-safe.
   */
  @Json(name = "body")
  val body: Any? = null,
) {
  companion object {
    /**
     * Build a create operation.
     * @param requestBody The values to create the event with
     * @return The create operation
     */
    @JvmStatic
    fun create(requestBody: CreateEventRequest) = BatchEventOperation(BatchEventMethod.CREATE, body = requestBody)

    /**
     * Build an update operation.
     * @param eventId The ID of the event to update
     * @param requestBody The values to update the event with
     * @return The update operation
     */
    @JvmStatic
    fun update(eventId: String, requestBody: UpdateEventRequest) = BatchEventOperation(BatchEventMethod.UPDATE, eventId, requestBody)

    /**
     * Build a delete operation.
     * @param eventId The ID of the event to delete
     * @return The delete operation
     */
    @JvmStatic
    fun delete(eventId: String) = BatchEventOperation(BatchEventMethod.DELETE, eventId)
  }
}

/**
 * Enum representation of the operations supported in an events batch request.
 */
enum class BatchEventMethod {
  @Json(name = "create")
  CREATE,

  @Json(name = "update")
  UPDATE,

  @Json(name = "delete")
  DELETE,
}
