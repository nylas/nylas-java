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
          "name": "My Scheduling Page",
          "slug": null,
          "participants": [
              {
                  "email": "test@nylas.com",
                  "is_organizer": true,
                  "name": "Test",
                  "availability": {
                      "calendar_ids": [
                          "primary"
                      ],
                      "open_hours": [
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
                              "exdates": null,
                              "timezone": "",
                              "start": "09:00",
                              "end": "17:00"
                          }
                      ]
                  },
                  "booking": {
                      "calendar_id": "primary"
                  },
                  "timezone": ""
              }
          ],
          "requires_session_auth": false,
          "availability": {
              "duration_minutes": 30,
              "interval_minutes": 15,
              "round_to": 15,
              "availability_rules": {
                  "availability_method": "collective",
                  "buffer": {
                      "before": 60,
                      "after": 0
                  },
                  "default_open_hours": [
                      {
                          "days": [
                              0,
                              1,
                              2,
                              5,
                              6
                          ],
                          "exdates": null,
                          "timezone": "",
                          "start": "09:00",
                          "end": "18:00"
                      }
                  ],
                  "round_robin_group_id": ""
              }
          },
          "event_booking": {
              "title": "Updated Title",
              "timezone": "utc",
              "description": "",
              "location": null,
              "booking_type": "booking",
              "conferencing": {
                  "provider": "Microsoft Teams",
                  "autocreate": {
                      "conf_grant_id": "",
                      "conf_settings": null
                  }
              },
              "hide_participants": null,
              "disable_emails": null
          },
          "scheduler": {
              "available_days_in_future": 7,
              "min_cancellation_notice": 60,
              "min_booking_notice": 120,
              "confirmation_redirect_url": "",
              "hide_rescheduling_options": false,
              "hide_cancellation_options": false,
              "hide_additional_guests": true,
              "cancellation_policy": "",
              "email_template": {
                  "booking_confirmed": {}
              }
          },
          "appearance": {
              "submit_button_label": "submit",
              "thank_you_message": "thank you for your business. your booking was successful."
          }
        }
        """.trimIndent(),
      )

      val config = adapter.fromJson(jsonBuffer)!!
      assertIs<Configuration>(config)
      assertEquals("abc-123-configuration-id", config.id)
      assertEquals("My Scheduling Page", config.name)
      assertEquals(false, config.requiresSessionAuth)
      assertEquals(7, config.scheduler?.availableDaysInFuture)
      assertEquals(60, config.scheduler?.minCancellationNotice)
      assertEquals(120, config.scheduler?.minBookingNotice)
      assertEquals("", config.scheduler?.confirmationRedirectUrl)
      assertEquals(false, config.scheduler?.hideReschedulingOptions)
      assertEquals(false, config.scheduler?.hideCancellationOptions)
      assertEquals(true, config.scheduler?.hideAdditionalGuests)
      assertEquals("", config.scheduler?.cancellationPolicy)
      assertEquals("submit", config.appearance?.get("submit_button_label"))
      assertEquals(30, config.availability.durationMinutes)
      assertEquals(15, config.availability.intervalMinutes)
      assertEquals(15, config.availability.roundTo)
      assertEquals(AvailabilityMethod.COLLECTIVE, config.availability.availabilityRules?.availabilityMethod)
      assertEquals(60, config.availability.availabilityRules?.buffer?.before)
      assertEquals(0, config.availability.availabilityRules?.buffer?.after)
      assertEquals("", config.availability.availabilityRules?.roundRobinGroupId)
      assertEquals(5, config.availability.availabilityRules?.defaultOpenHours?.first()?.days?.size)
      assertEquals("09:00", config.availability.availabilityRules?.defaultOpenHours?.first()?.start)
      assertEquals("18:00", config.availability.availabilityRules?.defaultOpenHours?.first()?.end)
      assertEquals("Updated Title", config.eventBooking.title)
      assertEquals("utc", config.eventBooking.timezone)
      assertEquals("", config.eventBooking.description)
      assertEquals(null, config.eventBooking.location)
      assertEquals(BookingType.BOOKING, config.eventBooking.bookingType)
      assertEquals(null, config.eventBooking.hideParticipants)
      assertEquals(null, config.eventBooking.disableEmails)
      assertEquals(true, config.participants.first().isOrganizer)
      assertEquals("test@nylas.com", config.participants.first().email)
      assertEquals("Test", config.participants.first().name)
      assertEquals("", config.participants.first().timezone)
    }

    @Test
    fun `EmailTemplate with new fields serializes properly`() {
      val adapter = JsonHelper.moshi().adapter(EmailTemplate::class.java)
      val jsonBuffer = Buffer().writeUtf8(
        """
        {
          "booking_confirmed": {
            "title": "Custom Booking Title",
            "body": "Thank you for booking with us!"
          },
          "logo": "https://example.com/logo.png",
          "show_nylas_branding": false
        }
        """.trimIndent(),
      )

      val emailTemplate = adapter.fromJson(jsonBuffer)!!
      assertIs<EmailTemplate>(emailTemplate)
      assertEquals("https://example.com/logo.png", emailTemplate.logo)
      assertEquals(false, emailTemplate.showNylasBranding)
      assertEquals("Custom Booking Title", emailTemplate.bookingConfirmed?.title)
      assertEquals("Thank you for booking with us!", emailTemplate.bookingConfirmed?.body)

      // Test serialization back to JSON
      val serializedJson = adapter.toJson(emailTemplate)
      assert(serializedJson.contains("\"logo\":\"https://example.com/logo.png\""))
      assert(serializedJson.contains("\"show_nylas_branding\":false"))
    }

    @Test
    fun `EmailTemplate Builder works correctly`() {
      val bookingConfirmed = BookingConfirmedTemplate(
        title = "Custom Title",
        body = "Custom Body"
      )
      
      val emailTemplate = EmailTemplate.Builder()
        .bookingConfirmed(bookingConfirmed)
        .logo("https://company.com/logo.svg")
        .showNylasBranding(true)
        .build()

      assertEquals("https://company.com/logo.svg", emailTemplate.logo)
      assertEquals(true, emailTemplate.showNylasBranding)
      assertEquals("Custom Title", emailTemplate.bookingConfirmed?.title)
      assertEquals("Custom Body", emailTemplate.bookingConfirmed?.body)
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

      assertEquals("v3/grants/$grantId/scheduling/configurations", pathCaptor.firstValue)
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
      val queryParamCaptor = argumentCaptor<ListConfigurationsQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePost<Response<Configuration>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )
      assertEquals("v3/grants/$grantId/scheduling/configurations", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Configuration::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(createConfigurationRequest), requestBodyCaptor.firstValue)
    }

    @Test
    fun `creating a configuration with name calls requests with the correct params`() {
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
      ).name("My Custom Scheduling Page").build()

      configurations.create(grantId, createConfigurationRequest)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<ListConfigurationsQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePost<Response<Configuration>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )
      assertEquals("v3/grants/$grantId/scheduling/configurations", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Configuration::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(createConfigurationRequest), requestBodyCaptor.firstValue)
    }

    @Test
    fun `updating a configuration calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(UpdateConfigurationRequest::class.java)
      val configId = "abc-123-configuration-id"
      val config = ConfigurationSchedulerSettings.Builder().minBookingNotice(120).build()
      val updateConfigurationRequest = UpdateConfigurationRequest.Builder().scheduler(config).build()

      configurations.update(grantId, configId, updateConfigurationRequest)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<ListConfigurationsQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePut<Response<Configuration>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )
      assertEquals("v3/grants/$grantId/scheduling/configurations/$configId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Configuration::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(updateConfigurationRequest), requestBodyCaptor.firstValue)
    }

    @Test
    fun `updating a configuration with name calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(UpdateConfigurationRequest::class.java)
      val configId = "abc-123-configuration-id"
      val updateConfigurationRequest = UpdateConfigurationRequest.Builder()
        .name("Updated Scheduling Page Name")
        .build()

      configurations.update(grantId, configId, updateConfigurationRequest)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<ListConfigurationsQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePut<Response<Configuration>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )
      assertEquals("v3/grants/$grantId/scheduling/configurations/$configId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Configuration::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(updateConfigurationRequest), requestBodyCaptor.firstValue)
    }

    @Test
    fun `finding a configuration calls requests with the correct params`() {
      val configId = "configuration-id"
      configurations.find(grantId, configId)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<ListConfigurationsQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<Response<Configuration>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )
      assertEquals("v3/grants/$grantId/scheduling/configurations/$configId", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Configuration::class.java), typeCaptor.firstValue)
    }

    @Test
    fun `destroying a configuration calls requests with the correct params`() {
      val configId = "configuration-id"
      configurations.destroy(grantId, configId)
      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<ListConfigurationsQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeDelete<DeleteResponse>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )
      assertEquals("v3/grants/$grantId/scheduling/configurations/$configId", pathCaptor.firstValue)
      assertEquals(DeleteResponse::class.java, typeCaptor.firstValue)
    }
  }
}
