package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.JsonHelper
import com.squareup.moshi.Types
import okhttp3.Call
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

class CredentialsTests {
  private val mockHttpClient: OkHttpClient = Mockito.mock(OkHttpClient::class.java)
  private val mockCall: Call = Mockito.mock(Call::class.java)
  private val mockResponse: okhttp3.Response = Mockito.mock(okhttp3.Response::class.java)
  private val mockResponseBody: ResponseBody = Mockito.mock(ResponseBody::class.java)
  private val mockOkHttpClientBuilder: OkHttpClient.Builder = Mockito.mock()

  @BeforeEach
  fun setup() {
    MockitoAnnotations.openMocks(this)
    whenever(mockOkHttpClientBuilder.addInterceptor(any())).thenReturn(mockOkHttpClientBuilder)
    whenever(mockOkHttpClientBuilder.build()).thenReturn(mockHttpClient)
    whenever(mockHttpClient.newCall(any())).thenReturn(mockCall)
    whenever(mockCall.execute()).thenReturn(mockResponse)
    whenever(mockResponse.isSuccessful).thenReturn(true)
    whenever(mockResponse.body()).thenReturn(mockResponseBody)
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
      verify(mockNylasClient).executeGet<ListResponse<Credential>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
      )

      assertEquals("v3/connectors/google/creds", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(ListResponse::class.java, Credential::class.java), typeCaptor.firstValue)
    }

    @Test
    fun `finding a credential calls requests with the correct params`() {
      val credentialId = "abc-123"

      credentials.find(AuthProvider.GOOGLE, credentialId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      verify(mockNylasClient).executeGet<Response<Credential>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
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
      verify(mockNylasClient).executePost<Response<Credential>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
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
          clientEmail = ""
        )
      )

      credentials.update(AuthProvider.GOOGLE, credentialId, updateCredentialRequest)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      verify(mockNylasClient).executePatch<Response<Credential>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
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
      verify(mockNylasClient).executeDelete<DeleteResponse>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
      )

      assertEquals("v3/connectors/imap/creds/abc-123", pathCaptor.firstValue)
      assertEquals(DeleteResponse::class.java, typeCaptor.firstValue)
    }
  }
}
