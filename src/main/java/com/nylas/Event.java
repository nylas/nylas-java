package com.nylas;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory;

import static com.nylas.Validations.assertState;
import static com.nylas.Validations.nullOrEmpty;

public class Event extends AccountOwnedModel implements JsonObject {

	private String calendar_id;
	private String ical_uid;
	private String master_event_id;
	private String customer_event_id;
	private String event_collection_id;
	private String title;
	private String description;
	private String location;
	private String owner;
	private String status;
	private String organizer_email;
	private String organizer_name;
	private String visibility;
	private Integer capacity;
	private Boolean read_only;
	private Boolean busy;
	private Boolean hide_participants;
	private Long original_start_time;
	private When when;
	private Conferencing conferencing;
	private Recurrence recurrence;
	private Reminders reminders;
	private List<String> round_robin_order = new ArrayList<>();
	private List<Notification> notifications = new ArrayList<>();
	private List<Participant> participants = new ArrayList<>();
	private Map<String, String> metadata = new HashMap<>();
	private final Map<String, Object> modifiedFields = new HashMap<>();
	
	/** for deserialization only */ public Event() {} 
	
	public Event(String calendarId, When when) {
		this.calendar_id = calendarId;
		this.when = when;
	}

	@Override
	public String getObjectType() {
		return "event";
	}
	
	public String getCalendarId() {
		return calendar_id;
	}

	public String getIcalUid() {
		return ical_uid;
	}

	public String getEventCollectionId() {
		return event_collection_id;
	}
	
	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public When getWhen() {
		return when;
	}

	public String getLocation() {
		return location;
	}

	public String getOwner() {
		return owner;
	}

	public String getCustomerEventId() {
		return customer_event_id;
	}

	public String getOrganizerEmail() {
		return organizer_email;
	}

	public String getOrganizerName() {
		return organizer_name;
	}

	public String getVisibility() {
		return visibility;
	}

	public List<Participant> getParticipants() {
		return participants;
	}

	public String getStatus() {
		return status;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public Boolean getReadOnly() {
		return read_only;
	}

	public Boolean getBusy() {
		return busy;
	}

	public Boolean getHideParticipants() {
		return hide_participants;
	}

	public Map<String, String> getMetadata() {
		return metadata;
	}

	public Conferencing getConferencing() {
		return conferencing;
	}

	public List<String> getRoundRobinOrder() {
		return round_robin_order;
	}

	public List<Notification> getNotifications() {
		return notifications;
	}

	public Recurrence getRecurrence() {
		return recurrence;
	}

	public Reminders getReminders() {
		return reminders;
	}

	public String getMasterEventId() {
		return master_event_id;
	}

	public Instant getOriginalStartTime() {
		return Instants.toNullableInstant(original_start_time);
	}

	public static JsonAdapter.Factory getWhenJsonFactory() {
		return WHEN_JSON_FACTORY;
	}

	@Override
	public String toString() {
		return "Event [" +
				"id='" + getId() + '\'' +
				", calendar_id='" + calendar_id + '\'' +
				", ical_uid='" + ical_uid + '\'' +
				", master_event_id='" + master_event_id + '\'' +
				", event_collection_id='" + event_collection_id + '\'' +
				", title='" + title + '\'' +
				", description='" + description + '\'' +
				", location='" + location + '\'' +
				", owner='" + owner + '\'' +
				", status='" + status + '\'' +
				", capacity=" + capacity +
				", read_only=" + read_only +
				", busy=" + busy +
				", hide_participants=" + hide_participants +
				", original_start_time=" + getOriginalStartTime() +
				", when=" + when +
				", conferencing=" + conferencing +
				", recurrence=" + recurrence +
				", round_robin_order=" + round_robin_order +
				", notifications=" + notifications +
				", participants=" + participants +
				", visibility=" + visibility +
				", metadata=" + metadata +
				", customer_event_id=" + customer_event_id +
				']';
	}

	public void setEventCollectionId(String eventCollectionId) {
		this.event_collection_id = eventCollectionId;
		this.modifiedFields.put("event_collection_id", this.event_collection_id);
	}

	public void setTitle(String title) {
		this.title = title;
		this.modifiedFields.put("title", this.title);
	}

	public void setDescription(String description) {
		this.description = description;
		this.modifiedFields.put("description", this.description);
	}

	public void setWhen(When when) {
		this.when = when;
		this.modifiedFields.put("when", this.when);
	}

	public void setLocation(String location) {
		this.location = location;
		this.modifiedFields.put("location", this.location);
	}

	public void setCustomerEventId(String customerEventId) {
		this.customer_event_id = customerEventId;
		this.modifiedFields.put("customer_event_id", this.customer_event_id);
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
		this.modifiedFields.put("capacity", this.capacity);
	}

	public void setParticipants(List<Participant> participants) {
		this.participants = participants;
		this.modifiedFields.put("participants", this.participants);
	}

	public void setBusy(Boolean busy) {
		this.busy = busy;
		this.modifiedFields.put("busy", this.busy);
	}

	public void setHideParticipants(Boolean hideParticipants) {
		this.hide_participants = hideParticipants;
		this.modifiedFields.put("hide_participants", this.hide_participants);
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
		this.modifiedFields.put("visibility", this.visibility);
	}

	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
		this.modifiedFields.put("metadata", this.metadata);
	}

