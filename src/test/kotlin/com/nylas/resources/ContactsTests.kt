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

class ContactsTests {
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
    fun `Contact serializes properly`() {
      val adapter = JsonHelper.moshi().adapter(Contact::class.java)
      val jsonBuffer = Buffer().writeUtf8(
        """
          {
            "birthday": "1960-12-31",
            "company_name": "Nylas",
            "emails": [
              {
                "type": "work",
                "email": "john-work@example.com"
              }
            ],
            "given_name": "John",
            "grant_id": "41009df5-bf11-4c97-aa18-b285b5f2e386",
            "groups": [
              {
                "id": "starred"
              }
            ],
            "id": "5d3qmne77v32r8l4phyuksl2x",
            "im_addresses": [
              {
                "type": "other",
                "im_address": "myjabberaddress"
              }
            ],
            "job_title": "Software Engineer",
            "manager_name": "Bill",
            "middle_name": "Jacob",
            "nickname": "JD",
            "notes": "Loves ramen",
            "object": "contact",
            "office_location": "123 Main Street",
            "phone_numbers": [
              {
                "type": "work",
                "number": "+1-555-555-5555"
              }
            ],
            "physical_addresses": [
              {
                "type": "work",
                "street_address": "123 Main Street",
                "postal_code": 94107,
                "state": "CA",
                "country": "US",
                "city": "San Francisco"
              }
            ],
            "picture_url": "https://example.com/picture.jpg",
            "suffix": "Jr.",
            "surname": "Doe",
            "web_pages": [
              {
                "type": "work",
                "url": "http://www.linkedin.com/in/johndoe"
              }
            ]
          }
        """.trimIndent(),
      )

      val contact = adapter.fromJson(jsonBuffer)!!
      assertIs<Contact>(contact)
      assertEquals("1960-12-31", contact.birthday)
      assertEquals("Nylas", contact.companyName)
      assertEquals("John", contact.givenName)
      assertEquals("41009df5-bf11-4c97-aa18-b285b5f2e386", contact.grantId)
      assertEquals("5d3qmne77v32r8l4phyuksl2x", contact.id)
      assertEquals("Software Engineer", contact.jobTitle)
      assertEquals("Bill", contact.managerName)
      assertEquals("Jacob", contact.middleName)
      assertEquals("JD", contact.nickname)
      assertEquals("Loves ramen", contact.notes)
      assertEquals("contact", contact.getObject())
      assertEquals("123 Main Street", contact.officeLocation)
      assertEquals("Jr.", contact.suffix)
      assertEquals("Doe", contact.surname)
      assertEquals("https://example.com/picture.jpg", contact.pictureUrl)
      assertEquals(
        listOf(
          ContactEmail(
            type = "work",
            email = "john-work@example.com",
          ),
        ),
        contact.emails,
      )
      assertEquals(
        listOf(
          ContactGroupId(
            id = "starred",
          ),
        ),
        contact.groups,
      )
      assertEquals(
        listOf(
          InstantMessagingAddress(
            type = "other",
            imAddress = "myjabberaddress",
          ),
        ),
        contact.imAddresses,
      )
      assertEquals(
        listOf(
          PhoneNumber(
            type = "work",
            number = "+1-555-555-5555",
          ),
        ),
        contact.phoneNumbers,
      )
      assertEquals(
        listOf(
          WebPage(
            type = "work",
            url = "http://www.linkedin.com/in/johndoe",
          ),
        ),
        contact.webPages,
      )
      assertEquals(
        listOf(
          PhysicalAddress(
            type = "work",
            streetAddress = "123 Main Street",
            postalCode = "94107",
            state = "CA",
            country = "US",
            city = "San Francisco",
          ),
        ),
        contact.physicalAddresses,
      )
    }
  }

  @Nested
  inner class CrudTests {
    private lateinit var grantId: String
    private lateinit var mockNylasClient: NylasClient
    private lateinit var contacts: Contacts

    @BeforeEach
    fun setup() {
      grantId = "abc-123-grant-id"
      mockNylasClient = Mockito.mock(NylasClient::class.java)
      contacts = Contacts(mockNylasClient)
    }

    @Test
    fun `listing contacts calls requests with the correct params`() {
      val queryParams = ListContactsQueryParams(
        limit = 10,
        email = "test@gmail.com",
      )

      contacts.list(grantId, queryParams)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<ListContactsQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<ListResponse<Contact>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/contacts", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(ListResponse::class.java, Contact::class.java), typeCaptor.firstValue)
      assertEquals(queryParams, queryParamCaptor.firstValue)
    }

    @Test
    fun `listing contacts without query params calls requests with the correct params`() {
      contacts.list(grantId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<ListContactsQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<ListResponse<Contact>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/contacts", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(ListResponse::class.java, Contact::class.java), typeCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }

    @Test
    fun `finding a contact calls requests with the correct params`() {
      val contactId = "contact-123"
      val queryParams = FindContactQueryParams(
        profilePicture = true,
      )

      contacts.find(grantId, contactId, queryParams)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<FindContactQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<ListResponse<Contact>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/contacts/$contactId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Contact::class.java), typeCaptor.firstValue)
      assertEquals(queryParams, queryParamCaptor.firstValue)
    }

    @Test
    fun `finding a contact without query params calls requests with the correct params`() {
      val contactId = "contact-123"

      contacts.find(grantId, contactId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<FindContactQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<ListResponse<Contact>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/contacts/$contactId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Contact::class.java), typeCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }

    @Test
    fun `creating a contact calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(CreateContactRequest::class.java)
      val createContactRequest = CreateContactRequest(
        birthday = "1960-12-31",
        companyName = "Nylas",
        emails = listOf(
          ContactEmail(
            type = "work",
            email = "test@gmail.com",
          ),
        ),
        givenName = "John",
        groups = listOf(
          ContactGroupId(
            id = "starred",
          ),
        ),
        imAddresses = listOf(
          InstantMessagingAddress(
            type = "other",
            imAddress = "myjabberaddress",
          ),
        ),
        jobTitle = "Software Engineer",
        managerName = "Bill",
        middleName = "Jacob",
        nickname = "JD",
        notes = "Loves ramen",
        officeLocation = "123 Main Street",
        phoneNumbers = listOf(
          PhoneNumber(
            type = "work",
            number = "+1-555-555-5555",
          ),
        ),
        physicalAddresses = listOf(
          PhysicalAddress(
            type = "work",
            streetAddress = "123 Main Street",
            postalCode = "94107",
            state = "CA",
            country = "US",
            city = "San Francisco",
          ),
        ),
        suffix = "Jr.",
        surname = "Doe",
        webPages = listOf(
          WebPage(
            type = "work",
            url = "http://www.linkedin.com/in/johndoe",
          ),
        ),
      )

      contacts.create(grantId, createContactRequest)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePost<ListResponse<Contact>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/contacts", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Contact::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(createContactRequest), requestBodyCaptor.firstValue)
    }

    @Test
    fun `updating a contact calls requests with the correct params`() {
      val contactId = "contact-123"
      val adapter = JsonHelper.moshi().adapter(UpdateContactRequest::class.java)
      val updateContactRequest = UpdateContactRequest(
        birthday = "1960-12-31",
        companyName = "Nylas",
        emails = listOf(
          ContactEmail(
            type = "work",
            email = "test@gmail.com",
          ),
        ),
        givenName = "John",
        groups = listOf(
          ContactGroupId(
            id = "starred",
          ),
        ),
        imAddresses = listOf(
          InstantMessagingAddress(
            type = "other",
            imAddress = "myjabberaddress",
          ),
        ),
        jobTitle = "Software Engineer",
        managerName = "Bill",
        middleName = "Jacob",
        nickname = "JD",
        notes = "Loves ramen",
        officeLocation = "123 Main Street",
        phoneNumbers = listOf(
          PhoneNumber(
            type = "work",
            number = "+1-555-555-5555",
          ),
        ),
        physicalAddresses = listOf(
          PhysicalAddress(
            type = "work",
            streetAddress = "123 Main Street",
            postalCode = "94107",
            state = "CA",
            country = "US",
            city = "San Francisco",
          ),
        ),
        suffix = "Jr.",
        surname = "Doe",
        webPages = listOf(
          WebPage(
            type = "work",
            url = "http://www.linkedin.com/in/johndoe",
          ),
        ),
      )

      contacts.update(grantId, contactId, updateContactRequest)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePut<ListResponse<Contact>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/contacts/$contactId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Contact::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(updateContactRequest), requestBodyCaptor.firstValue)
    }

    @Test
    fun `destroying a contact calls requests with the correct params`() {
      val contactId = "contact-123"

      contacts.destroy(grantId, contactId)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeDelete<ListResponse<Contact>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/contacts/$contactId", pathCaptor.firstValue)
      assertEquals(DeleteResponse::class.java, typeCaptor.firstValue)
    }
  }

  @Nested
  inner class OtherMethodTests {
    private lateinit var grantId: String
    private lateinit var mockNylasClient: NylasClient
    private lateinit var contacts: Contacts

    @BeforeEach
    fun setup() {
      grantId = "abc-123-grant-id"
      mockNylasClient = Mockito.mock(NylasClient::class.java)
      contacts = Contacts(mockNylasClient)
    }

    @Test
    fun `getting contact groups calls requests with the correct params`() {
      val grantId = "abc-123-grant-id"
      val queryParams = ListContactGroupsQueryParams(
        limit = 10,
      )

      contacts.listGroups(grantId, queryParams)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<ListContactGroupsQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<ListResponse<ContactGroup>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/contacts/groups", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(ListResponse::class.java, ContactGroup::class.java), typeCaptor.firstValue)
      assertEquals(queryParams, queryParamCaptor.firstValue)
    }

    @Test
    fun `getting contact groups without query params calls requests with the correct params`() {
      val grantId = "abc-123-grant-id"

      contacts.listGroups(grantId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<ListContactGroupsQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<ListResponse<ContactGroup>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/contacts/groups", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(ListResponse::class.java, ContactGroup::class.java), typeCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }
  }
}
