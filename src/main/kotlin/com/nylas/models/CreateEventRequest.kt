package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representing a request to create an event.
 */
data class CreateEventRequest(
  /**
   * Representation of time and duration for events. When object can be in one of four formats (sub-objects):
   * - [CreateEventRequest.When.Date]
   * - [CreateEventRequest.When.Datespan]
   * - [CreateEventRequest.When.Time]
   * - [CreateEventRequest.When.Timespan]
   */
  @Json(name = "when")
  val whenObj: When,
  /**
   * Creates an event with the specified title.
   */
  @Json(name = "title")
  val title: String? = null,
  /**
   * Creates an event with the specified description. This value may contain more details about an event or an agenda.
   */
  @Json(name = "description")
  val description: String? = null,
  /**
   * Creates an event with the specified location.
   */
  @Json(name = "location")
  val location: String? = null,
  /**
   * Creates an event with the specified participants.
   */
  @Json(name = "participants")
  val participants: List<Participant>? = null,
  /**
   * This value determines whether to show this event's time block as available on shared or public calendars.
   */
  @Json(name = "busy")
  val busy: Boolean? = null,
  /**
   * Representation of conferencing details for events. Conferencing object can be in one of two formats (sub-objects):
   * - [CreateEventRequest.Conferencing.Autocreate]
   * - [CreateEventRequest.Conferencing.Details]
   */
  @Json(name = "conferencing")
  val conferencing: Conferencing? = null,
  /**
   * The number of minutes before the event start time when a user wants a reminder for this event.
   * Reminder minutes need to be entered in the following format: "[20]".
   */
  @Json(name = "reminder_minutes")
  val reminderMinutes: String? = null,
  /**
   * Method to remind the user about the event. (Google only).
   */
  @Json(name = "reminder_method")
  val reminderMethod: ReminderMethod? = null,
  /**
   *  A list of key-value pairs storing additional data.
   */
  @Json(name = "metadata")
  val metadata: Map<String, String>? = null,
  /**
   * An array of RRULE and EXDATE strings.
   * Please note that EXRULE, RDATE, and TZID are not supported.
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc5545#section-3.8.5">RFC-5545</a>
   */
  @Json(name = "recurrence")
  val recurrence: List<String>? = null,
  /**
   * Calendar ID of the event.
   */
  @Json(name = "calendar_id")
  val calendarId: String? = null,
  /**
   * If the event participants are able to edit the event.
   */
  @Json(name = "read_only")
  val readOnly: Boolean? = null,
  /**
   * Sets the visibility for the event. The calendar default will be used if this field is omitted.
   */
  @Json(name = "visibility")
  val visibility: EventVisibility? = null,
  /**
   * Sets the maximum number of participants that may attend the event.
   */
  @Json(name = "capacity")
  val capacity: Int? = null,
  /**
   * Whether participants of the event should be hidden.
   */
  @Json(name = "hide_participant")
  val hideParticipant: Boolean? = null,
) {

  /**
   * This sealed class represents the different types of event time configurations.
   */
  sealed class When {
    /**
     * Class representation of an entire day spans without specific times.
     * Your birthday and holidays would be represented as date subobjects.
     */
    data class Date(
      /**
       * Date of occurrence in ISO 8601 format.
       * @see <a href="https://en.wikipedia.org/wiki/ISO_8601#Calendar_dates">ISO 8601</a>
       */
      @Json(name = "date")
      val date: String,
    ) : When()

    /**
     * Class representation of a specific dates without clock-based start or end times.
     * A business quarter or academic semester would be represented as datespan subobjects.
     */
    data class Datespan(
      /**
       * The start date in ISO 8601 format.
       * @see <a href="https://en.wikipedia.org/wiki/ISO_8601#Calendar_dates">ISO 8601</a>
       */
      @Json(name = "start_date")
      val startDate: String,
      /**
       * The end date in ISO 8601 format.
       * @see <a href="https://en.wikipedia.org/wiki/ISO_8601#Calendar_dates">ISO 8601</a>
       */
      @Json(name = "end_date")
      val endDate: String,
    ) : When()

    /**
     * Class representation of a specific point in time.
     * A meeting at 2pm would be represented as a time subobject.
     */
    data class Time(
      /**
       * A UNIX timestamp representing the time of occurrence.
       */
      @Json(name = "time")
      val time: Int,
      /**
       * If timezone is present, then the value for time will be read with timezone.
       * Timezone using IANA formatted string. (e.g. "America/New_York")
       * @see <a href="https://en.wikipedia.org/wiki/List_of_tz_database_time_zones">List of tz database time zones</a>
       */
      @Json(name = "timezone")
      val timezone: String? = null,
    ) : When() {
      /**
       * Builder for [Time].
       * @property time A UNIX timestamp representing the time of occurrence.
       */
      data class Builder(
        private val time: Int,
      ) {
        private var timezone: String? = null

        /**
         * Set the timezone for the time.
         * @param timezone Timezone using IANA formatted string. (e.g. "America/New_York")
         * @see <a href="https://en.wikipedia.org/wiki/List_of_tz_database_time_zones">List of tz database time zones</a>
         * @return The builder.
         */
        fun timezone(timezone: String) = apply { this.timezone = timezone }

        /**
         * Builds the [Time] object.
         * @return [Time] object.
         */
        fun build() = Time(time, timezone)
      }
    }

    /**
     * Class representation of a time span with start and end times.
     * An hour lunch meeting would be represented as timespan subobjects.
     */
    data class Timespan(
      /**
       * The start time of the event.
       */
      @Json(name = "start_time")
      val startTime: Int,
      /**
       * The end time of the event.
       */
      @Json(name = "end_time")
      val endTime: Int,
      /**
       * The timezone of the start time.
       * Timezone using IANA formatted string. (e.g. "America/New_York")
       * @see <a href="https://en.wikipedia.org/wiki/List_of_tz_database_time_zones">List of tz database time zones</a>
       */
      @Json(name = "start_timezone")
      val startTimezone: String? = null,
      /**
       * The timezone of the end time.
       * Timezone using IANA formatted string. (e.g. "America/New_York")
       * @see <a href="https://en.wikipedia.org/wiki/List_of_tz_database_time_zones">List of tz database time zones</a>
       */
      @Json(name = "end_timezone")
      val endTimezone: String? = null,
    ) : When() {
      /**
       * Builder for [Timespan].
       * @property startTime The start time of the event.
       * @property endTime The end time of the event.
       */
      data class Builder(
        private val startTime: Int,
        private val endTime: Int,
      ) {
        private var startTimezone: String? = null
        private var endTimezone: String? = null

        /**
         * Set the timezone of the start time.
         * @param startTimezone Timezone using IANA formatted string. (e.g. "America/New_York")
         * @see <a href="https://en.wikipedia.org/wiki/List_of_tz_database_time_zones">List of tz database time zones</a>
         * @return The builder.
         */
        fun startTimezone(startTimezone: String) = apply { this.startTimezone = startTimezone }

        /**
         * Set the timezone of the end time.
         * @param endTimezone Timezone using IANA formatted string. (e.g. "America/New_York")
         * @see <a href="https://en.wikipedia.org/wiki/List_of_tz_database_time_zones">List of tz database time zones</a>
         * @return The builder.
         */
        fun endTimezone(endTimezone: String) = apply { this.endTimezone = endTimezone }

        /**
         * Builds the [Timespan] object.
         * @return [Timespan] object.
         */
        fun build() = Timespan(startTime, endTime, startTimezone, endTimezone)
      }
    }
  }

  /**
   * Class representation of an event participant.
   * @property email The participant's email address.
   * @property status Attendance status.
   * @property name The participant's full name.
   * @property phoneNumber Participants phone number.
   * @property comment A comment by the participant.
   */
  data class Participant(
    @Json(name = "email")
    val email: String,
    @Json(name = "status")
    val status: ParticipantStatus? = null,
    @Json(name = "name")
    val name: String? = null,
    @Json(name = "phone_number")
    val phoneNumber: String? = null,
    @Json(name = "comment")
    val comment: String? = null,
  ) {
    /**
     * Builder for [Participant].
     * @property email The participant's email address.
     */
    data class Builder(
      private val email: String,
    ) {
      private var status: ParticipantStatus? = null
      private var name: String? = null
      private var phoneNumber: String? = null
      private var comment: String? = null

      /**
       * Set the attendance status.
       * @param status Attendance status.
       * @return The builder.
       */
      fun status(status: ParticipantStatus) = apply { this.status = status }

      /**
       * Set the participant's full name.
       * @param name The participant's full name.
       * @return The builder.
       */
      fun name(name: String) = apply { this.name = name }

      /**
       * Set the participant's phone number.
       * @param phoneNumber Participants phone number.
       * @return The builder.
       */
      fun phoneNumber(phoneNumber: String) = apply { this.phoneNumber = phoneNumber }

      /**
       * Set a comment by the participant.
       * @param comment A comment by the participant.
       * @return The builder.
       */
      fun comment(comment: String) = apply { this.comment = comment }

      /**
       * Builds the [Participant] object.
       * @return [Participant] object.
       */
      fun build() = Participant(email, status, name, phoneNumber, comment)
    }
  }

  /**
   * This sealed class represents the different types of conferencing configurations.
   */
  sealed class Conferencing {
    /**
     * Class representation of a conferencing autocreate object
     */
    data class Autocreate(
      /**
       * The conferencing provider.
       */
      @Json(name = "provider")
      val provider: ConferencingProvider,
      /**
       * Empty dict to indicate an intention to autocreate a video link.
       * Additional provider settings may be included in autocreate.settings, but Nylas does not validate these.
       */
      @Json(name = "autocreate")
      val autocreate: Map<String, Any> = emptyMap(),
    ) : Conferencing()

    /**
     * Class representation of a conferencing details object
     */
    data class Details(
      /**
       * The conferencing provider.
       */
      @Json(name = "provider")
      val provider: ConferencingProvider,
      /**
       * The conferencing details
       */
      @Json(name = "details")
      val details: Config,
    ) : Conferencing() {
      /**
       * Class representation of a conferencing details config object
       * @property meetingCode The conferencing meeting code. Used for Zoom.
       * @property password The conferencing meeting password. Used for Zoom.
       * @property url The conferencing meeting URL.
       * @property pin The conferencing meeting pin. Used for Google Meet.
       * @property phone The conferencing meeting phone numbers. Used for Google Meet.
       */
      data class Config(
        @Json(name = "meeting_code")
        val meetingCode: String? = null,
        @Json(name = "password")
        val password: String? = null,
        @Json(name = "url")
        val url: String? = null,
        @Json(name = "pin")
        val pin: String? = null,
        @Json(name = "phone")
        val phone: List<String>? = null,
      ) {
        /**
         * Builder for [Config].
         */
        class Builder {
          private var meetingCode: String? = null
          private var password: String? = null
          private var url: String? = null
          private var pin: String? = null
          private var phone: List<String>? = null

          /**
           * Set the conferencing meeting code. Used for Zoom.
           */
          fun meetingCode(meetingCode: String) = apply { this.meetingCode = meetingCode }

          /**
           * Set the conferencing meeting password. Used for Zoom.
           */
          fun password(password: String) = apply { this.password = password }

          /**
           * Set the conferencing meeting URL.
           */
          fun url(url: String) = apply { this.url = url }

          /**
           * Set the conferencing meeting pin. Used for Google Meet.
           */
          fun pin(pin: String) = apply { this.pin = pin }

          /**
           * Set the conferencing meeting phone numbers. Used for Google Meet.
           */
          fun phone(phone: List<String>) = apply { this.phone = phone }

          /**
           * Builds the [Config] object.
           * @return [Config] object.
           */
          fun build() = Config(meetingCode, password, url, pin, phone)
        }
      }
    }
  }

  /**
   * Builder for [CreateEventRequest].
   * @property whenObj Representation of time and duration for events.
   * When object can be in one of four formats (sub-objects):
   * - [CreateEventRequest.When.Date]
   * - [CreateEventRequest.When.Datespan]
   * - [CreateEventRequest.When.Time]
   * - [CreateEventRequest.When.Timespan]
   */
  data class Builder(
    private val whenObj: When,
  ) {
    private var title: String? = null
    private var description: String? = null
    private var location: String? = null
    private var participants: List<Participant>? = null
    private var busy: Boolean? = null
    private var conferencing: Conferencing? = null
    private var reminderMinutes: String? = null
    private var reminderMethod: ReminderMethod? = null
    private var metadata: Map<String, String>? = null
    private var recurrence: List<String>? = null
    private var calendarId: String? = null
    private var readOnly: Boolean? = null
    private var visibility: EventVisibility? = null
    private var capacity: Int? = null
    private var hideParticipant: Boolean? = null

    /**
     * Set the event title.
     * @param title The event title.
     * @return The builder.
     */
    fun title(title: String) = apply { this.title = title }

    /**
     * Set the event description.
     * @param description The event description.
     * @return The builder.
     */
    fun description(description: String) = apply { this.description = description }

    /**
     * Set the event location.
     * @param location The event location.
     * @return The builder.
     */
    fun location(location: String) = apply { this.location = location }

    /**
     * Set the event participants.
     * @param participants The event participants.
     * @return The builder.
     */
    fun participants(participants: List<Participant>) = apply { this.participants = participants }

    /**
     * Set the event busy status.
     * This value determines whether to show this event's time block as available on shared or public calendars.
     * @param busy The event busy status.
     * @return The builder.
     */
    fun busy(busy: Boolean) = apply { this.busy = busy }

    /**
     * Set the event conferencing details.
     * Representation of conferencing details for events. Conferencing object can be in one of two formats (sub-objects):
     * - [CreateEventRequest.Conferencing.Autocreate]
     * - [CreateEventRequest.Conferencing.Details]
     * @param conferencing The event conferencing details.
     * @return The builder.
     */
    fun conferencing(conferencing: Conferencing) = apply { this.conferencing = conferencing }

    /**
     * Set the number of minutes before the event start time when a user wants a reminder for this event.
     * Reminder minutes need to be entered in the following format: "[20]".
     * @param reminderMinutes The number of minutes before the event start time when a user wants a reminder for this event.
     * @return The builder.
     */
    fun reminderMinutes(reminderMinutes: String) = apply { this.reminderMinutes = reminderMinutes }

    /**
     * Set the method to remind the user about the event. (Google only).
     * @param reminderMethod Method to remind the user about the event.
     * @return The builder.
     */
    fun reminderMethod(reminderMethod: ReminderMethod) = apply { this.reminderMethod = reminderMethod }

    /**
     * Set the metadata, which is a key-value pair storing additional data.
     * @param metadata The metadata.
     * @return The builder.
     */
    fun metadata(metadata: Map<String, String>) = apply { this.metadata = metadata }

    /**
     * Set a list of RRULE and EXDATE strings.
     * Please note that EXRULE, RDATE, and TZID are not supported.
     * @param recurrence An array of RRULE and EXDATE strings.
     * @return The builder.
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc5545#section-3.8.5">RFC-5545</a>
     */
    fun recurrence(recurrence: List<String>) = apply { this.recurrence = recurrence }

    /**
     * Set the calendar ID of the event.
     * @param calendarId The calendar ID.
     * @return The builder.
     */
    fun calendarId(calendarId: String) = apply { this.calendarId = calendarId }

    /**
     * Set whether the event participants are able to edit the event.
     * @param readOnly Whether the event participants are able to edit the event.
     * @return The builder.
     */
    fun readOnly(readOnly: Boolean) = apply { this.readOnly = readOnly }

    /**
     * Set the event visibility.
     * The calendar default will be used if this field is omitted.
     * @param visibility The event visibility.
     * @return The builder.
     */
    fun visibility(visibility: EventVisibility) = apply { this.visibility = visibility }

    /**
     * Set the maximum number of participants that may attend the event.
     * @param capacity The maximum number of participants that may attend the event.
     * @return The builder.
     */
    fun capacity(capacity: Int) = apply { this.capacity = capacity }

    /**
     * Set whether to hide the participant of the event.
     * @param hideParticipant Whether to hide the participant of the event.
     * @return The builder.
     */
    fun hideParticipant(hideParticipant: Boolean) = apply { this.hideParticipant = hideParticipant }

    /**
     * Builds the [CreateEventRequest] object.
     * @return [CreateEventRequest] object.
     */
    fun build() = CreateEventRequest(
      whenObj,
      title,
      description,
      location,
      participants,
      busy,
      conferencing,
      reminderMinutes,
      reminderMethod,
      metadata,
      recurrence,
      calendarId,
      readOnly,
      visibility,
      capacity,
      hideParticipant,
    )
  }
}
