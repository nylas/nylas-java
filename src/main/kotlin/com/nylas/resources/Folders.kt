package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.JsonHelper

class Folders(client: NylasClient) : Resource<Folder>(client, Folder::class.java) {
  /**
   * Return all Folders
   * @param identifier Grant ID or email account to query.
   * @param queryParams The query parameters to include in the request
   * @param overrides Optional request overrides to apply
   * @return The list of Folders
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun list(identifier: String, queryParams: ListFoldersQueryParams? = null, overrides: RequestOverrides? = null): ListResponse<Folder> {
    val path = String.format("v3/grants/%s/folders", identifier)
    return listResource(path, queryParams, overrides)
  }

  /**
   * Return a Folder
   * @param identifier Grant ID or email account to query.
   * @param folderId The id of the folder to retrieve.
   * @param queryParams The query parameters to include in the request
   * @param overrides Optional request overrides to apply
   * @return The folder
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun find(identifier: String, folderId: String, queryParams: FindFolderQueryParams? = null, overrides: RequestOverrides? = null): Response<Folder> {
    val path = String.format("v3/grants/%s/folders/%s", identifier, folderId)
    return findResource(path, queryParams, overrides = overrides)
  }

  /**
   * Create a Folder
   * @param identifier Grant ID or email account in which to create the object.
   * @param requestBody The values to create the folder with
   * @param overrides Optional request overrides to apply
   * @return The created folder
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun create(identifier: String, requestBody: CreateFolderRequest, overrides: RequestOverrides? = null): Response<Folder> {
    val path = String.format("v3/grants/%s/folders", identifier)
    val adapter = JsonHelper.moshi().adapter(CreateFolderRequest::class.java)
    val serializedRequestBody = adapter.toJson(requestBody)
    return createResource(path, serializedRequestBody, overrides = overrides)
  }

  /**
   * Update a Folder
   * @param identifier Grant ID or email account in which to update an object.
   * @param folderId The id of the folder to update.
   * @param requestBody The values to update the folder with
   * @param overrides Optional request overrides to apply
   * @return The updated folder
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun update(identifier: String, folderId: String, requestBody: UpdateFolderRequest, overrides: RequestOverrides? = null): Response<Folder> {
    val path = String.format("v3/grants/%s/folders/%s", identifier, folderId)
    val adapter = JsonHelper.moshi().adapter(UpdateFolderRequest::class.java)
    val serializedRequestBody = adapter.toJson(requestBody)
    return updateResource(path, serializedRequestBody, overrides = overrides)
  }

  /**
   * Delete a Folder
   * @param identifier Grant ID or email account from which to delete an object.
   * @param folderId The id of the folder to delete.
   * @param overrides Optional request overrides to apply
   * @return The deletion response
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun destroy(identifier: String, folderId: String, overrides: RequestOverrides? = null): DeleteResponse {
    val path = String.format("v3/grants/%s/folders/%s", identifier, folderId)
    return destroyResource(path, overrides = overrides)
  }
}
