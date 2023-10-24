package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing an IMAP connector creation request.
 */
data class ImapCreateConnectorRequest(
  /**
   * Custom name of the connector
   */
  @Json(name = "name")
  override val name: String,
) : CreateConnectorRequest(name, AuthProvider.IMAP)