	public void setConferencing(Conferencing conferencing) {
		this.conferencing = conferencing;
		this.modifiedFields.put("conferencing", this.conferencing);
	}

	public void setRoundRobinOrder(List<String> roundRobinOrder) {
		this.round_robin_order = roundRobinOrder;
		this.modifiedFields.put("round_robin_order", this.round_robin_order);
	}

	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
		this.modifiedFields.put("notifications", this.notifications);
	}

	public void setRecurrence(Recurrence recurrence) {
		this.recurrence = recurrence;
		this.modifiedFields.put("recurrence", this.recurrence);
	}

	public void setReminders(Reminders reminders) {
		this.reminders = reminders;
		this.modifiedFields.put("reminders", this.reminders);
	}

	/**
	 * Add single metadata key-value pair to the event
	 * @param key The key of the metadata entry
	 * @param value The value of the metadata entry
	 */
	public void addMetadata(String key, String value) {
		this.metadata.put(key, value);
		this.modifiedFields.put("metadata", this.metadata);
	}

	/**
	 * Remove all metadata from the event
	 */
	public void clearMetadata() {
		this.metadata.clear();
	}

	/**
	 * Add one (or many) notifications to the event
	 * @param notifications The notification(s) to append to the event's notification list
	 */
	public void addNotification(Notification... notifications) {
		if(this.notifications == null) {
			this.notifications = new ArrayList<>();
		}
		this.notifications.addAll(Arrays.asList(notifications));
		this.modifiedFields.put("notifications", this.notifications);
	}

	/**
	 * Remove all notifications from the event
	 */
	public void clearNotifications() {
		this.notifications.clear();
	}

	/**
	 * Add one (or many) participants to the event
	 * @param participants The participant(s) to append to the event's participant list
	 */
	public void addParticipants(Participant... participants) {
		this.participants.addAll(Arrays.asList(participants));
		this.modifiedFields.put("participants", serializeParticipants(false));
	}

	/**
	 * Remove all participants from the event
	 */
	public void clearParticipants() {
		this.participants.clear();
	}

	/**
	 * Checks the validity of the event
	 * @return If the event is valid
	 */
	public boolean isValid() {
 		return conferencing == null || conferencing.getAutocreate() == null || conferencing.getDetails() == null &&
				(this.capacity == null || capacity == -1 || nullOrEmpty(this.participants) || this.participants.size() <= this.capacity);
	}

	void validate() {
		assertState(conferencing == null || conferencing.getAutocreate() == null || conferencing.getDetails() == null,
				"Cannot set both 'details' and 'autocreate' in conferencing object.");
		assertState(this.capacity == null || capacity == -1 || nullOrEmpty(this.participants) || this.participants.size() <= this.capacity,
				"The number of participants in the event exceeds the set capacity");
	}

	protected void setId(String id) {
		this.id = id;
	}

	@Override
	protected Map<String, Object> getWritableFields(boolean creation) {
		if (!creation) {
			return modifiedFields;
		}

		Map<String, Object> params = new HashMap<>();

		// Reminders, when creating an event, need to be included in the main object
		if(reminders != null && reminders.reminder_minutes != null && reminders.reminder_method != null) {
			params.put("reminder_minutes", String.format("[%d]", reminders.reminder_minutes));
			params.put("reminder_method", reminders.reminder_method);
		}

		Maps.putIfNotNull(params, "calendar_id", getCalendarId());
		Maps.putIfNotNull(params, "when", getWhen());
		Maps.putIfNotNull(params, "title", getTitle());
		Maps.putIfNotNull(params, "description", getDescription());
		Maps.putIfNotNull(params, "location", getLocation());
		Maps.putIfNotNull(params, "participants", serializeParticipants(true));
		Maps.putIfNotNull(params, "busy", getBusy());
		Maps.putIfNotNull(params, "metadata", getMetadata());
		Maps.putIfNotNull(params, "conferencing", getConferencing());
		Maps.putIfNotNull(params, "notifications", getNotifications());
		Maps.putIfNotNull(params, "recurrence", getRecurrence());
		Maps.putIfNotNull(params, "hide_participants", getHideParticipants());
		Maps.putIfNotNull(params, "visibility", getVisibility());
		Maps.putIfNotNull(params, "customer_event_id", getCustomerEventId());
		return params;
	}

	private List<Map<String, Object>> serializeParticipants(boolean creation) {
		if(this.participants == null || this.participants.isEmpty()) {
			return Collections.emptyList();
		}

		return this.participants.stream()
				.map(participant -> participant.getWritableFields(creation))
				.collect(Collectors.toList());
	}

	public static class Recurrence {
		private String timezone;
		private List<String> rrule;
		
		/** For deserialization only */ public Recurrence() {}
		
		public Recurrence(String timezone, List<String> rrule) {
			this.timezone = timezone;
			this.rrule = rrule;
		}
		
		public String getTimezone() {
			return timezone;
		}

		public List<String> getRrule() {
			return rrule;
		}

		@Override
		public String toString() {
			return "Recurrence [timezone=" + timezone + ", rrule=" + rrule + "]";
		}
	}

	public static class Conferencing {
		private String provider;
		private Details details;
		private Autocreate autocreate;

		/**
		 * Enumeration containing the different notification types
		 */
		public enum ConferencingProviders {
			ZOOM("Zoom Meeting"),
			GOOGLE_MEET("Google Meet"),
			MS_TEAMS("Microsoft Teams"),
			WEBEX("WebEx"),
			GOTOMEETING("GoToMeeting"),

			;

			private final String name;

			ConferencingProviders(String name) {
				this.name = name;
			}

			public String getName() {
				return name;
			}
		}

		/** For deserialization only */ public Conferencing() {}

		public String getProvider() {
			return provider;
		}

		public void setProvider(ConferencingProviders provider) {
			this.provider = provider.getName();
		}

		/**
		 * It is recommended to use the setter with the enumerated values instead.
		 */
		public void setProvider(String provider) {
			this.provider = provider;
		}

		public Details getDetails() {
			return details;
		}

		public void setDetails(Details details) {
			this.details = details;
		}

		public Autocreate getAutocreate() {
			return autocreate;
		}

		public void setAutocreate(Autocreate autocreate) {
			this.autocreate = autocreate;
		}

		@Override
		public String toString() {
			return String.format("Conferencing [provider=%s, details=%s, autocreate=%s]",
					provider, details, autocreate);
		}

		public static class Details {
			private String url;
			private String password;
			private String pin;
			private String meeting_code;
			private List<String> phone;

			public String getUrl() {
				return url;
			}

			public void setUrl(String url) {
				this.url = url;
			}

			public String getPassword() {
				return password;
			}

			public void setPassword(String password) {
				this.password = password;
			}

			public String getPin() {
				return pin;
			}

			public void setPin(String pin) {
				this.pin = pin;
			}

			public String getMeetingCode() {
				return meeting_code;
			}

			public void setMeetingCode(String meetingCode) {
				this.meeting_code = meetingCode;
			}

			public List<String> getPhone() {
				return phone;
			}

			public void setPhone(List<String> phone) {
				this.phone = phone;
			}

			@Override
			public String toString() {
				return String.format("Details [url=%s, password=%s, pin=%s, meeting_code=%s, phone=%s]",
						url, password, pin, meeting_code, phone);
			}
		}

		public static class Autocreate {
			private Map<String, String> settings;

			public Map<String, String> getSettings() {
				return settings;
			}

			public void setSettings(Map<String, String> settings) {
				this.settings = settings;
			}

			@Override
			public String toString() {
				return String.format("Autocreate [settings=%s]", settings);
			}
		}
	}

	public static class Reminders {
		private final Integer reminder_minutes;
		private final String reminder_method;

		/**
		 * Enumeration containing the reminder methods
		 */
		public enum ReminderMethod {
			EMAIL,
			POPUP,
			DISPLAY,
			SOUND,

			;

			@Override
			public String toString() {
				return super.toString().toLowerCase();
			}
		}

		public Reminders(int reminderMinutes, ReminderMethod reminderMethod) {
			this.reminder_minutes = reminderMinutes;
			this.reminder_method = reminderMethod.toString();
		}

		public Integer getReminderMinutes() {
			return reminder_minutes;
		}

		public ReminderMethod getReminderMethod() {
			try {
				return ReminderMethod.valueOf(reminder_method.toUpperCase());
			} catch (IllegalArgumentException | NullPointerException e) {
				return null;
			}
		}

		@Override
		public String toString() {
			return "Reminders [reminderMinutes=" + getReminderMinutes() + ", reminderMethod=" + getReminderMethod() + "]";
		}
	}

	public static abstract class Notification {
		private final String type;
		private int minutes_before_event;

		/**
		 * For deserialization only
		 */
		public Notification(String type) {
			this.type = type;
		}

		public String getType() {
			return type;
		}

		public int getMinutesBeforeEvent() {
			return minutes_before_event;
		}

		public void setMinutesBeforeEvent(int minutesBeforeEvent) {
			this.minutes_before_event = minutesBeforeEvent;
		}
	}

	public static class EmailNotification extends Notification {
		private String body;
		private String subject;

		/**
		 * For deserialization only
		 */
		public EmailNotification() {
			super("email");
		}

		public String getBody() {
			return body;
		}

		public void setBody(String body) {
			this.body = body;
		}

		public String getSubject() {
			return subject;
		}

		public void setSubject(String subject) {
			this.subject = subject;
		}

		@Override
		public String toString() {
			return "EmailNotification [type=" + getType() + ", body=" + body + ", subject=" + subject +
					", minutesBeforeEvent=" + getMinutesBeforeEvent() + "]";
		}
	}

	public static class SMSNotification extends Notification {
		private String message;

		/**
		 * For deserialization only
		 */
		public SMSNotification() {
			super("sms");
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		@Override
		public String toString() {
			return "SMSNotification [type=" + getType() + ", message=" + message
					+ ", minutesBeforeEvent=" + getMinutesBeforeEvent() + "]";
		}
	}

	public static class WebhookNotification extends Notification {
		private String url;
		private String payload;

		/**
		 * For deserialization only
		 */
		public WebhookNotification() {
			super("webhook");
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getPayload() {
			return payload;
		}

		public void setPayload(String payload) {
			this.payload = payload;
		}

		@Override
		public String toString() {
			return "WebhookNotification [type=" + getType() +", url=" + url + ", payload=" + payload
					+ ", minutesBeforeEvent=" + getMinutesBeforeEvent() + "]";
		}
	}

	public static final JsonAdapter.Factory EVENT_NOTIFICATION_JSON_FACTORY
			= PolymorphicJsonAdapterFactory.of(Event.Notification.class, "type")
			.withSubtype(Event.EmailNotification.class, "email")
			.withSubtype(Event.SMSNotification.class, "sms")
			.withSubtype(Event.WebhookNotification.class, "webhook");

	public static interface When extends JsonObject {}

	/**
	 * A single moment in time.
	 * Commonly used for reminders or alarms.
	 */
	public static class Time implements When {
		
		private long time;
		
		// used only for json serialization
		private String timezone;
		
		/** For deserialization only */ public Time() {}
		
		/**
		 * Construct a Time instance from the given Instant.
		 * This Time instance will have no timezone.
		 */
		public Time(Instant time) {
			this.time = time.getEpochSecond();
		}
		
		/**
		 * Construct a Time instance from the given ZonedDateTime
		 * Note that the given ZonedDateTime must have a ZoneId with a name listed in the IANA time zone database.
		 * Java ZoneOffset objects (including ZoneOffset.UTC) only have numerical ids and so are invalid.
		 * The set of legal ZoneId's known by this JVM may not match exactly with the set known by the server.
		 * 
		 * @throws IllegalArgumentException if the timezone is not a recognized time zone in this JVM's list
		 */
		public Time(ZonedDateTime time) {
			this.time = time.toEpochSecond();
			timezone = time.getZone().getId();
			
			if (!ZoneId.getAvailableZoneIds().contains(timezone)) {
				throw new IllegalArgumentException("Time zone \"" + timezone + "\" is not recognized"
						+ " as a named IANA time zone");
			}
		}
		
		/**
		 * Construct a Time instance from the given time and timezone.
		 * The time field represents a unix timestamp: number of seconds since 1970-01-01 UTC.
		 * The timezone field can be null or a name from the IANA time zone database.
		 * This constructor does not check for valid timezone. 
		 */
		public Time(long time, String timezone) {
			this.time = time;
			this.timezone = timezone;
		}
		
		/**
		 * Get the time as an Instant, without timezone information
		 */
		public Instant getTime() {
			return Instant.ofEpochSecond(time);
		}

		public String getTimezone() {
			return timezone;
		}

		@Override
		public String getObjectType() {
			return "time";
		}

		@Override
		public String toString() {
			return "Time [time=" + getTime() + "]";
		}
	}

	/**
	 * A span of time with a specific beginning and end time.
	 * Commonly used for a meeting or event with a start and end time.
	 */
	public static class Timespan implements When {
		
		private long start_time;
		private long end_time;
		
		// used only for json serialization
		private String start_timezone;
		
		// used only for json serialization
		private String end_timezone;

		/** For deserialization only */ public Timespan() {}

		/**
		 * Construct a Timespan instance from the given Instants.
		 * This Timespan instance will have no timezone.
		 */
		public Timespan(Instant startTime, Instant endTime) {
			this.start_time = startTime.getEpochSecond();
			this.end_time = endTime.getEpochSecond();
		}
		
		/**
		 * Construct a Timespan instance from the given ZonedDateTimes.
		 * Note that the given ZonedDateTimes must have ZoneId with names listed in the IANA time zone database.
		 * Java ZoneOffset objects (including ZoneOffset.UTC) only have numerical ids and so are invalid.
		 * The set of legal ZoneId's known by this JVM may not match exactly with the set known by the server.
		 * 
		 * @throws IllegalArgumentException if the timezones are not recognized time zones in this JVM's list
		 */
		public Timespan(ZonedDateTime startTime, ZonedDateTime endTime) {
			this.start_time = startTime.toEpochSecond();
			start_timezone = startTime.getZone().getId();
			
			if (!ZoneId.getAvailableZoneIds().contains(start_timezone)) {
				throw new IllegalArgumentException("Start time zone \"" + start_timezone + "\" is not recognized"
						+ " as a named IANA time zone");
			}
			
			this.end_time = endTime.toEpochSecond();
			end_timezone = endTime.getZone().getId();
			
			if (!ZoneId.getAvailableZoneIds().contains(end_timezone)) {
				throw new IllegalArgumentException("End time zone \"" + end_timezone + "\" is not recognized"
						+ " as a named IANA time zone");
			}
		}

		/**
		 * Construct a Timespan instance from the given times and timezones.
		 * The start and end time fields represent unix timestamps: number of seconds since 1970-01-01 UTC.
		 * The start and end timezone fields can be null or names from the IANA time zone database.
		 * This constructor does not check for valid timezones.
		 */
		public Timespan(long startTime, String startTimezone, long endTime, String endTimezone) {
			this.start_time = startTime;
			this.start_timezone = startTimezone;
			this.end_time = endTime;
			this.end_timezone = endTimezone;
		}
		
		/**
		 * Get the start time as an Instant, without timezone information
		 */
		public Instant getStartTime() {
			return Instant.ofEpochSecond(start_time);
		}

		public String getStartTimezone() {
			return start_timezone;
		}

		/**
		 * Get the end time as an Instant, without timezone information
		 */
		public Instant getEndTime() {
			return Instant.ofEpochSecond(end_time);
		}

		public String getEndTimezone() {
			return end_timezone;
		}

		@Override
		public String getObjectType() {
			return "timespan";
		}

		@Override
		public String toString() {
			return "Timespan [start_time=" + getStartTime() + ", end_time=" + getEndTime() + "]";
		}
	}

	/**
	 * A specific date for an event, without a clock-based start or end time.
	 * Examples are birthdays and holidays.
	 */
	public static class Date implements When {
		
		private String date;

		/** For deserialization only */ public Date() {}

		public Date(LocalDate date) {
			this.date = date.toString();
		}

		public LocalDate getDate() {
			return LocalDate.parse(date);
		}

		@Override
		public String getObjectType() {
			return "date";
		}

		@Override
		public String toString() {
			return "Date [date=" + date + "]";
		}
	}

	/**
	 * A span of entire days without specific times.
	 * Examples are a business quarter or academic semester.
	 */
	public static class Datespan implements When {
		
		private String start_date;
		private String end_date;
		
		/** For deserialization only */ public Datespan() {}

		public Datespan(LocalDate startDate, LocalDate endDate) {
			this.start_date = startDate.toString();
			this.end_date = endDate.toString();
		}

		public LocalDate getStartDate() {
			return LocalDate.parse(start_date);
		}

		public LocalDate getEndDate() {
			return LocalDate.parse(end_date);
		}

		@Override
		public String getObjectType() {
			return "datespan";
		}

		@Override
		public String toString() {
			return "Datespan [start_date=" + start_date + ", end_date=" + end_date + "]";
		}
	}
	
	public static final JsonAdapter.Factory WHEN_JSON_FACTORY
		= PolymorphicJsonAdapterFactory.of(Event.When.class, "object")
			.withSubtype(Event.Time.class, "time")
			.withSubtype(Event.Timespan.class, "timespan")
			.withSubtype(Event.Date.class, "date")
			.withSubtype(Event.Datespan.class, "datespan");

}
