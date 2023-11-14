package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.JsonHelper

class Folders(client: NylasClient) : Resource<Folder>(client, Folder::class.java) {
  /**
   * Return all Folders
   * @param identifier Grant ID or email account to query.
   * @return The list of Folders
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun list(identifier: String): ListResponse<Folder> {
    val path = String.format("v3/grants/%s/folders", identifier)
    return listResource(path)
  }

  /**
   * Return a Folder
   * @param identifier Grant ID or email account to query.
   * @param folderId The id of the folder to retrieve.
   * @return The folder
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun find(identifier: String, folderId: String): Response<Folder> {
    val path = String.format("v3/grants/%s/folders/%s", identifier, folderId)
    return findResource(path)
  }

  /**
   * Create a Folder
   * @param identifier Grant ID or email account in which to create the object.
   * @param requestBody The values to create the folder with
   * @return The created folder
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun create(identifier: String, requestBody: CreateFolderRequest): Response<Folder> {
    val path = String.format("v3/grants/%s/folders", identifier)
    val adapter = JsonHelper.moshi().adapter(CreateFolderRequest::class.java)
    val serializedRequestBody = adapter.toJson(requestBody)
    return createResource(path, serializedRequestBody)
  }

  /**
   * Update a Folder
   * @param identifier Grant ID or email account in which to update an object.
   * @param folderId The id of the folder to update.
   * @param requestBody The values to update the folder with
   * @return The updated folder
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun update(identifier: String, folderId: String, requestBody: UpdateFolderRequest): Response<Folder> {
    val path = String.format("v3/grants/%s/folders/%s", identifier, folderId)
    val adapter = JsonHelper.moshi().adapter(UpdateFolderRequest::class.java)
    val serializedRequestBody = adapter.toJson(requestBody)
    return updateResource(path, serializedRequestBody)
  }

  /**
   * Delete a Folder
   * @param identifier Grant ID or email account from which to delete an object.
   * @param folderId The id of the folder to delete.
   * @return The deletion response
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun destroy(identifier: String, folderId: String): DeleteResponse {
    val path = String.format("v3/grants/%s/folders/%s", identifier, folderId)
    return destroyResource(path)
  }
}
