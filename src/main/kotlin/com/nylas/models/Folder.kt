package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of the Nylas folder response.
 */
data class Folder(
  /**
   * A globally unique object identifier.
   */
  @Json(name = "id")
  val id: String,
  /**
   * A Grant ID of the Nylas account.
   */
  @Json(name = "grant_id")
  val grantId: String,
  /**
   * Indicates if the folder is user created or system created. (Google Only)
   */
  @Json(name = "system_folder")
  val systemFolder: Boolean,
  /**
   * The number of immediate child folders in the current folder. (Microsoft only)
   */
  @Json(name = "child_count")
  val childCount: Int,
  /**
   * The number of unread items inside of a folder.
   */
  @Json(name = "unread_count")
  val unreadCount: Int,
  /**
   * The number of items inside of a folder.
   */
  @Json(name = "total_count")
  val totalCount: Int,
  /**
   * The type of object.
   */
  @Json(name = "object")
  val obj: String = "folder",
  /**
   * Folder name
   */
  @Json(name = "name")
  val name: String? = null,
  /**
   * ID of the parent folder. (Microsoft only)
   */
  @Json(name = "parent_id")
  val parentId: String? = null,
  /**
   * Folder background color. (Google only)
   */
  @Json(name = "background_color")
  val backgroundColor: String? = null,
  /**
   * Folder text color. (Google only)
   */
  @Json(name = "text_color")
  val textColor: String? = null,
)
