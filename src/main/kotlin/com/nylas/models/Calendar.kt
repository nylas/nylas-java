package com.nylas.models

import com.squareup.moshi.Json

data class Calendar(
    @Json(name = "id")
    val id: String = "",
    @Json(name = "grant_id")
    val grantId: String = "",
    @Json(name = "name")
    val name: String = "",
    @Json(name = "object")
    private val obj: String = "",
    @Json(name = "timezone")
    val timezone: String = "",
    @Json(name = "read_only")
    val readOnly: Boolean = false,
    @Json(name = "is_owned_by_user")
    val isOwnedByUser: Boolean = false,
    @Json(name = "is_primary")
    val isPrimary: Boolean = false,
    @Json(name = "description")
    val description: String? = null,
    @Json(name = "location")
    val location: String? = null,
    @Json(name = "hex_color")
    val hexColor: String? = null,
    @Json(name = "hex_foreground_color")
    val hexForegroundColor: String? = null,
    @Json(name = "metadata")
    val metadata: Map<String, String>? = null
)
