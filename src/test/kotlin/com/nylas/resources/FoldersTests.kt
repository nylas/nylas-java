package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.models.Response
import com.nylas.util.JsonHelper
import com.squareup.moshi.Types
import okhttp3.*
import okio.Buffer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.*
import java.lang.reflect.Type
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class FoldersTests {
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
    fun `Folder serializes properly`() {
      val adapter = JsonHelper.moshi().adapter(Folder::class.java)
      val jsonBuffer = Buffer().writeUtf8(
        """
          {
            "id": "SENT",
            "grant_id": "41009df5-bf11-4c97-aa18-b285b5f2e386",
            "name": "SENT",
            "system_folder": true,
            "object": "folder",
            "unread_count": 0,
            "child_count": 0,
            "parent_id": "ascsf21412",
            "background_color": "#039BE5",
            "text_color": "#039BE5",
            "total_count": 0
          }
        """.trimIndent(),
      )

      val folder = adapter.fromJson(jsonBuffer)!!
      assertIs<Folder>(folder)
      assertEquals("SENT", folder.id)
      assertEquals("41009df5-bf11-4c97-aa18-b285b5f2e386", folder.grantId)
      assertEquals("SENT", folder.name)
      assertEquals(true, folder.systemFolder)
      assertEquals("folder", folder.getObject())
      assertEquals(0, folder.unreadCount)
      assertEquals(0, folder.childCount)
      assertEquals("ascsf21412", folder.parentId)
      assertEquals("#039BE5", folder.backgroundColor)
      assertEquals("#039BE5", folder.textColor)
      assertEquals(0, folder.totalCount)
    }
  }

  @Nested
  inner class CrudTests {
    private lateinit var grantId: String
    private lateinit var mockNylasClient: NylasClient
    private lateinit var folders: Folders

    @BeforeEach
    fun setup() {
      grantId = "abc-123-grant-id"
      mockNylasClient = Mockito.mock(NylasClient::class.java)
      folders = Folders(mockNylasClient)
    }

    @Test
    fun `listing folders calls requests with the correct params`() {
      folders.list(grantId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      verify(mockNylasClient).executeGet<ListResponse<Folder>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/folders", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(ListResponse::class.java, Folder::class.java), typeCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }

    @Test
    fun `finding a folder calls requests with the correct params`() {
      val folderId = "folder-123"

      folders.find(grantId, folderId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      verify(mockNylasClient).executeGet<ListResponse<Folder>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/folders/$folderId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Folder::class.java), typeCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }

    @Test
    fun `creating a folder calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(CreateFolderRequest::class.java)
      val createFolderRequest = CreateFolderRequest(
        name = "My New Folder",
        parentId = "parent-folder-id",
        backgroundColor = "#039BE5",
        textColor = "#039BE5",
      )

      folders.create(grantId, createFolderRequest)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      verify(mockNylasClient).executePost<ListResponse<Folder>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/folders", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Folder::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(createFolderRequest), requestBodyCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }

    @Test
    fun `updating a folder calls requests with the correct params`() {
      val folderId = "folder-123"
      val adapter = JsonHelper.moshi().adapter(UpdateFolderRequest::class.java)
      val updateFolderRequest = UpdateFolderRequest(
        name = "My New Folder",
        parentId = "parent-folder-id",
        backgroundColor = "#039BE5",
        textColor = "#039BE5",
      )

      folders.update(grantId, folderId, updateFolderRequest)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      verify(mockNylasClient).executePut<ListResponse<Folder>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/folders/$folderId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Folder::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(updateFolderRequest), requestBodyCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }

    @Test
    fun `destroying a folder calls requests with the correct params`() {
      val folderId = "folder-123"

      folders.destroy(grantId, folderId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      verify(mockNylasClient).executeDelete<ListResponse<Folder>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/folders/$folderId", pathCaptor.firstValue)
      assertEquals(DeleteResponse::class.java, typeCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }
  }
}
