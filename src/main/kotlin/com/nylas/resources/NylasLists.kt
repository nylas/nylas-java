package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.JsonHelper
import com.squareup.moshi.Types

/**
 * Nylas Lists API
 *
 * The Nylas Lists API allows you to create and manage typed collections of domains, TLDs,
 * or email addresses. Lists are referenced by rule conditions via the [RuleConditionOperator.IN_LIST] operator.
 *
 * @param client The configured Nylas API client
 */
class NylasLists(client: NylasClient) : Resource<NylasList>(client, NylasList::class.java) {
  /**
   * Return all lists for your application.
   * @param queryParams Optional query parameters to apply
   * @param overrides Optional request overrides to apply
   * @return The list of Nylas lists
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun list(queryParams: ListNylasListsQueryParams? = null, overrides: RequestOverrides? = null): ListResponse<NylasList> {
    return listResource("v3/lists", queryParams, overrides)
  }

  /**
   * Return a Nylas list.
   * @param listId The ID of the list to retrieve
   * @param overrides Optional request overrides to apply
   * @return The Nylas list
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun find(listId: String, overrides: RequestOverrides? = null): Response<NylasList> {
    val path = String.format("v3/lists/%s", listId)
    return findResource(path, overrides = overrides)
  }

  /**
   * Create a Nylas list.
   * @param requestBody The values to create the list with
   * @param overrides Optional request overrides to apply
   * @return The created Nylas list
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun create(requestBody: CreateNylasListRequest, overrides: RequestOverrides? = null): Response<NylasList> {
    val serializedRequestBody = JsonHelper.moshi().adapter(CreateNylasListRequest::class.java).toJson(requestBody)
    return createResource("v3/lists", serializedRequestBody, overrides = overrides)
  }

  /**
   * Update a Nylas list. Only [UpdateNylasListRequest.name] and [UpdateNylasListRequest.description]
   * can be changed — the list type is immutable after creation.
   * @param listId The ID of the list to update
   * @param requestBody The values to update the list with
   * @param overrides Optional request overrides to apply
   * @return The updated Nylas list
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun update(listId: String, requestBody: UpdateNylasListRequest, overrides: RequestOverrides? = null): Response<NylasList> {
    val path = String.format("v3/lists/%s", listId)
    val serializedRequestBody = JsonHelper.moshi().adapter(UpdateNylasListRequest::class.java).toJson(requestBody)
    return updateResource(path, serializedRequestBody, overrides = overrides)
  }

  /**
   * Delete a Nylas list. Cascades to all items in the list.
   * @param listId The ID of the list to delete
   * @param overrides Optional request overrides to apply
   * @return The deletion response
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun destroy(listId: String, overrides: RequestOverrides? = null): DeleteResponse {
    val path = String.format("v3/lists/%s", listId)
    return destroyResource(path, overrides = overrides)
  }

  /**
   * Return all items in a Nylas list.
   * @param listId The ID of the list
   * @param queryParams Optional query parameters to apply
   * @param overrides Optional request overrides to apply
   * @return The paginated list of items
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun listItems(
    listId: String,
    queryParams: ListNylasListItemsQueryParams? = null,
    overrides: RequestOverrides? = null,
  ): ListResponse<NylasListItem> {
    val path = String.format("v3/lists/%s/items", listId)
    val responseType = Types.newParameterizedType(ListResponse::class.java, NylasListItem::class.java)
    return client.executeGet(path, responseType, queryParams, overrides)
  }

  /**
   * Add items to a Nylas list. Duplicate additions are silently ignored. Max 1000 items per request.
   * @param listId The ID of the list
   * @param requestBody The items to add
   * @param overrides Optional request overrides to apply
   * @return The updated Nylas list
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun addItems(
    listId: String,
    requestBody: ListItemsRequest,
    overrides: RequestOverrides? = null,
  ): Response<NylasList> {
    val path = String.format("v3/lists/%s/items", listId)
    val serializedRequestBody = JsonHelper.moshi().adapter(ListItemsRequest::class.java).toJson(requestBody)
    val responseType = Types.newParameterizedType(Response::class.java, NylasList::class.java)
    return client.executePost(path, responseType, serializedRequestBody, overrides = overrides)
  }

  /**
   * Remove items from a Nylas list. Values not in the list are silently ignored. Max 1000 items per request.
   * @param listId The ID of the list
   * @param requestBody The items to remove
   * @param overrides Optional request overrides to apply
   * @return The updated Nylas list
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun removeItems(
    listId: String,
    requestBody: ListItemsRequest,
    overrides: RequestOverrides? = null,
  ): Response<NylasList> {
    val path = String.format("v3/lists/%s/items", listId)
    val serializedRequestBody = JsonHelper.moshi().adapter(ListItemsRequest::class.java).toJson(requestBody)
    val responseType = Types.newParameterizedType(Response::class.java, NylasList::class.java)
    return client.executeDelete(path, responseType, serializedRequestBody, overrides = overrides)
  }
}
