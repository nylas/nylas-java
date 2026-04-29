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

class NylasListsTests {
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
    fun `NylasList deserializes properly`() {
      val adapter = JsonHelper.moshi().adapter(NylasList::class.java)
      val jsonBuffer = Buffer().writeUtf8(
        """
          {
            "id": "d1e2f3a4-5678-4abc-9def-0123456789ab",
            "name": "Blocked domains",
            "description": "Domains sending unwanted mail.",
            "type": "domain",
            "items_count": 42,
            "application_id": "ad410018-d306-43f9-8361-fa5d7b2172e0",
            "organization_id": "org-abc123",
            "created_at": 1742932766,
            "updated_at": 1742932766
          }
        """.trimIndent(),
      )

      val list = adapter.fromJson(jsonBuffer)!!
      assertIs<NylasList>(list)
      assertEquals("d1e2f3a4-5678-4abc-9def-0123456789ab", list.id)
      assertEquals("Blocked domains", list.name)
      assertEquals("Domains sending unwanted mail.", list.description)
      assertEquals(NylasListType.DOMAIN, list.type)
      assertEquals(42, list.itemsCount)
      assertEquals("ad410018-d306-43f9-8361-fa5d7b2172e0", list.applicationId)
      assertEquals("org-abc123", list.organizationId)
      assertEquals(1742932766L, list.createdAt)
      assertEquals(1742932766L, list.updatedAt)
    }

    @Test
    fun `NylasListItem deserializes properly`() {
      val adapter = JsonHelper.moshi().adapter(NylasListItem::class.java)
      val jsonBuffer = Buffer().writeUtf8(
        """
          {
            "id": "e1f2a3b4-5678-4abc-9def-0123456789ab",
            "list_id": "d1e2f3a4-5678-4abc-9def-0123456789ab",
            "value": "spam-domain.com",
            "created_at": 1742932766
          }
        """.trimIndent(),
      )

      val item = adapter.fromJson(jsonBuffer)!!
      assertIs<NylasListItem>(item)
      assertEquals("e1f2a3b4-5678-4abc-9def-0123456789ab", item.id)
      assertEquals("d1e2f3a4-5678-4abc-9def-0123456789ab", item.listId)
      assertEquals("spam-domain.com", item.value)
      assertEquals(1742932766L, item.createdAt)
    }

    @Test
    fun `CreateNylasListRequest serializes correctly`() {
      val adapter = JsonHelper.moshi().adapter(CreateNylasListRequest::class.java)
      val request = CreateNylasListRequest(name = "Blocked domains", type = NylasListType.DOMAIN)
      val json = adapter.toJson(request)
      val deserialized = adapter.fromJson(json)!!
      assertEquals("Blocked domains", deserialized.name)
      assertEquals(NylasListType.DOMAIN, deserialized.type)
      assertNull(deserialized.description)
    }

    @Test
    fun `CreateNylasListRequest Builder sets all fields`() {
      val request = CreateNylasListRequest.Builder("My TLD List", NylasListType.TLD)
        .description("Top-level domains to block")
        .build()

      assertEquals("My TLD List", request.name)
      assertEquals(NylasListType.TLD, request.type)
      assertEquals("Top-level domains to block", request.description)
    }

    @Test
    fun `ListItemsRequest serializes correctly`() {
      val adapter = JsonHelper.moshi().adapter(ListItemsRequest::class.java)
      val request = ListItemsRequest(items = listOf("spam.com", "bad.net"))
      val json = adapter.toJson(request)
      val deserialized = adapter.fromJson(json)!!
      assertEquals(listOf("spam.com", "bad.net"), deserialized.items)
    }
  }

  @Nested
  inner class CrudTests {
    private lateinit var mockNylasClient: NylasClient
    private lateinit var nylaslists: NylasLists

    @BeforeEach
    fun setup() {
      mockNylasClient = Mockito.mock(NylasClient::class.java)
      nylaslists = NylasLists(mockNylasClient)
    }

    @Test
    fun `listing lists calls requests with the correct params`() {
      nylaslists.list()

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<ListResponse<NylasList>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/lists", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(ListResponse::class.java, NylasList::class.java), typeCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }

    @Test
    fun `listing lists with query params passes them correctly`() {
      val queryParams = ListNylasListsQueryParams(limit = 5, pageToken = "cursor123")
      nylaslists.list(queryParams)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<ListResponse<NylasList>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/lists", pathCaptor.firstValue)
      assertEquals(queryParams, queryParamCaptor.firstValue)
    }

    @Test
    fun `finding a list calls requests with the correct params`() {
      val listId = "list-abc123"
      nylaslists.find(listId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<Response<NylasList>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/lists/$listId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, NylasList::class.java), typeCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }

    @Test
    fun `creating a list calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(CreateNylasListRequest::class.java)
      val requestBody = CreateNylasListRequest(name = "Blocked domains", type = NylasListType.DOMAIN)
      nylaslists.create(requestBody)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePost<Response<NylasList>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/lists", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, NylasList::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(requestBody), requestBodyCaptor.firstValue)
    }

    @Test
    fun `updating a list calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(UpdateNylasListRequest::class.java)
      val listId = "list-abc123"
      val requestBody = UpdateNylasListRequest(name = "Updated name")
      nylaslists.update(listId, requestBody)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePut<Response<NylasList>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/lists/$listId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, NylasList::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(requestBody), requestBodyCaptor.firstValue)
    }

    @Test
    fun `destroying a list calls requests with the correct params`() {
      val listId = "list-abc123"
      nylaslists.destroy(listId)

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

      assertEquals("v3/lists/$listId", pathCaptor.firstValue)
      assertEquals(DeleteResponse::class.java, typeCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }

    @Test
    fun `listing items in a list calls requests with the correct params`() {
      val listId = "list-abc123"
      nylaslists.listItems(listId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<ListResponse<NylasListItem>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/lists/$listId/items", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(ListResponse::class.java, NylasListItem::class.java), typeCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }

    @Test
    fun `adding items to a list calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(ListItemsRequest::class.java)
      val listId = "list-abc123"
      val requestBody = ListItemsRequest(items = listOf("spam.com", "bad.net"))
      nylaslists.addItems(listId, requestBody)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePost<Response<NylasList>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/lists/$listId/items", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, NylasList::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(requestBody), requestBodyCaptor.firstValue)
    }

    @Test
    fun `removing items from a list calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(ListItemsRequest::class.java)
      val listId = "list-abc123"
      val requestBody = ListItemsRequest(items = listOf("spam.com"))
      nylaslists.removeItems(listId, requestBody)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeDelete<Response<NylasList>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/lists/$listId/items", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, NylasList::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(requestBody), requestBodyCaptor.firstValue)
    }
  }
}
