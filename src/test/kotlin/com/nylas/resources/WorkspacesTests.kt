package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.models.Response
import com.nylas.util.JsonHelper
import com.squareup.moshi.Types
import okio.Buffer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.mockito.Mockito
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import java.lang.reflect.Type
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertNull

class WorkspacesTests {
  @Nested
  inner class SerializationTests {
    @Test
    fun `Workspace deserializes properly`() {
      val adapter = JsonHelper.moshi().adapter(Workspace::class.java)
      val jsonBuffer = Buffer().writeUtf8(
        """
          {
            "workspace_id": "ws-123",
            "application_id": "app-123",
            "name": "Acme",
            "domain": "acme.com",
            "auto_group": true,
            "default": true,
            "policy_id": "policy-123",
            "rule_ids": ["rule-1"],
            "created_at": 1742933005,
            "updated_at": 1742933006
          }
        """.trimIndent(),
      )

      val workspace = adapter.fromJson(jsonBuffer)!!

      assertIs<Workspace>(workspace)
      assertEquals("ws-123", workspace.workspaceId)
      assertEquals("acme.com", workspace.domain)
      assertEquals(true, workspace.autoGroup)
      assertEquals(true, workspace.default)
      assertEquals(listOf("rule-1"), workspace.ruleIds)
    }

    @Test
    fun `WorkspaceManualAssignResponse allows null grant arrays`() {
      val adapter = JsonHelper.moshi().adapter(WorkspaceManualAssignResponse::class.java)
      val jsonBuffer = Buffer().writeUtf8(
        """
          {
            "application_id": "app-123",
            "workspace_id": "ws-123",
            "domain": "acme.com",
            "grants_assigned": null,
            "grants_removed": null
          }
        """.trimIndent(),
      )

      val response = adapter.fromJson(jsonBuffer)!!

      assertNull(response.grantsAssigned)
      assertNull(response.grantsRemoved)
    }

    @Test
    fun `CreateWorkspaceRequest requires domain only when auto-group is true`() {
      assertFailsWith<IllegalArgumentException> {
        CreateWorkspaceRequest.Builder("Acme").autoGroup(true).build()
      }

      val explicitNoAutoGroup = CreateWorkspaceRequest.Builder("Acme").autoGroup(false).build()
      assertNull(explicitNoAutoGroup.domain)
      assertEquals(false, explicitNoAutoGroup.autoGroup)

      val defaultAutoGroup = CreateWorkspaceRequest.Builder("Acme").build()
      assertNull(defaultAutoGroup.domain)
      assertNull(defaultAutoGroup.autoGroup)
    }

    @Test
    fun `UpdateWorkspaceRequest only serializes documented update fields`() {
      val adapter = JsonHelper.moshi().adapter(UpdateWorkspaceRequest::class.java)
      val request = UpdateWorkspaceRequest.Builder()
        .name("Renamed")
        .autoGroup(false)
        .policyId("policy-123")
        .ruleIds(listOf("rule-1"))
        .build()

      val json = adapter.toJson(request)

      assert(json.contains("\"name\":\"Renamed\"")) { "Expected name in JSON, got: $json" }
      assert(json.contains("\"auto_group\":false")) { "Expected auto_group in JSON, got: $json" }
      assert(json.contains("\"policy_id\":\"policy-123\"")) { "Expected policy_id in JSON, got: $json" }
      assert(json.contains("\"rule_ids\":[\"rule-1\"]")) { "Expected rule_ids in JSON, got: $json" }
      assert(!json.contains("\"domain\"")) { "Expected domain to stay out of update serialization, got: $json" }
    }

    @Test
    fun `UpdateWorkspaceRequest omits policy id when unchanged`() {
      val adapter = JsonHelper.moshi().adapter(UpdateWorkspaceRequest::class.java)
      val request = UpdateWorkspaceRequest.Builder()
        .name("Renamed")
        .build()

      val json = adapter.toJson(request)

      assert(!json.contains("\"policy_id\"")) { "Expected policy_id to be omitted, got: $json" }
    }

    @Test
    fun `UpdateWorkspaceRequest serializes explicit null policy detach`() {
      val adapter = JsonHelper.moshi().adapter(UpdateWorkspaceRequest::class.java)
      val request = UpdateWorkspaceRequest.Builder()
        .policyId(null)
        .build()

      val json = adapter.toJson(request)

      assert(json.contains("\"policy_id\":null")) { "Expected policy_id:null in JSON, got: $json" }
    }

    @Test
    fun `UpdateWorkspaceRequest clearPolicyId serializes explicit null policy detach`() {
      val adapter = JsonHelper.moshi().adapter(UpdateWorkspaceRequest::class.java)
      val request = UpdateWorkspaceRequest.Builder()
        .clearPolicyId()
        .build()

      val json = adapter.toJson(request)

      assert(json.contains("\"policy_id\":null")) { "Expected policy_id:null in JSON, got: $json" }
    }
  }

