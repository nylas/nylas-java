package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing a Nylas update connector request.
 */
sealed class UpdateConnectorRequest {
  /**
   * Class representing a Google connector creation request.
   */
  data class Google(
    /**
     * The Google OAuth provider credentials and settings
     */
    @Json(name = "settings")
    val settings: GoogleConnectorSettings? = null,
    /**
     * The Google OAuth scopes
     */
    @Json(name = "scope")
    val scope: List<String>? = null,
  ) : UpdateConnectorRequest() {
    class Builder {
      private var settings: GoogleConnectorSettings? = null
      private var scope: List<String>? = null

      /**
       * Set the Google OAuth provider credentials and settings
       * @param settings The Google OAuth provider credentials and settings
       * @return The builder
       */
      fun settings(settings: GoogleConnectorSettings) = apply { this.settings = settings }

      /**
       * Set the Google OAuth scopes
       * @param scope The Google OAuth scopes
       * @return The builder
       */
      fun scope(scope: List<String>) = apply { this.scope = scope }

      /**
       * Build the Google connector creation request
       * @return The Google connector creation request
       */
      fun build() = Google(settings, scope)
    }
  }

  /**
   * Class representing a Microsoft connector creation request.
   */
  data class Microsoft(
    /**
     * The Microsoft OAuth provider credentials and settings
     */
    @Json(name = "settings")
    val settings: MicrosoftConnectorSettings? = null,
    /**
     * The Microsoft OAuth scopes
     */
    @Json(name = "scope")
    val scope: List<String>? = null,
  ) : UpdateConnectorRequest() {
    class Builder {
      private var settings: MicrosoftConnectorSettings? = null
      private var scope: List<String>? = null

      /**
       * Set the Microsoft OAuth provider credentials and settings
       * @param settings The Microsoft OAuth provider credentials and settings
       * @return The builder
       */
      fun settings(settings: MicrosoftConnectorSettings) = apply { this.settings = settings }

      /**
       * Set the Microsoft OAuth scopes
       * @param scope The Microsoft OAuth scopes
       * @return The builder
       */
      fun scope(scope: List<String>) = apply { this.scope = scope }

      /**
       * Build the Microsoft connector creation request
       * @return The Microsoft connector creation request
       */
      fun build() = Microsoft(settings, scope)
    }
  }
}
