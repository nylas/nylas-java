package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.JsonHelper
import com.nylas.util.PathEncoder
import com.squareup.moshi.Types

/**
 * Nylas Workspaces API
 *
 * Workspaces group grants in a Nylas application by email domain. Grants can be
 * auto-grouped by matching email domain or manually assigned and removed.
 *
 * @param client The configured Nylas API client
 */
class Workspaces(client: NylasClient) : Resource<Workspace>(client, Workspace::class.java) {
  /**
   * Return all workspaces for your application.
   * @param overrides Optional request overrides to apply
   * @return The list of workspaces
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun list(overrides: RequestOverrides? = null): ListResponse<Workspace> {
    return listResource("v3/workspaces", overrides = overrides)
  }

  /**
   * Return a workspace.
   * @param workspaceId The ID or domain address of the workspace to retrieve
   * @param overrides Optional request overrides to apply
   * @return The workspace
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun find(workspaceId: String, overrides: RequestOverrides? = null): Response<Workspace> {
    val path = String.format("v3/workspaces/%s", PathEncoder.encode(workspaceId))
    return findResource(path, overrides = overrides)
  }

  /**
   * Create a workspace.
   * @param requestBody The values to create the workspace with
   * @param overrides Optional request overrides to apply
   * @return The created workspace
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun create(requestBody: CreateWorkspaceRequest, overrides: RequestOverrides? = null): Response<Workspace> {
    val serializedRequestBody = JsonHelper.moshi().adapter(CreateWorkspaceRequest::class.java).toJson(requestBody)
    return createResource("v3/workspaces", serializedRequestBody, overrides = overrides)
  }

  /**
   * Update a workspace.
   * @param workspaceId The workspace UUID to update
   * @param requestBody The values to update the workspace with
   * @param overrides Optional request overrides to apply
   * @return The updated workspace
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun update(workspaceId: String, requestBody: UpdateWorkspaceRequest, overrides: RequestOverrides? = null): Response<Workspace> {
    val path = String.format("v3/workspaces/%s", PathEncoder.encode(workspaceId))
    val serializedRequestBody = JsonHelper.moshi().adapter(UpdateWorkspaceRequest::class.java).toJson(requestBody)
    return patchResource(path, serializedRequestBody, overrides = overrides)
  }

  /**
   * Delete a workspace.
   * @param workspaceId The ID or domain address of the workspace to delete
   * @param overrides Optional request overrides to apply
   * @return The deletion response
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun destroy(workspaceId: String, overrides: RequestOverrides? = null): DeleteResponse {
    val path = String.format("v3/workspaces/%s", PathEncoder.encode(workspaceId))
    return destroyResource(path, overrides = overrides)
  }

  /**
   * Start a background job that auto-groups grants into workspaces by email domain.
   * @param requestBody Optional filters to scope which grants are grouped
   * @param overrides Optional request overrides to apply
   * @return The auto-group job response
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun autoGroup(
    requestBody: WorkspaceAutoGroupRequest? = null,
    overrides: RequestOverrides? = null,
  ): Response<WorkspaceAutoGroupResponse> {
    val responseType = Types.newParameterizedType(Response::class.java, WorkspaceAutoGroupResponse::class.java)
    val serializedRequestBody = requestBody?.let {
      JsonHelper.moshi().adapter(WorkspaceAutoGroupRequest::class.java).toJson(it)
    }
    return client.executePost("v3/workspaces/auto-group", responseType, serializedRequestBody, overrides = overrides)
  }

  /**
   * Manually assign grants to or remove grants from a workspace.
   * @param workspaceId The ID or domain address of the workspace
   * @param requestBody The grants to assign and/or remove
   * @param overrides Optional request overrides to apply
   * @return The assignment response
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun manualAssign(
    workspaceId: String,
    requestBody: WorkspaceManualAssignRequest,
    overrides: RequestOverrides? = null,
  ): Response<WorkspaceManualAssignResponse> {
    val path = String.format("v3/workspaces/%s/manual-assign", PathEncoder.encode(workspaceId))
    val responseType = Types.newParameterizedType(Response::class.java, WorkspaceManualAssignResponse::class.java)
    val serializedRequestBody = JsonHelper.moshi().adapter(WorkspaceManualAssignRequest::class.java).toJson(requestBody)
    return client.executePost(path, responseType, serializedRequestBody, overrides = overrides)
  }
}
