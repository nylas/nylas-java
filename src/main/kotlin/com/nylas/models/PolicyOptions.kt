package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas policy options object.
 */
data class PolicyOptions(
  /**
   * Additional folders to create in the agent account mailbox.
   */
  @Json(name = "additional_folders")
  val additionalFolders: List<String>? = null,
  /**
   * If true, enables CIDR aliasing for IP-based allow/block rules.
   */
  @Json(name = "use_cidr_aliasing")
  val useCidrAliasing: Boolean? = null,
)
