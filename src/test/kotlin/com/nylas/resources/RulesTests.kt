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

class RulesTests {
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
    fun `Rule deserializes properly`() {
      val adapter = JsonHelper.moshi().adapter(Rule::class.java)
      val jsonBuffer = Buffer().writeUtf8(
        """
          {
            "id": "c1d2e3f4-5678-4abc-9def-0123456789ab",
            "name": "Block spam domains",
            "description": "Rejects messages from known spam domains.",
            "priority": 1,
            "enabled": true,
            "trigger": "inbound",
            "match": {
              "operator": "any",
              "conditions": [
                {
                  "field": "from.domain",
                  "operator": "is",
                  "value": "spam-domain.com"
                },
                {
                  "field": "from.tld",
                  "operator": "is",
                  "value": "xyz"
                }
              ]
            },
            "actions": [
              {
                "type": "block"
              }
            ],
            "application_id": "ad410018-d306-43f9-8361-fa5d7b2172e0",
            "organization_id": "org-abc123",
            "created_at": 1742932766,
            "updated_at": 1742932766
          }
        """.trimIndent(),
      )

      val rule = adapter.fromJson(jsonBuffer)!!
      assertIs<Rule>(rule)
      assertEquals("c1d2e3f4-5678-4abc-9def-0123456789ab", rule.id)
      assertEquals("Block spam domains", rule.name)
      assertEquals("Rejects messages from known spam domains.", rule.description)
      assertEquals(1, rule.priority)
      assertEquals(true, rule.enabled)
      assertEquals(RuleTrigger.INBOUND, rule.trigger)
      assertEquals(RuleMatchOperator.ANY, rule.match.operator)
      assertEquals(2, rule.match.conditions.size)
      assertEquals("from.domain", rule.match.conditions[0].field)
      assertEquals(RuleConditionOperator.IS, rule.match.conditions[0].operator)
      assertEquals("spam-domain.com", rule.match.conditions[0].value)
      assertEquals(1, rule.actions.size)
      assertEquals(RuleActionType.BLOCK, rule.actions[0].type)
      assertNull(rule.actions[0].value)
      assertEquals("ad410018-d306-43f9-8361-fa5d7b2172e0", rule.applicationId)
      assertEquals(1742932766L, rule.createdAt)
    }

    @Test
    fun `Outbound rule with assign_to_folder action deserializes properly`() {
      val adapter = JsonHelper.moshi().adapter(Rule::class.java)
      val jsonBuffer = Buffer().writeUtf8(
        """
          {
            "id": "e3f4a5b6-789a-4cde-bf01-23456789abcd",
            "name": "Archive sent replies",
            "priority": 50,
            "enabled": true,
            "trigger": "outbound",
            "match": {
              "operator": "all",
              "conditions": [
                {
                  "field": "outbound.type",
                  "operator": "is",
                  "value": "reply"
                }
              ]
            },
            "actions": [
              {
                "type": "assign_to_folder",
                "value": "Label_1234567890"
              }
            ],
            "application_id": "ad410018-d306-43f9-8361-fa5d7b2172e0",
            "organization_id": "org-abc123",
            "created_at": 1742933005,
            "updated_at": 1742933005
          }
        """.trimIndent(),
      )

      val rule = adapter.fromJson(jsonBuffer)!!
      assertIs<Rule>(rule)
      assertEquals(RuleTrigger.OUTBOUND, rule.trigger)
      assertEquals(RuleMatchOperator.ALL, rule.match.operator)
      assertEquals("outbound.type", rule.match.conditions[0].field)
      assertEquals(RuleActionType.ASSIGN_TO_FOLDER, rule.actions[0].type)
      assertEquals("Label_1234567890", rule.actions[0].value)
    }

    @Test
    fun `CreateRuleRequest serializes with all required fields`() {
      val adapter = JsonHelper.moshi().adapter(CreateRuleRequest::class.java)
      val request = CreateRuleRequest(
        name = "Block spam",
        trigger = RuleTrigger.INBOUND,
        match = RuleMatch(
          operator = RuleMatchOperator.ANY,
          conditions = listOf(RuleCondition(field = "from.domain", operator = RuleConditionOperator.IS, value = "spam.com")),
        ),
        actions = listOf(RuleAction(type = RuleActionType.BLOCK)),
      )

      val json = adapter.toJson(request)
      val deserialized = adapter.fromJson(json)!!
      assertEquals("Block spam", deserialized.name)
      assertEquals(RuleTrigger.INBOUND, deserialized.trigger)
      assertEquals(RuleMatchOperator.ANY, deserialized.match.operator)
      assertEquals(1, deserialized.match.conditions.size)
      assertEquals(RuleActionType.BLOCK, deserialized.actions[0].type)
      assertNull(deserialized.description)
      assertNull(deserialized.priority)
      assertNull(deserialized.enabled)
    }

    @Test
    fun `CreateRuleRequest Builder sets all fields`() {
      val request = CreateRuleRequest.Builder(
        name = "My Rule",
        trigger = RuleTrigger.OUTBOUND,
        match = RuleMatch(operator = RuleMatchOperator.ALL, conditions = emptyList()),
        actions = listOf(RuleAction(type = RuleActionType.ARCHIVE)),
      )
        .description("Test rule")
        .priority(5)
        .enabled(false)
        .build()

      assertEquals("My Rule", request.name)
      assertEquals("Test rule", request.description)
      assertEquals(5, request.priority)
      assertEquals(false, request.enabled)
    }
  }

  @Nested
  inner class CrudTests {
    private lateinit var mockNylasClient: NylasClient
    private lateinit var rules: Rules

    @BeforeEach
    fun setup() {
      mockNylasClient = Mockito.mock(NylasClient::class.java)
      rules = Rules(mockNylasClient)
    }

    @Test
    fun `listing rules calls requests with the correct params`() {
      rules.list()

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<ListResponse<Rule>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/rules", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(ListResponse::class.java, Rule::class.java), typeCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }

    @Test
    fun `finding a rule calls requests with the correct params`() {
      val ruleId = "rule-abc123"
      rules.find(ruleId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<Response<Rule>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/rules/$ruleId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Rule::class.java), typeCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }

    @Test
    fun `creating a rule calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(CreateRuleRequest::class.java)
      val requestBody = CreateRuleRequest(
        name = "Block spam",
        trigger = RuleTrigger.INBOUND,
        match = RuleMatch(
          operator = RuleMatchOperator.ANY,
          conditions = listOf(RuleCondition(field = "from.domain", operator = RuleConditionOperator.IS, value = "spam.com")),
        ),
        actions = listOf(RuleAction(type = RuleActionType.BLOCK)),
      )
      rules.create(requestBody)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePost<Response<Rule>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/rules", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Rule::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(requestBody), requestBodyCaptor.firstValue)
    }

    @Test
    fun `updating a rule calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(UpdateRuleRequest::class.java)
      val ruleId = "rule-abc123"
      val requestBody = UpdateRuleRequest(name = "Updated Rule", priority = 5)
      rules.update(ruleId, requestBody)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePut<Response<Rule>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/rules/$ruleId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Rule::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(requestBody), requestBodyCaptor.firstValue)
    }

    @Test
    fun `destroying a rule calls requests with the correct params`() {
      val ruleId = "rule-abc123"
      rules.destroy(ruleId)

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

      assertEquals("v3/rules/$ruleId", pathCaptor.firstValue)
      assertEquals(DeleteResponse::class.java, typeCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }
  }
}
