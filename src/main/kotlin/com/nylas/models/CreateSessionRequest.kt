package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a request to create a session.
 */
data class CreateSessionRequest(
  /**
   * The identifier of the Configuration to act upon. If you're using slug, configuration_id is not required.
   */
  @Json(name = "configuration_id")
  val configurationId: String? = null,
  /**
   * The time to live for the session.
   */
  @Json(name = "time_to_live")
  val timeToLive: Int? = null,

  /**
   * The slug of the Scheduler Configuration object for the session. You can use slug instead of configuration_id
   */
  @Json(name = "slug")
  val slug: String? = null,
) {
  /**
   * A builder for creating a [CreateSessionRequest].
   * @param configurationId The identifier of the Configuration to act upon.
   * @param timeToLive The time to live for the session.
   * @param slug The slug of the Scheduler Configuration object for the session.
   */
  class Builder {
    private var configurationId: String? = null
    private var timeToLive: Int? = null
    private var slug: String? = null

    /**
     * Set the configuration ID.
     * @param configurationId The identifier of the Configuration to act upon.
     * @return The builder.
     */
    fun configurationId(configurationId: String) = apply { this.configurationId = configurationId }

    /**
     * Set the time to live for the session.
     * @param timeToLive The time to live for the session.
     * @return The builder.
     */
    fun timeToLive(timeToLive: Int) = apply { this.timeToLive = timeToLive }

    /**
     * Set the slug of the Scheduler Configuration object for the session.
     * @param slug The slug of the Scheduler Configuration object for the session.
     * @return The builder.
     */
    fun slug(slug: String) = apply { this.slug = slug }

    /**
     * Build the [CreateSessionRequest].
     * @return The [CreateSessionRequest].
     */
    fun build() = CreateSessionRequest(configurationId, timeToLive, slug)
  }
}
