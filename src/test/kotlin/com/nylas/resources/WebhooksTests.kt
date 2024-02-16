package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.JsonHelper
import com.squareup.moshi.Types
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okio.Buffer
import org.junit.jupiter.api.Assertions.assertNull
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
import kotlin.test.assertIs

class WebhooksTests {
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
    fun `Webhook serializes properly`() {
      val adapter = JsonHelper.moshi().adapter(Webhook::class.java)
      val jsonBuffer = Buffer().writeUtf8(
        """
          {
            "id": "UMWjAjMeWQ4D8gYF2moonK4486",
            "description": "Production webhook destination",
            "trigger_types": [
              "calendar.created"
            ],
            "webhook_url": "https://example.com/webhooks",
            "status": "active",
            "notification_email_addresses": [
              "jane@example.com",
              "joe@example.com"
            ],
            "status_updated_at": 1234567890,
            "created_at": 1234567890,
            "updated_at": 1234567890
          }
        """.trimIndent(),
      )

      val webhook = adapter.fromJson(jsonBuffer)!!
      assertEquals("UMWjAjMeWQ4D8gYF2moonK4486", webhook.id)
      assertEquals("Production webhook destination", webhook.description)
      assertEquals(listOf(WebhookTriggers.CALENDAR_CREATED), webhook.triggerTypes)
      assertEquals("https://example.com/webhooks", webhook.webhookUrl)
      assertEquals(WebhookStatus.ACTIVE, webhook.status)
      assertEquals(listOf("jane@example.com", "joe@example.com"), webhook.notificationEmailAddresses)
      assertEquals(1234567890, webhook.statusUpdatedAt)
      assertEquals(1234567890, webhook.createdAt)
      assertEquals(1234567890, webhook.updatedAt)
    }
  }

  @Nested
  inner class CrudTests {
    private lateinit var mockNylasClient: NylasClient
    private lateinit var webhooks: Webhooks

    @BeforeEach
    fun setup() {
      mockNylasClient = Mockito.mock(NylasClient::class.java)
      webhooks = Webhooks(mockNylasClient)
    }

    @Test
    fun `listing webhooks calls requests with the correct params`() {
      webhooks.list()

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      verify(mockNylasClient).executeGet<ListResponse<Webhook>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
      )

      assertEquals("v3/webhooks", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(ListResponse::class.java, Webhook::class.java), typeCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }

    @Test
    fun `finding a webhook calls requests with the correct params`() {
      val webhookId = "WEBHOOK_ID"
      webhooks.find(webhookId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      verify(mockNylasClient).executeGet<Response<Webhook>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
      )

      assertEquals("v3/webhooks/$webhookId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Webhook::class.java), typeCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }

    @Test
    fun `creating a webhook calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(CreateWebhookRequest::class.java)
      val createWebhookRequest = CreateWebhookRequest(
        description = "Production webhook destination",
        triggerTypes = listOf(WebhookTriggers.CALENDAR_CREATED),
        webhookUrl = "https://example.com/webhooks",
        notificationEmailAddresses = listOf("jane@example.com"),
      )

      webhooks.create(createWebhookRequest)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      verify(mockNylasClient).executePost<Response<Webhook>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
      )

      assertEquals("v3/webhooks", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, WebhookWithSecret::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(createWebhookRequest), requestBodyCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }

    @Test
    fun `updating a webhook calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(UpdateWebhookRequest::class.java)
      val updateWebhookRequest = UpdateWebhookRequest(
        description = "Production webhook destination",
        triggerTypes = listOf(WebhookTriggers.CALENDAR_CREATED),
        webhookUrl = "https://example.com/webhooks",
        notificationEmailAddresses = listOf("jane@example.com"),
      )
      val webhookId = "WEBHOOK_ID"

      webhooks.update(webhookId, updateWebhookRequest)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      verify(mockNylasClient).executePut<Response<Webhook>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
      )

      assertEquals("v3/webhooks/$webhookId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Webhook::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(updateWebhookRequest), requestBodyCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }

    @Test
    fun `destroying a webhook calls requests with the correct params`() {
      val webhookId = "WEBHOOK_ID"

      webhooks.destroy(webhookId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      verify(mockNylasClient).executeDelete<DeleteResponse>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
      )

      assertEquals("v3/webhooks/$webhookId", pathCaptor.firstValue)
      assertEquals(WebhookDeleteResponse::class.java, typeCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }
  }

  @Nested
  inner class OtherMethodTests {
    private lateinit var mockNylasClient: NylasClient
    private lateinit var webhooks: Webhooks

    @BeforeEach
    fun setup() {
      mockNylasClient = Mockito.mock(NylasClient::class.java)
      webhooks = Webhooks(mockNylasClient)
    }

    @Test
    fun `rotating secret calls requests with the correct params`() {
      val webhookId = "webhook-123"

      webhooks.rotateSecret(webhookId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      verify(mockNylasClient).executePost<Response<Webhook>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
      )

      assertEquals("v3/webhooks/$webhookId/rotate-secret", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, WebhookWithSecret::class.java), typeCaptor.firstValue)
      assertNull(requestBodyCaptor.firstValue)
    }

    @Test
    fun `getting ip addresses calls requests with the correct params`() {
      webhooks.ipAddresses()

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      verify(mockNylasClient).executeGet<ListResponse<Webhook>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
      )

      assertEquals("v3/webhooks/ip-addresses", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, WebhookIpAddressesResponse::class.java), typeCaptor.firstValue)
    }

    @Test
    fun `extracting challenge parameter from url succeeds if present`() {
      val url = "https://example.com?challenge=12345"
      val challenge = webhooks.extractChallengeParameter(url)
      assertEquals("12345", challenge)
    }

    @Test
    fun `extracting challenge parameter from url throws an error if not present`() {
      val url = "https://example.com?foo=bar"
      try {
        webhooks.extractChallengeParameter(url)
      } catch (e: IllegalArgumentException) {
        assertIs<IllegalArgumentException>(e)
      }
    }
  }
}
