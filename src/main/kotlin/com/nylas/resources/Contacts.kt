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
   * @return The list of contacts
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun list(identifier: String, queryParams: ListContactsQueryParams? = null): ListResponse<Contact> {
    val path = String.format("v3/grants/%s/contacts", identifier)
    return listResource(path, queryParams)
  }

  /**
   * Return a contact
   * @param identifier The identifier of the grant to act upon
   * @param contactId The id of the contact to retrieve.
   * @param queryParams The query parameters to include in the request
   * @return The contact
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun find(identifier: String, contactId: String, queryParams: FindContactQueryParams? = null): Response<Contact> {
    val path = String.format("v3/grants/%s/contacts/%s", identifier, contactId)
    return findResource(path, queryParams)
  }

  /**
   * Create a Contact
   * @param identifier Grant ID or email account in which to create the object
   * @param requestBody The values to create the event with
   * @return The created contact
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun create(identifier: String, requestBody: CreateContactRequest): Response<Contact> {
    val path = String.format("v3/grants/%s/contacts", identifier)
    val adapter = JsonHelper.moshi().adapter(CreateContactRequest::class.java)
    val serializedRequestBody = adapter.toJson(requestBody)
    return createResource(path, serializedRequestBody)
  }

  /**
   * Update a Contact
   * @param identifier The identifier of the grant to act upon
   * @param contactId The id of the contact to update.
   * @param requestBody The values to update the Contact with
   * @return The updated contact
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun update(identifier: String, contactId: String, requestBody: UpdateContactRequest): Response<Contact> {
    val path = String.format("v3/grants/%s/contacts/%s", identifier, contactId)
    val adapter = JsonHelper.moshi().adapter(UpdateContactRequest::class.java)
    val serializedRequestBody = adapter.toJson(requestBody)
    return updateResource(path, serializedRequestBody)
  }

  /**
   * Delete a Contact
   * @param identifier The identifier of the grant to act upon
   * @param contactId The id of the Contact to delete.
   * @return The deletion response
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  fun destroy(identifier: String, contactId: String): DeleteResponse {
    val path = String.format("v3/grants/%s/contacts/%s", identifier, contactId)
    return destroyResource(path)
  }

  /**
   * Return all contact groups
   * @param identifier The identifier of the grant to act upon
   * @param queryParams The query parameters to include in the request
   * @return The scheduled message
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun listGroups(identifier: String, queryParams: ListContactGroupsQueryParams? = null): ListResponse<ContactGroup> {
    val path = String.format("v3/grants/%s/contacts/groups", identifier)
    val responseType = Types.newParameterizedType(ListResponse::class.java, ContactGroup::class.java)
    return client.executeGet(path, responseType, queryParams)
  }
}
