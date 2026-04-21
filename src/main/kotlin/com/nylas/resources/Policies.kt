package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.JsonHelper

/**
 * Nylas Policies API
 *
 * The Nylas Policies API allows you to create and manage policies for Nylas Agent Accounts.
 * Policies define message limits, spam detection, retention, and linked rules.
 *
 * @param client The configured Nylas API client
 */
class Policies(client: NylasClient) : Resource<Policy>(client, Policy::class.java) {
  /**
   * Return all policies for your application.
   * @param queryParams Optional query parameters to apply
   * @param overrides Optional request overrides to apply
   * @return The list of policies
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun list(queryParams: ListPoliciesQueryParams? = null, overrides: RequestOverrides? = null): ListResponse<Policy> {
    return listResource("v3/policies", queryParams, overrides)
  }

  /**
   * Return a policy.
   * @param policyId The ID of the policy to retrieve
   * @param overrides Optional request overrides to apply
   * @return The policy
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun find(policyId: String, overrides: RequestOverrides? = null): Response<Policy> {
    val path = String.format("v3/policies/%s", policyId)
    return findResource(path, overrides = overrides)
  }

  /**
   * Create a policy.
   * @param requestBody The values to create the policy with
   * @param overrides Optional request overrides to apply
   * @return The created policy
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun create(requestBody: CreatePolicyRequest, overrides: RequestOverrides? = null): Response<Policy> {
    val serializedRequestBody = JsonHelper.moshi().adapter(CreatePolicyRequest::class.java).toJson(requestBody)
    return createResource("v3/policies", serializedRequestBody, overrides = overrides)
  }

  /**
   * Update a policy.
   * @param policyId The ID of the policy to update
   * @param requestBody The values to update the policy with
   * @param overrides Optional request overrides to apply
   * @return The updated policy
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun update(policyId: String, requestBody: UpdatePolicyRequest, overrides: RequestOverrides? = null): Response<Policy> {
    val path = String.format("v3/policies/%s", policyId)
    val serializedRequestBody = JsonHelper.moshi().adapter(UpdatePolicyRequest::class.java).toJson(requestBody)
    return updateResource(path, serializedRequestBody, overrides = overrides)
  }

  /**
   * Delete a policy.
   * @param policyId The ID of the policy to delete
   * @param overrides Optional request overrides to apply
   * @return The deletion response
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun destroy(policyId: String, overrides: RequestOverrides? = null): DeleteResponse {
    val path = String.format("v3/policies/%s", policyId)
    return destroyResource(path, overrides = overrides)
  }
}
