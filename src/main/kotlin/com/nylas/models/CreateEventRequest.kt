package com.nylas.models

import com.squareup.moshi.Json

data class CreateEventRequest(
  @Json(name = "when")
  val whenObj: When = When.Time(0),
  @Json(name = "title")
  val title: String? = null,
  @Json(name = "description")
  val description: String? = null,
  @Json(name = "location")
  val location: String? = null,
  @Json(name = "participants")
  val participants: List<Participant>? = null,
  @Json(name = "busy")
  val busy: Boolean? = null,
  @Json(name = "conferencing")
  val conferencing: Conferencing? = null,
  @Json(name = "reminder_minutes")
  val reminderMinutes: String? = null,
  @Json(name = "reminder_method")
  val reminderMethod: ReminderMethod? = null,
  @Json(name = "metadata")
  val metadata: Map<String, String>? = null,
  @Json(name = "recurrence")
  val recurrence: List<String>? = null,
  @Json(name = "calendar_id")
  val calendarId: String? = null,
  @Json(name = "read_only")
  val readOnly: Boolean? = null,
  @Json(name = "round_robin_order")
  val roundRobinOrder: List<String>? = null,
  @Json(name = "visibility")
  val visibility: EvenVisibility? = null,
  @Json(name = "capacity")
  val capacity: Int? = null,
  @Json(name = "hide_participant")
  val hideParticipant: Boolean? = null,
) {

  sealed class When {
    data class Date(
      @Json(name = "date")
      val date: String,
    ) : When()

    data class Datespan(
      @Json(name = "start_date")
      val startDate: String,
      @Json(name = "end_date")
      val endDate: String,
    ) : When()

    data class Time(
      @Json(name = "time")
      val time: Int,
      @Json(name = "timezone")
      val timezone: String? = null,
    ) : When() {
      data class Builder(
        private val time: Int,
      ) {
        private var timezone: String? = null

        fun timezone(timezone: String) = apply { this.timezone = timezone }

        fun build() = Time(time, timezone)
      }
    }

    data class Timespan(
      @Json(name = "start_time")
      val startTime: Int,
      @Json(name = "end_time")
      val endTime: Int,
      @Json(name = "start_timezone")
      val startTimezone: String? = null,
      @Json(name = "end_timezone")
      val endTimezone: String? = null,
    ) : When() {
      data class Builder(
        private val startTime: Int,
        private val endTime: Int,
      ) {
        private var startTimezone: String? = null
        private var endTimezone: String? = null

        fun startTimezone(startTimezone: String) = apply { this.startTimezone = startTimezone }
        fun endTimezone(endTimezone: String) = apply { this.endTimezone = endTimezone }

        fun build() = Timespan(startTime, endTime, startTimezone, endTimezone)
      }
    }
  }

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
    data class Builder(
      private val email: String,
    ) {
      private var status: ParticipantStatus? = null
      private var name: String? = null
      private var phoneNumber: String? = null
      private var comment: String? = null

      fun status(status: ParticipantStatus) = apply { this.status = status }
      fun name(name: String) = apply { this.name = name }
      fun phoneNumber(phoneNumber: String) = apply { this.phoneNumber = phoneNumber }
      fun comment(comment: String) = apply { this.comment = comment }

      fun build() = Participant(email, status, name, phoneNumber, comment)
    }
  }

  sealed class Conferencing {
    data class Autocreate(
      @Json(name = "provider")
      val provider: ConferencingProvider,
      @Json(name = "autocreate")
      val autocreate: Map<String, Any> = emptyMap(),
    ) : Conferencing()

    data class Details(
      @Json(name = "provider")
      val provider: ConferencingProvider,
      @Json(name = "details")
      val details: Config,
    ) : Conferencing() {
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
        class Builder {
          private var meetingCode: String? = null
          private var password: String? = null
          private var url: String? = null
          private var pin: String? = null
          private var phone: List<String>? = null

          fun meetingCode(meetingCode: String) = apply { this.meetingCode = meetingCode }
          fun password(password: String) = apply { this.password = password }
          fun url(url: String) = apply { this.url = url }
          fun pin(pin: String) = apply { this.pin = pin }
          fun phone(phone: List<String>) = apply { this.phone = phone }

          fun build() = Config(meetingCode, password, url, pin, phone)
        }
      }
    }
  }

  data class Recurrence(
    @Json(name = "rrule")
    val rrule: List<String>,
    @Json(name = "timezone")
    val timezone: String,
  )

  // builder
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
    private var roundRobinOrder: List<String>? = null
    private var visibility: EvenVisibility? = null
    private var capacity: Int? = null
    private var hideParticipant: Boolean? = null

    fun title(title: String) = apply { this.title = title }
    fun description(description: String) = apply { this.description = description }
    fun location(location: String) = apply { this.location = location }
    fun participants(participants: List<Participant>) = apply { this.participants = participants }
    fun busy(busy: Boolean) = apply { this.busy = busy }
    fun conferencing(conferencing: Conferencing) = apply { this.conferencing = conferencing }
    fun reminderMinutes(reminderMinutes: String) = apply { this.reminderMinutes = reminderMinutes }
    fun reminderMethod(reminderMethod: ReminderMethod) = apply { this.reminderMethod = reminderMethod }
    fun metadata(metadata: Map<String, String>) = apply { this.metadata = metadata }
    fun recurrence(recurrence: List<String>) = apply { this.recurrence = recurrence }
    fun calendarId(calendarId: String) = apply { this.calendarId = calendarId }
    fun readOnly(readOnly: Boolean) = apply { this.readOnly = readOnly }
    fun roundRobinOrder(roundRobinOrder: List<String>) = apply { this.roundRobinOrder = roundRobinOrder }
    fun visibility(visibility: EvenVisibility) = apply { this.visibility = visibility }
    fun capacity(capacity: Int) = apply { this.capacity = capacity }
    fun hideParticipant(hideParticipant: Boolean) = apply { this.hideParticipant = hideParticipant }

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
      roundRobinOrder,
      visibility,
      capacity,
      hideParticipant,
    )
  }
}
