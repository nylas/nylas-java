package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import okhttp3.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import kotlin.test.*

/**
 * Integration tests to verify that select parameter functionality works end-to-end,
 * focusing on the response parsing and URL construction.
 */
class SelectParameterIntegrationTests {

  private lateinit var grantId: String
  private lateinit var mockNylasClient: NylasClient

  @BeforeEach
  fun setup() {
    grantId = "abc-123-grant-id"
    mockNylasClient = mock<NylasClient>()
  }

  @Test
  fun `list folders with select parameter includes correct query params`() {
    val folders = Folders(mockNylasClient)
    val queryParams = ListFoldersQueryParams(
      limit = 10,
      select = "id,name",
    )

    folders.list(grantId, queryParams)

    val pathCaptor = argumentCaptor<String>()
    val typeCaptor = argumentCaptor<java.lang.reflect.Type>()
    val queryParamCaptor = argumentCaptor<IQueryParams>()
    val overrideParamCaptor = argumentCaptor<RequestOverrides>()

    verify(mockNylasClient).executeGet<ListResponse<Folder>>(
      pathCaptor.capture(),
      typeCaptor.capture(),
      queryParamCaptor.capture(),
      overrideParamCaptor.capture(),
    )

    assertEquals("v3/grants/$grantId/folders", pathCaptor.firstValue)
    assertEquals(queryParams, queryParamCaptor.firstValue)
    assertEquals("id,name", (queryParamCaptor.firstValue as ListFoldersQueryParams).select)
  }

  @Test
  fun `find folder with select parameter includes correct query params`() {
    val folders = Folders(mockNylasClient)
    val queryParams = FindFolderQueryParams(select = "id,name")

    folders.find(grantId, "folder-123", queryParams)

    val pathCaptor = argumentCaptor<String>()
    val typeCaptor = argumentCaptor<java.lang.reflect.Type>()
    val queryParamCaptor = argumentCaptor<IQueryParams>()
    val overrideParamCaptor = argumentCaptor<RequestOverrides>()

    verify(mockNylasClient).executeGet<com.nylas.models.Response<Folder>>(
      pathCaptor.capture(),
      typeCaptor.capture(),
      queryParamCaptor.capture(),
      overrideParamCaptor.capture(),
    )

    assertEquals("v3/grants/$grantId/folders/folder-123", pathCaptor.firstValue)
    assertEquals(queryParams, queryParamCaptor.firstValue)
    assertEquals("id,name", (queryParamCaptor.firstValue as FindFolderQueryParams).select)
  }

  @Test
  fun `list contacts with select parameter includes correct query params`() {
    val contacts = Contacts(mockNylasClient)
    val queryParams = ListContactsQueryParams(
      limit = 10,
      select = "id,display_name",
    )

    contacts.list(grantId, queryParams)

    val pathCaptor = argumentCaptor<String>()
    val queryParamCaptor = argumentCaptor<IQueryParams>()

    verify(mockNylasClient).executeGet<ListResponse<Contact>>(
      pathCaptor.capture(),
      any(),
      queryParamCaptor.capture(),
      any(),
    )

    assertEquals("v3/grants/$grantId/contacts", pathCaptor.firstValue)
    assertEquals("id,display_name", (queryParamCaptor.firstValue as ListContactsQueryParams).select)
  }

  @Test
  fun `list messages with select parameter includes correct query params`() {
    val messages = Messages(mockNylasClient)
    val queryParams = ListMessagesQueryParams(
      limit = 10,
      select = "id,subject",
    )

    messages.list(grantId, queryParams)

    val pathCaptor = argumentCaptor<String>()
    val queryParamCaptor = argumentCaptor<IQueryParams>()

    verify(mockNylasClient).executeGet<ListResponse<Message>>(
      pathCaptor.capture(),
      any(),
      queryParamCaptor.capture(),
      any(),
    )

    assertEquals("v3/grants/$grantId/messages", pathCaptor.firstValue)
    assertEquals("id,subject", (queryParamCaptor.firstValue as ListMessagesQueryParams).select)
  }
}
