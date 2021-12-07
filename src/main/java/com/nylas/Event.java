package com.nylas;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory;

public class Event extends AccountOwnedModel implements JsonObject {

	private String calendar_id;
	private String ical_uid;
	private String title;
	private String description;
	private When when;
	private String location;
	private String owner;
	private List<Participant> participants;
	private String status;
	private Boolean read_only;
	private Boolean busy;
	private Map<String, String> metadata;
	private Conferencing conferencing;
	private List<Notification> notifications;
	
	private Recurrence recurrence;
	
	private String master_event_id;
	private Long original_start_time;
	
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

	public List<Participant> getParticipants() {
		return participants;
	}

	public String getStatus() {
		return status;
	}

	public Boolean getReadOnly() {
		return read_only;
	}

	public Boolean getBusy() {
		return busy;
	}

	public Map<String, String> getMetadata() {
		return metadata;
	}

	public Conferencing getConferencing() {
		return conferencing;
	}

	public List<Notification> getNotifications() {
		return notifications;
	}

	public Recurrence getRecurrence() {
		return recurrence;
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
		return "Event [id=" + getId() + ", calendar_id=" + calendar_id + ", ical_uid=" + ical_uid + ", title=" + title
				+ ", when=" + when + ", location=" + location + ", owner=" + owner + ", participants=" + participants
				+ ", status=" + status + ", read_only=" + read_only + ", busy=" + busy + ", metadata=" + metadata
				+ ", recurrence=" + recurrence + ", master_event_id=" + master_event_id + ", conferencing" + conferencing
				+ ", original_start_time=" + getOriginalStartTime() + "]";
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setWhen(When when) {
		this.when = when;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setParticipants(List<Participant> participants) {
		this.participants = participants;
	}

	public void setBusy(Boolean busy) {
		this.busy = busy;
	}

	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}

	public void setConferencing(Conferencing conferencing) {
		this.conferencing = conferencing;
	}

	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}

	public void setRecurrence(Recurrence recurrence) {
		this.recurrence = recurrence;
	}

	@Override
	Map<String, Object> getWritableFields(boolean creation) {
		Map<String, Object> params = new HashMap<>();
		if (creation) {
			Maps.putIfNotNull(params, "calendar_id", getCalendarId());
		}
		Maps.putIfNotNull(params, "when", getWhen());
		Maps.putIfNotNull(params, "title", getTitle());
		Maps.putIfNotNull(params, "description", getDescription());
		Maps.putIfNotNull(params, "location", getLocation());
		Maps.putIfNotNull(params, "participants", getParticipants());
		Maps.putIfNotNull(params, "busy", getBusy());
		Maps.putIfNotNull(params, "metadata", getMetadata());
		Maps.putIfNotNull(params, "conferencing", getConferencing());
		Maps.putIfNotNull(params, "notifications", getNotifications());
		Maps.putIfNotNull(params, "recurrence", getRecurrence());
		return params;
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

	public static abstract class Notification {
		protected String type;
		protected int minutesBeforeEvent;

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
			return minutesBeforeEvent;
		}

		public void setMinutesBeforeEvent(int minutesBeforeEvent) {
			this.minutesBeforeEvent = minutesBeforeEvent;
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
			return "EmailNotification [type=" + type + ", body=" + body + ", subject=" + subject +
					", minutesBeforeEvent=" + minutesBeforeEvent + "]";
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
			return "SMSNotification [type=" + type + ", message=" + message
					+ ", minutesBeforeEvent=" + minutesBeforeEvent + "]";
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
			return "WebhookNotification [type=" + type +", url=" + url + ", payload=" + payload
					+ ", minutesBeforeEvent=" + minutesBeforeEvent + "]";
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
