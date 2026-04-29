package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.models.Response
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
import kotlin.test.assertIs
import kotlin.test.assertNull

class PoliciesTests {
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
    fun `Policy deserializes properly`() {
      val adapter = JsonHelper.moshi().adapter(Policy::class.java)
      val jsonBuffer = Buffer().writeUtf8(
        """
          {
            "id": "b1c2d3e4-5678-4abc-9def-0123456789ab",
            "name": "Standard Agent Account Policy",
            "application_id": "ad410018-d306-43f9-8361-fa5d7b2172e0",
            "organization_id": "org-abc123",
            "options": {
              "additional_folders": ["archive", "follow-up"],
              "use_cidr_aliasing": false
            },
            "limits": {
              "limit_attachment_size_limit": 26214400,
              "limit_attachment_count_limit": 50,
              "limit_attachment_allowed_types": ["image/png", "application/pdf"],
              "limit_size_total_mime": 31457280,
              "limit_storage_total": 10737418240,
              "limit_count_daily_message_per_grant": 1000,
              "limit_inbox_retention_period": 365,
              "limit_spam_retention_period": 30
            },
            "rules": ["c1d2e3f4-5678-4abc-9def-0123456789ab"],
            "spam_detection": {
              "use_list_dnsbl": true,
              "use_header_anomaly_detection": true,
              "spam_sensitivity": 1.5
            },
            "created_at": 1742932766,
            "updated_at": 1742932766
          }
        """.trimIndent(),
      )

      val policy = adapter.fromJson(jsonBuffer)!!
      assertIs<Policy>(policy)
      assertEquals("b1c2d3e4-5678-4abc-9def-0123456789ab", policy.id)
      assertEquals("Standard Agent Account Policy", policy.name)
      assertEquals("ad410018-d306-43f9-8361-fa5d7b2172e0", policy.applicationId)
      assertEquals("org-abc123", policy.organizationId)
      assertEquals(listOf("archive", "follow-up"), policy.options?.additionalFolders)
      assertEquals(false, policy.options?.useCidrAliasing)
      assertEquals(26214400L, policy.limits?.attachmentSizeLimit)
      assertEquals(50, policy.limits?.attachmentCountLimit)
      assertEquals(listOf("image/png", "application/pdf"), policy.limits?.attachmentAllowedTypes)
      assertEquals(1000, policy.limits?.countDailyMessagePerGrant)
      assertEquals(365, policy.limits?.inboxRetentionPeriod)
      assertEquals(30, policy.limits?.spamRetentionPeriod)
      assertEquals(listOf("c1d2e3f4-5678-4abc-9def-0123456789ab"), policy.rules)
      assertEquals(true, policy.spamDetection?.useListDnsbl)
      assertEquals(true, policy.spamDetection?.useHeaderAnomalyDetection)
      assertEquals(1.5, policy.spamDetection?.spamSensitivity)
      assertEquals(1742932766L, policy.createdAt)
      assertEquals(1742932766L, policy.updatedAt)
    }

    @Test
    fun `Policy with null optional fields deserializes properly`() {
      val adapter = JsonHelper.moshi().adapter(Policy::class.java)
      val jsonBuffer = Buffer().writeUtf8(
        """
          {
            "id": "b1c2d3e4-5678-4abc-9def-0123456789ab",
            "name": "Minimal Policy",
            "application_id": "ad410018-d306-43f9-8361-fa5d7b2172e0",
            "organization_id": "org-abc123",
            "created_at": 1742932766,
            "updated_at": 1742932766
          }
        """.trimIndent(),
      )

      val policy = adapter.fromJson(jsonBuffer)!!
      assertIs<Policy>(policy)
      assertNull(policy.options)
      assertNull(policy.limits)
      assertNull(policy.rules)
      assertNull(policy.spamDetection)
    }

    @Test
    fun `CreatePolicyRequest serializes with only required fields`() {
      val adapter = JsonHelper.moshi().adapter(CreatePolicyRequest::class.java)
      val request = CreatePolicyRequest(name = "My Policy")
      val json = adapter.toJson(request)
      val deserialized = adapter.fromJson(json)!!
      assertEquals("My Policy", deserialized.name)
      assertNull(deserialized.options)
      assertNull(deserialized.limits)
      assertNull(deserialized.rules)
      assertNull(deserialized.spamDetection)
    }

    @Test
    fun `CreatePolicyRequest serializes all fields`() {
      val adapter = JsonHelper.moshi().adapter(CreatePolicyRequest::class.java)
      val request = CreatePolicyRequest.Builder("Full Policy")
        .options(PolicyOptions(additionalFolders = listOf("archive"), useCidrAliasing = true))
        .limits(PolicyLimits(countDailyMessagePerGrant = 500))
        .rules(listOf("rule-id-1"))
        .spamDetection(PolicySpamDetection(spamSensitivity = 2.0))
        .build()

      val json = adapter.toJson(request)
      val deserialized = adapter.fromJson(json)!!
      assertEquals("Full Policy", deserialized.name)
      assertEquals(listOf("archive"), deserialized.options?.additionalFolders)
      assertEquals(500, deserialized.limits?.countDailyMessagePerGrant)
      assertEquals(listOf("rule-id-1"), deserialized.rules)
      assertEquals(2.0, deserialized.spamDetection?.spamSensitivity)
    }
  }

  @Nested
  inner class CrudTests {
    private lateinit var mockNylasClient: NylasClient
    private lateinit var policies: Policies

    @BeforeEach
    fun setup() {
      mockNylasClient = Mockito.mock(NylasClient::class.java)
      policies = Policies(mockNylasClient)
    }

    @Test
    fun `listing policies calls requests with the correct params`() {
      policies.list()

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<ListResponse<Policy>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/policies", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(ListResponse::class.java, Policy::class.java), typeCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }

    @Test
    fun `listing policies with query params passes them correctly`() {
      val queryParams = ListPoliciesQueryParams(limit = 5, pageToken = "cursor123")
      policies.list(queryParams)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<ListResponse<Policy>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/policies", pathCaptor.firstValue)
      assertEquals(queryParams, queryParamCaptor.firstValue)
    }

    @Test
    fun `finding a policy calls requests with the correct params`() {
      val policyId = "policy-abc123"
      policies.find(policyId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<Response<Policy>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/policies/$policyId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Policy::class.java), typeCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }

    @Test
    fun `creating a policy calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(CreatePolicyRequest::class.java)
      val requestBody = CreatePolicyRequest(name = "My Policy")
      policies.create(requestBody)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePost<Response<Policy>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/policies", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Policy::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(requestBody), requestBodyCaptor.firstValue)
    }

    @Test
    fun `updating a policy calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(UpdatePolicyRequest::class.java)
      val policyId = "policy-abc123"
      val requestBody = UpdatePolicyRequest(name = "Updated Policy")
      policies.update(policyId, requestBody)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePut<Response<Policy>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/policies/$policyId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Policy::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(requestBody), requestBodyCaptor.firstValue)
    }

    @Test
    fun `destroying a policy calls requests with the correct params`() {
      val policyId = "policy-abc123"
      policies.destroy(policyId)

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

      assertEquals("v3/policies/$policyId", pathCaptor.firstValue)
      assertEquals(DeleteResponse::class.java, typeCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }
  }
}
