package com.nylas.models

/**
 * Class representing a Nylas update connector request.
 */
data class UpdateConnectorRequest(
  /**
   * Custom name of the connector
   */
  val name: String?,
  /**
   * The OAuth provider credentials and settings
   */
  val settings: Map<String, Any>?,
  /**
   * The OAuth scopes
   */
  val scope: List<String>?,
)
