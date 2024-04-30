package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.JsonHelper
import com.squareup.moshi.Types

class Contacts(client: NylasClient) : Resource<Contact>(client, Contact::class.java) {
  /**
   * Return all contacts
   * @param identifier The identifier of the grant to act upon
   * @param queryParams The query parameters to include in the request
   * @param overrides Optional request overrides to apply
   * @return The list of contacts
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun list(identifier: String, queryParams: ListContactsQueryParams? = null, overrides: RequestOverrides? = null): ListResponse<Contact> {
    val path = String.format("v3/grants/%s/contacts", identifier)
    return listResource(path, queryParams, overrides)
  }

  /**
   * Return a contact
   * @param identifier The identifier of the grant to act upon
   * @param contactId The id of the contact to retrieve.
   * @param queryParams The query parameters to include in the request
   * @param overrides Optional request overrides to apply
   * @return The contact
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun find(identifier: String, contactId: String, queryParams: FindContactQueryParams? = null, overrides: RequestOverrides? = null): Response<Contact> {
    val path = String.format("v3/grants/%s/contacts/%s", identifier, contactId)
    return findResource(path, queryParams, overrides)
  }

  /**
   * Create a Contact
   * @param identifier Grant ID or email account in which to create the object
   * @param requestBody The values to create the event with
   * @param overrides Optional request overrides to apply
   * @return The created contact
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun create(identifier: String, requestBody: CreateContactRequest, overrides: RequestOverrides? = null): Response<Contact> {
    val path = String.format("v3/grants/%s/contacts", identifier)
    val adapter = JsonHelper.moshi().adapter(CreateContactRequest::class.java)
    val serializedRequestBody = adapter.toJson(requestBody)
    return createResource(path, serializedRequestBody, overrides = overrides)
  }

  /**
   * Update a Contact
   * @param identifier The identifier of the grant to act upon
   * @param contactId The id of the contact to update.
   * @param requestBody The values to update the Contact with
   * @param overrides Optional request overrides to apply
   * @return The updated contact
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun update(identifier: String, contactId: String, requestBody: UpdateContactRequest, overrides: RequestOverrides? = null): Response<Contact> {
    val path = String.format("v3/grants/%s/contacts/%s", identifier, contactId)
    val adapter = JsonHelper.moshi().adapter(UpdateContactRequest::class.java)
    val serializedRequestBody = adapter.toJson(requestBody)
    return updateResource(path, serializedRequestBody, overrides = overrides)
  }

  /**
   * Delete a Contact
   * @param identifier The identifier of the grant to act upon
   * @param contactId The id of the Contact to delete.
   * @param overrides Optional request overrides to apply
   * @return The deletion response
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun destroy(identifier: String, contactId: String, overrides: RequestOverrides? = null): DeleteResponse {
    val path = String.format("v3/grants/%s/contacts/%s", identifier, contactId)
    return destroyResource(path, overrides = overrides)
  }

  /**
   * Return all contact groups
   * @param identifier The identifier of the grant to act upon
   * @param queryParams The query parameters to include in the request
   * @param overrides Optional request overrides to apply
   * @return The scheduled message
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun listGroups(identifier: String, queryParams: ListContactGroupsQueryParams? = null, overrides: RequestOverrides? = null): ListResponse<ContactGroup> {
    val path = String.format("v3/grants/%s/contacts/groups", identifier)
    val responseType = Types.newParameterizedType(ListResponse::class.java, ContactGroup::class.java)
    return client.executeGet(path, responseType, queryParams, overrides)
  }
}