  @Nested
  inner class CrudTests {
    private lateinit var mockNylasClient: NylasClient
    private lateinit var workspaces: Workspaces

    @BeforeEach
    fun setup() {
      mockNylasClient = Mockito.mock(NylasClient::class.java)
      workspaces = Workspaces(mockNylasClient)
    }

    @Test
    fun `listing workspaces calls requests with the correct params`() {
      workspaces.list()

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<ListResponse<Workspace>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/workspaces", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(ListResponse::class.java, Workspace::class.java), typeCaptor.firstValue)
    }

    @Test
    fun `finding a workspace calls requests with the correct params`() {
      workspaces.find("domain/with/slash")

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<Response<Workspace>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/workspaces/domain%2Fwith%2Fslash", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Workspace::class.java), typeCaptor.firstValue)
    }

    @Test
    fun `creating a workspace calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(CreateWorkspaceRequest::class.java)
      val requestBody = CreateWorkspaceRequest.Builder("Acme").domain("acme.com").autoGroup(true).build()
      workspaces.create(requestBody)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePost<Response<Workspace>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/workspaces", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Workspace::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(requestBody), requestBodyCaptor.firstValue)
    }

    @Test
    fun `updating a workspace uses patch with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(UpdateWorkspaceRequest::class.java)
      val requestBody = UpdateWorkspaceRequest.Builder().name("Renamed").build()
      workspaces.update("ws-123", requestBody)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePatch<Response<Workspace>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/workspaces/ws-123", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Workspace::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(requestBody), requestBodyCaptor.firstValue)
    }

    @Test
    fun `destroying a workspace calls requests with the correct params`() {
      workspaces.destroy("ws-123")

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeDelete<DeleteResponse>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/workspaces/ws-123", pathCaptor.firstValue)
      assertEquals(DeleteResponse::class.java, typeCaptor.firstValue)
    }

    @Test
    fun `auto-grouping workspaces calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(WorkspaceAutoGroupRequest::class.java)
      val requestBody = WorkspaceAutoGroupRequest(afterCreatedAt = 1742933005, specificDomain = "acme.com")
      workspaces.autoGroup(requestBody)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePost<Response<WorkspaceAutoGroupResponse>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/workspaces/auto-group", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, WorkspaceAutoGroupResponse::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(requestBody), requestBodyCaptor.firstValue)
    }

    @Test
    fun `manually assigning a workspace calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(WorkspaceManualAssignRequest::class.java)
      val requestBody = WorkspaceManualAssignRequest(assignGrants = listOf("grant-1"), removeGrants = listOf("grant-2"))
      workspaces.manualAssign("ws-123", requestBody)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePost<Response<WorkspaceManualAssignResponse>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/workspaces/ws-123/manual-assign", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, WorkspaceManualAssignResponse::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(requestBody), requestBodyCaptor.firstValue)
    }
  }
}
