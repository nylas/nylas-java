package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.JsonHelper
import com.squareup.moshi.Types
import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okio.Buffer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.lang.reflect.Type
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CredentialsTests {
  private val mockHttpClient: OkHttpClient = Mockito.mock(OkHttpClient::class.java)
  private val mockCall: Call = Mockito.mock(Call::class.java)
  private val mockResponse: okhttp3.Response = Mockito.mock(okhttp3.Response::class.java)
  private val mockResponseBody: ResponseBody = Mockito.mock(ResponseBody::class.java)
  private val mockOkHttpClientBuilder: OkHttpClient.Builder = Mockito.mock()

  @BeforeEach
  fun setup() {
    MockitoAnnotations.openMocks(this)
    whenever(mockOkHttpClientBuilder.addInterceptor(any<Interceptor>())).thenReturn(mockOkHttpClientBuilder)
    whenever(mockOkHttpClientBuilder.build()).thenReturn(mockHttpClient)
    whenever(mockHttpClient.newCall(any())).thenReturn(mockCall)
    whenever(mockCall.execute()).thenReturn(mockResponse)
    whenever(mockResponse.isSuccessful).thenReturn(true)
    whenever(mockResponse.body).thenReturn(mockResponseBody)
  }

  @Nested
  inner class SerializationTests {
    @Test
    fun `Credential serializes properly`() {
      val adapter = JsonHelper.moshi().adapter(Credential::class.java)
      val jsonBuffer = Buffer().writeUtf8(
        """
          {
            "id": "e19f8e1a-eb1c-41c0-b6a6-d2e59daf7f47",
            "name": "My first Google credential",
            "created_at": 1617817109,
            "updated_at": 1617817109
          }
        """.trimIndent(),
      )

      val credential = adapter.fromJson(jsonBuffer)!!
      assertEquals("e19f8e1a-eb1c-41c0-b6a6-d2e59daf7f47", credential.id)
      assertEquals("My first Google credential", credential.name)
      assertEquals(1617817109L, credential.createdAt)
      assertEquals(1617817109L, credential.updatedAt)
    }

    @Test
    fun `Credential with connector type serializes properly`() {
      val adapter = JsonHelper.moshi().adapter(Credential::class.java)
      val jsonBuffer = Buffer().writeUtf8(
        """
          {
            "id": "e19f8e1a-eb1c-41c0-b6a6-d2e59daf7f47",
            "name": "My GCP credential",
            "credential_type": "connector",
            "created_at": 1617817109,
            "updated_at": 1617817109
          }
        """.trimIndent(),
      )

      val credential = adapter.fromJson(jsonBuffer)!!
      assertEquals("e19f8e1a-eb1c-41c0-b6a6-d2e59daf7f47", credential.id)
      assertEquals("My GCP credential", credential.name)
      assertEquals(CredentialType.CONNECTOR, credential.credentialType)
      assertEquals(1617817109L, credential.createdAt)
      assertEquals(1617817109L, credential.updatedAt)
    }

    @Test
    fun `CreateCredentialRequest Connector with OAuth credentials serializes properly`() {
      val adapter = JsonHelper.moshi().adapter(CreateCredentialRequest::class.java)
      val request = CreateCredentialRequest.Connector(
        name = "my GCP credential Google connector",
        credentialData = CredentialData.ConnectorOverride(
          clientId = "906653528181-hiu1u4q78kk1ag529robsq2s4un3kndo.apps.googleusercontent.com",
          clientSecret = "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",
        ),
      )

      val json = adapter.toJson(request)
      val jsonMap = JsonHelper.jsonToMap(json)

      assertEquals("my GCP credential Google connector", jsonMap["name"])
      assertEquals("connector", jsonMap["credential_type"])
      @Suppress("UNCHECKED_CAST")
      val credentialData = jsonMap["credential_data"] as Map<String, Any>
      assertEquals("906653528181-hiu1u4q78kk1ag529robsq2s4un3kndo.apps.googleusercontent.com", credentialData["client_id"])
      assertEquals("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", credentialData["client_secret"])
    }

    @Test
    fun `CredentialData ConnectorOverride with OAuth credentials serializes properly`() {
      val adapter = JsonHelper.moshi().adapter(CredentialData.ConnectorOverride::class.java)
      val credentialData = CredentialData.ConnectorOverride(
        clientId = "test-client-id",
        clientSecret = "test-client-secret",
      )

      val json = adapter.toJson(credentialData)
      val jsonMap = JsonHelper.jsonToMap(json)

      assertEquals("test-client-id", jsonMap["client_id"])
      assertEquals("test-client-secret", jsonMap["client_secret"])
    }

    @Test
    fun `CredentialData ConnectorOverride with extraProperties serializes properly`() {
      val adapter = JsonHelper.moshi().adapter(CredentialData.ConnectorOverride::class.java)
      val credentialData = CredentialData.ConnectorOverride(
        clientId = "test-client-id",
        clientSecret = "test-client-secret",
        extraProperties = mapOf("tenant" to "my-tenant-id"),
      )

      val json = adapter.toJson(credentialData)
      val jsonMap = JsonHelper.jsonToMap(json)

      assertEquals("test-client-id", jsonMap["client_id"])
      assertEquals("test-client-secret", jsonMap["client_secret"])
      assertEquals("my-tenant-id", jsonMap["tenant"])
    }

    @Test
    fun `CredentialData ConnectorOverride deserializes properly`() {
      val adapter = JsonHelper.moshi().adapter(CredentialData.ConnectorOverride::class.java)
      val jsonBuffer = Buffer().writeUtf8(
        """
          {
            "client_id": "test-client-id",
            "client_secret": "test-client-secret",
            "tenant": "my-tenant-id"
          }
        """.trimIndent(),
      )

      val credentialData = adapter.fromJson(jsonBuffer)!!
      assertEquals("test-client-id", credentialData.clientId)
      assertEquals("test-client-secret", credentialData.clientSecret)
      assertEquals("my-tenant-id", credentialData.extraProperties?.get("tenant"))
    }

    @Test
    fun `CredentialData ConnectorOverride with only extraProperties serializes properly`() {
      val adapter = JsonHelper.moshi().adapter(CredentialData.ConnectorOverride::class.java)
      val credentialData = CredentialData.ConnectorOverride(
        extraProperties = mapOf("custom_key" to "custom_value"),
      )

      val json = adapter.toJson(credentialData)
      val jsonMap = JsonHelper.jsonToMap(json)

      assertNull(jsonMap["client_id"])
      assertNull(jsonMap["client_secret"])
      assertEquals("custom_value", jsonMap["custom_key"])
    }
  }

  @Nested
  inner class CrudTests {
    private lateinit var mockNylasClient: NylasClient
    private lateinit var credentials: Credentials

    @BeforeEach
    fun setup() {
      mockNylasClient = Mockito.mock(NylasClient::class.java)
      credentials = Credentials(mockNylasClient)
    }

    @Test
    fun `listing credentials calls requests with the correct params`() {
      val queryParams = ListCredentialsQueryParams(
        limit = 10,
        offset = 10,
      )

      credentials.list(AuthProvider.GOOGLE, queryParams)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<ListResponse<Credential>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/connectors/google/creds", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(ListResponse::class.java, Credential::class.java), typeCaptor.firstValue)
      assertEquals(queryParams, queryParamCaptor.firstValue)
    }

    @Test
    fun `listing credentials without query params requests with the correct params`() {
      credentials.list(AuthProvider.GOOGLE)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<ListResponse<Credential>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/connectors/google/creds", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(ListResponse::class.java, Credential::class.java), typeCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }

    @Test
    fun `finding a credential calls requests with the correct params`() {
      val credentialId = "abc-123"

      credentials.find(AuthProvider.GOOGLE, credentialId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<Response<Credential>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/connectors/google/creds/abc-123", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Credential::class.java), typeCaptor.firstValue)
    }

    @Test
    fun `creating a credential calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(CreateCredentialRequest::class.java)
      val createCredentialRequest = CreateCredentialRequest.Google(
        name = "My first Google credential",
        credentialData = CredentialData.GoogleServiceAccount(
          privateKeyId = "abc",
          privateKey = "123",
          clientEmail = "abc@gmail.com",
        ),
      )

      credentials.create(AuthProvider.GOOGLE, createCredentialRequest)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePost<Response<Credential>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/connectors/google/creds", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Credential::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(createCredentialRequest), requestBodyCaptor.firstValue)
    }

    @Test
    fun `creating a connector credential with OAuth credentials calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(CreateCredentialRequest::class.java)
      val createCredentialRequest = CreateCredentialRequest.Connector(
        name = "my GCP credential Google connector",
        credentialData = CredentialData.ConnectorOverride(
          clientId = "906653528181-hiu1u4q78kk1ag529robsq2s4un3kndo.apps.googleusercontent.com",
          clientSecret = "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",
        ),
      )

      credentials.create(AuthProvider.GOOGLE, createCredentialRequest)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePost<Response<Credential>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/connectors/google/creds", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Credential::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(createCredentialRequest), requestBodyCaptor.firstValue)
    }

    @Test
    fun `updating a credential calls requests with the correct params`() {
      val credentialId = "abc-123"
      val adapter = JsonHelper.moshi().adapter(UpdateCredentialRequest::class.java)
      val updateCredentialRequest = UpdateCredentialRequest(
        name = "My first Google credential",
        credentialData = CredentialData.GoogleServiceAccount(
          privateKeyId = "abc",
          privateKey = "123",
          clientEmail = "",
        ),
      )

      credentials.update(AuthProvider.GOOGLE, credentialId, updateCredentialRequest)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePatch<Response<Credential>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/connectors/google/creds/abc-123", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Credential::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(updateCredentialRequest), requestBodyCaptor.firstValue)
    }

    @Test
    fun `destroying a credential calls requests with the correct params`() {
      val credentialId = "abc-123"

      credentials.destroy(AuthProvider.IMAP, credentialId)
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

      assertEquals("v3/connectors/imap/creds/abc-123", pathCaptor.firstValue)
      assertEquals(DeleteResponse::class.java, typeCaptor.firstValue)
    }

    @Test
    fun `updating a credential with ConnectorOverride data calls requests with the correct params`() {
      val credentialId = "abc-123"
      val updateCredentialRequest = UpdateCredentialRequest(
        name = "Updated connector credential",
        credentialData = CredentialData.ConnectorOverride(
          clientId = "new-client-id",
          clientSecret = "new-client-secret",
          extraProperties = mapOf("tenant" to "my-tenant"),
        ),
      )

      credentials.update(AuthProvider.GOOGLE, credentialId, updateCredentialRequest)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePatch<Response<Credential>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/connectors/google/creds/abc-123", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Credential::class.java), typeCaptor.firstValue)
      val json = requestBodyCaptor.firstValue
      val jsonMap = JsonHelper.jsonToMap(json)
      assertEquals("Updated connector credential", jsonMap["name"])
      @Suppress("UNCHECKED_CAST")
      val credentialData = jsonMap["credential_data"] as Map<String, Any>
      assertEquals("new-client-id", credentialData["client_id"])
      assertEquals("new-client-secret", credentialData["client_secret"])
      assertEquals("my-tenant", credentialData["tenant"])
    }
  }
}
