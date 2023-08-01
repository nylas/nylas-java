package com.nylas.models

import com.squareup.moshi.*

/**
 * This sealed class represents the different types of conferencing configurations.
 */
sealed class Conferencing {
  /**
   * Class representation of a conferencing autocreate object
   */
  data class Autocreate(
    /**
     * The conferencing provider
     */
    @Json(name = "provider")
    val provider: ConferencingProvider,
    /**
     * Empty dict to indicate an intention to autocreate a video link.
     * Additional provider settings may be included in autocreate.settings, but Nylas does not validate these.
     */
    @Json(name = "autocreate")
    val autocreate: Map<String, Any> = emptyMap(),
  ) : Conferencing()

  /**
   * Class representation of a conferencing details object
   */
  data class Details(
    /**
     * The conferencing provider
     */
    @Json(name = "provider")
    val provider: ConferencingProvider,
    /**
     * The conferencing details
     */
    @Json(name = "details")
    val details: Config,
  ) : Conferencing() {
    data class Config(
      /**
       * The conferencing meeting code. Used for Zoom.
       */
      @Json(name = "meeting_code")
      val meetingCode: String? = null,
      /**
       * The conferencing meeting password. Used for Zoom.
       */
      @Json(name = "password")
      val password: String? = null,
      /**
       * The conferencing meeting url.
       */
      @Json(name = "url")
      val url: String? = null,
      /**
       * The conferencing meeting pin. Used for Google Meet.
       */
      @Json(name = "pin")
      val pin: String? = null,
      /**
       * The conferencing meeting phone numbers. Used for Google Meet.
       */
      @Json(name = "phone")
      val phone: List<String>? = null,
    )
  }
}
