package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.JsonHelper
import com.squareup.moshi.Types

class Notetakers(client: NylasClient) : Resource<Notetaker>(client, Notetaker::class.java) {
  /**
   * Return all Notetakers
   * @param queryParams The query parameters to include in the request
   * @param identifier Grant ID or email account to query.
   * @param overrides Optional request overrides to apply
   * @return The list of Notetakers
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun list(
    queryParams: ListNotetakersQueryParams? = null,
    identifier: String? = null,
    overrides: RequestOverrides? = null,
  ): ListResponse<Notetaker> {
    val path = identifier?.let { String.format("v3/grants/%s/notetakers", it) } ?: "v3/notetakers"
    return listResource(path, queryParams, overrides)
  }

  /**
   * Return a Notetaker
   * @param notetakerId The id of the Notetaker to retrieve.
   * @param identifier Grant ID or email account to query.
   * @param overrides Optional request overrides to apply
   * @return The Notetaker
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun find(
    notetakerId: String,
    identifier: String? = null,
    overrides: RequestOverrides? = null,
  ): Response<Notetaker> {
    val path = identifier?.let { String.format("v3/grants/%s/notetakers/%s", it, notetakerId) } ?: String.format("v3/notetakers/%s", notetakerId)
    return findResource(path, overrides = overrides)
  }

  /**
   * Create a Notetaker
   * @param requestBody The values to create the Notetaker with
   * @param identifier Grant ID or email account in which to create the object.
   * @param overrides Optional request overrides to apply
   * @return The created Notetaker
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun create(
    requestBody: CreateNotetakerRequest,
    identifier: String? = null,
    overrides: RequestOverrides? = null,
  ): Response<Notetaker> {
    val path = identifier?.let { String.format("v3/grants/%s/notetakers", it) } ?: "v3/notetakers"
    val adapter = JsonHelper.moshi().adapter(CreateNotetakerRequest::class.java)
    val serializedRequestBody = adapter.toJson(requestBody)
    return createResource(path, serializedRequestBody, overrides = overrides)
  }

  /**
   * Update a Notetaker
   * @param notetakerId The id of the Notetaker to update.
   * @param requestBody The values to update the Notetaker with
   * @param identifier Grant ID or email account in which to update an object.
   * @param overrides Optional request overrides to apply
   * @return The updated Notetaker
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun update(
    notetakerId: String,
    requestBody: UpdateNotetakerRequest,
    identifier: String? = null,
    overrides: RequestOverrides? = null,
  ): Response<Notetaker> {
    val path = identifier?.let { String.format("v3/grants/%s/notetakers/%s", it, notetakerId) } ?: String.format("v3/notetakers/%s", notetakerId)
    val adapter = JsonHelper.moshi().adapter(UpdateNotetakerRequest::class.java)
    val serializedRequestBody = adapter.toJson(requestBody)
    return updateResource(path, serializedRequestBody, overrides = overrides)
  }

  /**
   * Remove Notetaker from meeting
   * @param notetakerId The id of the Notetaker to remove.
   * @param identifier Grant ID or email account from which to remove the Notetaker.
   * @param overrides Optional request overrides to apply
   * @return The removal response
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun leave(
    notetakerId: String,
    identifier: String? = null,
    overrides: RequestOverrides? = null,
  ): Response<LeaveNotetakerResponse> {
    val path = identifier?.let { String.format("v3/grants/%s/notetakers/%s/leave", it, notetakerId) } ?: String.format("v3/notetakers/%s/leave", notetakerId)
    val responseType = Types.newParameterizedType(Response::class.java, LeaveNotetakerResponse::class.java)
    return client.executePost(path, responseType, null, null, overrides)
  }

  /**
   * Download Notetaker media
   * @param notetakerId The id of the Notetaker to access.
   * @param identifier Grant ID or email account to access.
   * @param overrides Optional request overrides to apply
   * @return The Notetaker media download response
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun downloadMedia(
    notetakerId: String,
    identifier: String? = null,
    overrides: RequestOverrides? = null,
  ): Response<NotetakerMediaResponse> {
    val path = identifier?.let { String.format("v3/grants/%s/notetakers/%s/media", it, notetakerId) } ?: String.format("v3/notetakers/%s/media", notetakerId)
    val type = Types.newParameterizedType(Response::class.java, NotetakerMediaResponse::class.java)
    return client.executeGet(path, type, null, overrides)
  }

  /**
   * Cancel a scheduled Notetaker
   * @param notetakerId The id of the Notetaker to cancel.
   * @param identifier Grant ID or email account to query.
   * @param overrides Optional request overrides to apply
   * @return The cancellation response
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun cancel(
    notetakerId: String,
    identifier: String? = null,
    overrides: RequestOverrides? = null,
  ): DeleteResponse {
    val path = identifier?.let { String.format("v3/grants/%s/notetakers/%s/cancel", it, notetakerId) } ?: String.format("v3/notetakers/%s/cancel", notetakerId)
    return destroyResource(path, overrides = overrides)
  }
}
