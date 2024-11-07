package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.models.ListConfigurationsQueryParams
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

class ConfigurationsTest {
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
    fun `Configuration serializes properly`() {
      val adapter = JsonHelper.moshi().adapter(Configuration::class.java)
      val jsonBuffer = Buffer().writeUtf8(
        """
        {
          "id": "abc-123-configuration-id",
          "slug": None,
          "participants": [
              {
                  "email": "test@nylas.com",
                  "isOrganizer": true,
                  "name": "Test",
                  "availability": {
                      "calendarIds": [
                          "primary"
                      ],
                      "openHours": [
                          {
                              "days": [
                                  0,
                                  1,
                                  2,
                                  3,
                                  4,
                                  5,
                                  6
                              ],
                              "exdates": None,
                              "timezone": "",
                              "start": "09:00",
                              "end": "17:00"
                          }
                      ]
                  },
                  "booking": {
                      "calendarId": "primary"
                  },
                  "timezone": ""
              }
          ],
          "requiresSessionAuth": false,
          "availability": {
              "durationMinutes": 30,
              "intervalMinutes": 15,
              "roundTo": 15,
              "availabilityRules": {
                  "availabilityMethod": "collective",
                  "buffer": {
                      "before": 60,
                      "after": 0
                  },
                  "defaultOpenHours": [
                      {
                          "days": [
                              0,
                              1,
                              2,
                              5,
                              6
                          ],
                          "exdates": None,
                          "timezone": "",
                          "start": "09:00",
                          "end": "18:00"
                      }
                  ],
                  "roundRobinGroupId": ""
              }
          },
          "eventBooking": {
              "title": "Updated Title",
              "timezone": "utc",
              "description": "",
              "location": "none",
              "bookingType": "booking",
              "conferencing": {
                  "provider": "Microsoft Teams",
                  "autocreate": {
                      "conf_grant_id": "",
                      "conf_settings": None
                  }
              },
              "hideParticipants": null,
              "disableEmails": null
          },
          "scheduler": {
              "availableDaysInFuture": 7,
              "minCancellationNotice": 60,
              "minBookingNotice": 120,
              "confirmationRedirectUrl": "",
              "hideReschedulingOptions": false,
              "hideCancellationOptions": false,
              "hideAdditionalGuests": true,
              "cancellationPolicy": "",
              "emailTemplate": {
                  "bookingConfirmed": {}
              }
          },
          "appearance": {
              "submitButtonLabel": "submit",
              "thankYouMessage": "thank you for your business. your booking was successful."
          }
        }
        """.trimIndent(),
      )

      val config = adapter.fromJson(jsonBuffer)!!
      assertIs<Configuration>(config)
      assertEquals("abc-123-configuration-id", config.id)
      assertEquals(false, config.requiresSessionAuth)
      assertEquals(7, config.scheduler?.availableDaysInFuture)
      assertEquals(60, config.scheduler?.minCancellationNotice)
      assertEquals(120, config.scheduler?.minBookingNotice)
      assertEquals("", config.scheduler?.confirmationRedirectUrl)
      assertEquals(false, config.scheduler?.hideReschedulingOptions)
      assertEquals(false, config.scheduler?.hideCancellationOptions)
      assertEquals(true, config.scheduler?.hideAdditionalGuests)
      assertEquals("", config.scheduler?.cancellationPolicy)
      assertEquals("submit", config.appearance?.get("submitButtonLabel"))
      assertEquals("thank you for your business. your booking was successful.", config.appearance?.get("thankYouMessage"))
      assertEquals(15, config.availability.durationMinutes)
      assertEquals(15, config.availability.intervalMinutes)
      assertEquals(15, config.availability.roundTo)
      assertEquals(AvailabilityMethod.COLLECTIVE, config.availability.availabilityRules?.availabilityMethod)
      assertEquals(60, config.availability.availabilityRules?.buffer?.before)
      assertEquals(0, config.availability.availabilityRules?.buffer?.after)
      assertEquals("", config.availability.availabilityRules?.roundRobinGroupId)
      assertEquals(0, config.availability.availabilityRules?.defaultOpenHours?.first()?.days?.size)
      assertEquals("09:00", config.availability.availabilityRules?.defaultOpenHours?.first()?.start)
      assertEquals("18:00", config.availability.availabilityRules?.defaultOpenHours?.first()?.end)
    }
  }

  @Nested
  inner class CrudTests {
    private lateinit var grantId: String
    private lateinit var mockNylasClient: NylasClient
    private lateinit var configurations: Configurations

    @BeforeEach
    fun setup() {
      grantId = "abc-123-grant-id"
      mockNylasClient = Mockito.mock(NylasClient::class.java)
      configurations = Configurations(mockNylasClient)
    }

    @Test
    fun `listing configurations calls requests with the correct params`() {
      configurations.list(grantId)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<ListConfigurationsQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<ListResponse<Configuration>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/grants/$grantId/configurations", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(ListResponse::class.java, Configuration::class.java), typeCaptor.firstValue)
    }

    @Test
    fun `creating a configuration calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(CreateConfigurationRequest::class.java)
      val participantCalendarIds = ArrayList<String>()
      participantCalendarIds.add("primary")

      val configurationAvailabilityParticipant = ConfigurationAvailabilityParticipant.Builder().calendarIds(participantCalendarIds).build()

      val configurationBookingParticipant = ConfigurationBookingParticipant.Builder().calendarId("primary").build()

      val configurationParticipant = ConfigurationParticipant.Builder("test@nylas.com")
        .availability(configurationAvailabilityParticipant)
        .booking(configurationBookingParticipant)
        .name("Test Participant")
        .isOrganizer(true)
        .build()

      val configurationAvailability = ConfigurationAvailability.Builder().intervalMinutes(30).build()

      val configurationEventBooking = ConfigurationEventBooking.Builder().title("Test Event Booking").build()

      val participants = ArrayList<ConfigurationParticipant>()
      participants.add(configurationParticipant)

      val createConfigurationRequest = CreateConfigurationRequest.Builder(
        participants,
        configurationAvailability,
        configurationEventBooking,
      ).build()

      configurations.create(grantId, createConfigurationRequest)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      verify(mockNylasClient).executePost<Response<Configuration>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
      )
      assertEquals("v3/grants/$grantId/configurations", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Configuration::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(createConfigurationRequest), requestBodyCaptor.firstValue)
    }

    @Test
    fun `updating a configuration calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(UpdateConfigurationRequest::class.java)
      val configId = "abc-123-configuration-id"
      val config = ConfigurationSchedulerSettings.Builder().minBookingNotice(120).build()
      val updateConfigurationRequest = UpdateConfigurationRequest().Builder().scheduler(config).build()
    
      configurations.update(grantId, configId, updateConfigurationRequest)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      verify(mockNylasClient).executePut<Response<Configuration>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
      )
      assertEquals("v3/grants/$grantId/configurations/$configId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Configuration::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(updateConfigurationRequest), requestBodyCaptor.firstValue)
    }

    @Test
    fun `finding a configuration calls requests with the correct params`() {
      val configId = "configuration-id"
      configurations.find(grantId, configId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      verify(mockNylasClient).executeGet<Response<Configuration>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
      )
      assertEquals("v3/grants/$grantId/configurations/$configId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Configuration::class.java), typeCaptor.firstValue)

    }

    @Test
    fun `destroying a configuration calls requests with the correct params`() {
      val configId = "configuration-id"
      configurations.destroy(grantId, configId)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeDelete<DeleteResponse>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        overrideParamCaptor.capture(),  
      )
      assertEquals("v3/grants/$grantId/configurations/$configId", pathCaptor.firstValue)
      assertEquals(DeleteResponse::class.java, typeCaptor.firstValue)
    }
  }
}
