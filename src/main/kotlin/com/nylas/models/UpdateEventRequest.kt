package com.nylas.models

import com.squareup.moshi.Json

/**
 * Class representation of a Nylas update event request
 */
data class UpdateEventRequest(
  /**
   * Representation of time and duration for events. When object can be in one of four formats (sub-objects):
   * - [UpdateEventRequest.When.Date]
   * - [UpdateEventRequest.When.Datespan]
   * - [UpdateEventRequest.When.Time]
   * - [UpdateEventRequest.When.Timespan]
   */
  @Json(name = "when")
  val whenObj: When? = null,
  /**
   * The title of the event
   */
  @Json(name = "title")
  val title: String? = null,
  /**
   * Description of the event.
   */
  @Json(name = "description")
  val description: String? = null,
  /**
   * Location of the event, such as a physical address or meeting room name.
   */
  @Json(name = "location")
  val location: String? = null,
  /**
   * List of participants invited to the event. Participants may also be rooms or resources.
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
   * - [UpdateEventRequest.Conferencing.Autocreate]
   * - [UpdateEventRequest.Conferencing.Details]
   */
  @Json(name = "conferencing")
  val conferencing: Conferencing? = null,
  /**
   * A list of reminders to send for the event. If left empty or omitted, the event uses the provider defaults.
   */
  @Json(name = "reminders")
  val reminders: Reminders? = null,
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
      val date: String? = null,
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
      val startDate: String? = null,
      /**
       * The end date in ISO 8601 format.
       * @see <a href="https://en.wikipedia.org/wiki/ISO_8601#Calendar_dates">ISO 8601</a>
       */
      @Json(name = "end_date")
      val endDate: String? = null,
    ) : When() {
      /**
       * Builder for [Datespan]
       */
      class Builder {
        private var startDate: String? = null
        private var endDate: String? = null

        /**
         * Set the start date in ISO 8601 format.
         * @see <a href="https://en.wikipedia.org/wiki/ISO_8601#Calendar_dates">ISO 8601</a>
         * @param startDate The start date in ISO 8601 format.
         * @return The builder instance.
         */
        fun startDate(startDate: String) = apply { this.startDate = startDate }

        /**
         * Set the end date in ISO 8601 format.
         * @see <a href="https://en.wikipedia.org/wiki/ISO_8601#Calendar_dates">ISO 8601</a>
         * @param endDate The end date in ISO 8601 format.
         * @return The builder instance.
         */
        fun endDate(endDate: String) = apply { this.endDate = endDate }

        /**
         * Build the [Datespan] object.
         * @return The [Datespan] object.
         */
        fun build() = Datespan(startDate, endDate)
      }
    }

    /**
     * Class representation of a specific point in time.
     * A meeting at 2pm would be represented as a time subobject.
     */
    data class Time(
      /**
       * A UNIX timestamp representing the time of occurrence.
       */
      @Json(name = "time")
      val time: Int? = null,
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
      class Builder {
        private var time: Int? = null
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
      val startTime: Int? = null,
      /**
       * The end time of the event.
       */
      @Json(name = "end_time")
      val endTime: Int? = null,
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
      class Builder {
        private var startTime: Int? = null
        private var endTime: Int? = null
        private var startTimezone: String? = null
        private var endTimezone: String? = null

        /**
         * Set the start time of the event.
         * @param startTime The start time of the event.
         * @return The builder.
         */
        fun startTime(startTime: Int) = apply { this.startTime = startTime }

        /**
         * Set the end time of the event.
         * @param endTime The end time of the event.
         * @return The builder.
         */
        fun endTime(endTime: Int) = apply { this.endTime = endTime }

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
   * @property name The participant's full name.
   * @property phoneNumber Participants phone number.
   * @property comment A comment by the participant.
   */
  data class Participant(
    @Json(name = "email")
    val email: String? = null,
    @Json(name = "name")
    val name: String? = null,
    @Json(name = "phone_number")
    val phoneNumber: String? = null,
    @Json(name = "comment")
    val comment: String? = null,
  ) {
    class Builder {
      private var email: String? = null
      private var name: String? = null
      private var phoneNumber: String? = null
      private var comment: String? = null

      /**
       * Set the participant's email address.
       * @param email The participant's email address.
       * @return The builder.
       */
      fun email(email: String) = apply { this.email = email }

      /**
       * Set the participant's full name.
       * @param name The participant's full name.
       * @return The builder.
       */
      fun name(name: String) = apply { this.name = name }

      /**
       * Set the participant's phone number.
       * @param phoneNumber The participant's phone number.
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
      fun build() = Participant(email, name, phoneNumber, comment)
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
      val provider: ConferencingProvider? = null,
      /**
       * Empty dict to indicate an intention to autocreate a video link.
       * Additional provider settings may be included in autocreate.settings, but Nylas does not validate these.
       */
      @Json(name = "autocreate")
      val autocreate: Map<String, Any>? = null,
    ) : Conferencing() {
      /**
       * Builder for [Autocreate].
       */
      class Builder {
        private var provider: ConferencingProvider? = null
        private var autocreate: Map<String, Any>? = null

        /**
         * Set the conferencing provider.
         * @param provider The conferencing provider.
         * @return The builder.
         */
        fun provider(provider: ConferencingProvider) = apply { this.provider = provider }

        /**
         * Set the autocreate settings.
         * @param autocreate Empty dict to indicate an intention to autocreate a video link.
         * Additional provider settings may be included in autocreate.settings, but Nylas does not validate these.
         * @return The builder.
         */
        fun autocreate(autocreate: Map<String, Any>) = apply { this.autocreate = autocreate }

        /**
         * Builds the [Autocreate] object.
         * @return [Autocreate] object.
         */
        fun build() = Autocreate(provider, autocreate)
      }
    }

    /**
     * Class representation of a conferencing details object
     */
    data class Details(
      /**
       * The conferencing provider.
       */
      @Json(name = "provider")
      val provider: ConferencingProvider? = null,
      /**
       * The conferencing details
       */
      @Json(name = "details")
      val details: Config? = null,
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

      /**
       * Builder for [Details].
       */
      class Builder {
        private var provider: ConferencingProvider? = null
        private var details: Config? = null

        /**
         * Set the conferencing provider.
         * @param provider The conferencing provider.
         * @return The builder.
         */
        fun provider(provider: ConferencingProvider) = apply { this.provider = provider }

        /**
         * Set the conferencing details.
         * @param details The conferencing details.
         * @return The builder.
         */
        fun details(details: Config) = apply { this.details = details }

        /**
         * Builds the [Details] object.
         * @return [Details] object.
         */
        fun build() = Details(provider, details)
      }
    }
  }

  /**
   * Class representing the reminders field of an event.
   */
  data class Reminders(
    /**
     * Whether to use the default reminders for the calendar.
     * When true, uses the default reminder settings for the calendar
     */
    @Json(name = "use_default")
    val useDefault: Boolean? = null,
    /**
     * A list of reminders for the event if useDefault is set to false.
     */
    @Json(name = "override")
    val override: List<ReminderOverride>? = null,
  )

  /**
   * Builder for [UpdateEventRequest].
   */
  class Builder {
    private var whenObj: When? = null
    private var title: String? = null
    private var description: String? = null
    private var location: String? = null
    private var participants: List<Participant>? = null
    private var busy: Boolean? = null
    private var conferencing: Conferencing? = null
    private var reminders: Reminders? = null
    private var metadata: Map<String, String>? = null
    private var recurrence: List<String>? = null
    private var calendarId: String? = null
    private var readOnly: Boolean? = null
    private var visibility: EventVisibility? = null
    private var capacity: Int? = null
    private var hideParticipant: Boolean? = null

    /**
     * Set the when object.
     * When is the representation of time and duration for events. When object can be in one of four formats (sub-objects):
     * - [When.Date]
     * - [When.Datespan]
     * - [When.Time]
     * - [When.Timespan]
     * @param whenObj The when object.
     * @return The builder.
     */
    fun setWhen(whenObj: When) = apply { this.whenObj = whenObj }

    /**
     * Update the title of the event.
     * @param title The title of the event.
     * @return The builder.
     */
    fun title(title: String) = apply { this.title = title }

    /**
     * Update the description of the event.
     * @param description Description of the event.
     * @return The builder.
     */
    fun description(description: String) = apply { this.description = description }

    /**
     * Update the location of the event.
     * @param location Location of the event, such as a physical address or meeting room name.
     * @return The builder.
     */
    fun location(location: String) = apply { this.location = location }

    /**
     * Update the list of participants invited to the event.
     * @param participants List of participants invited to the event. Participants may also be rooms or resources.
     * @return The builder.
     */
    fun participants(participants: List<Participant>) = apply { this.participants = participants }

    /**
     * Update the busy status of the event.
     * @param busy This value determines whether to show this event's time block as available on shared or public calendars.
     * @return The builder.
     */
    fun busy(busy: Boolean) = apply { this.busy = busy }

    /**
     * Update the conferencing details of the event.
     * Conferencing is a representation of conferencing details for events. Conferencing object can be in one of two formats (sub-objects):
     * - [Conferencing.Autocreate]
     * - [Conferencing.Details]
     * @param conferencing Representation of conferencing details for events.
     * @return The builder.
     */
    fun conferencing(conferencing: Conferencing) = apply { this.conferencing = conferencing }

    /**
     * Update the reminders of the event.
     * @param reminders A list of reminders to send for the event. If left empty or omitted, the event uses the provider defaults.
     * @return The builder.
     */
    fun reminders(reminders: Reminders) = apply { this.reminders = reminders }

    /**
     * Update the metadata of the event.
     * @param metadata A list of key-value pairs storing additional data.
     * @return The builder.
     */
    fun metadata(metadata: Map<String, String>) = apply { this.metadata = metadata }

    /**
     * Update the recurrence of the event.
     * @param recurrence An array of RRULE and EXDATE strings.
     * Please note that EXRULE, RDATE, and TZID are not supported.
     * @return The builder.
     */
    fun recurrence(recurrence: List<String>) = apply { this.recurrence = recurrence }

    /**
     * Update the calendar ID of the event.
     * @param calendarId Calendar ID of the event.
     * @return The builder.
     */
    fun calendarId(calendarId: String) = apply { this.calendarId = calendarId }

    /**
     * Update the read only status of the event.
     * @param readOnly If the event participants are able to edit the event.
     * @return The builder.
     */
    fun readOnly(readOnly: Boolean) = apply { this.readOnly = readOnly }

    /**
     * Update the visibility of the event.
     * @param visibility Sets the visibility for the event. The calendar default will be used if this field is omitted.
     * @return The builder.
     */
    fun visibility(visibility: EventVisibility) = apply { this.visibility = visibility }

    /**
     * Update the capacity of the event.
     * @param capacity Sets the maximum number of participants that may attend the event.
     * @return The builder.
     */
    fun capacity(capacity: Int) = apply { this.capacity = capacity }

    /**
     * Update the hide participant status of the event.
     * @param hideParticipant Whether participants of the event should be hidden.
     * @return The builder.
     */
    fun hideParticipant(hideParticipant: Boolean) = apply { this.hideParticipant = hideParticipant }

    /**
     * Builds the [UpdateEventRequest] object.
     * @return [UpdateEventRequest] object.
     */
    fun build() =
      UpdateEventRequest(
        whenObj,
        title,
        description,
        location,
        participants,
        busy,
        conferencing,
        reminders,
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
