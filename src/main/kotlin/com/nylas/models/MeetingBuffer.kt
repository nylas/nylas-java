package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of the meeting buffer object within an availability request.
 */
data class MeetingBuffer(
  /**
   * The amount of buffer time in increments of 5 minutes to add before existing meetings.
   * Defaults to 0.
   */
  @Json(name = "before")
  val before: Int? = null,
  /**
   * The amount of buffer time in increments of 5 minutes to add after existing meetings.
   * Defaults to 0.
   */
  @Json(name = "after")
  val after: Int? = null,
) {
  /**
   * A builder for creating a [MeetingBuffer].
   */
  data class Builder(
    private var before: Int? = null,
    private var after: Int? = null,
  ) {
    /**
     * Set the amount of buffer time in increments of 5 minutes to add before existing meetings.
     * Defaults to 0.
     * @param before The amount of buffer time in increments of 5 minutes to add before existing meetings.
     * @return The builder.
     */
    fun before(before: Int) = apply { this.before = before }

    /**
     * Set the amount of buffer time in increments of 5 minutes to add after existing meetings.
     * Defaults to 0.
     * @param after The amount of buffer time in increments of 5 minutes to add after existing meetings.
     * @return The builder.
     */
    fun after(after: Int) = apply { this.after = after }

    /**
     * Build the [MeetingBuffer] object.
     * @return The meeting buffer object.
     */
    fun build() = MeetingBuffer(before, after)
  }
}
