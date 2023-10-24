package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing a virtual calendar connector creation request.
 */
data class VirtualCalendarsCreateConnectorRequest(
  /**
   * Custom name of the connector
   */
  @Json(name = "name")
  override val name: String,
) : CreateConnectorRequest(name, AuthProvider.VIRTUAL_CALENDAR)
