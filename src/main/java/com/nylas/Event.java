package com.nylas;

import java.time.Instant;
import java.time.LocalDate;
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
				+ ", status=" + status + ", read_only=" + read_only + ", busy=" + busy + ", recurrence=" + recurrence
				+ ", master_event_id=" + master_event_id + ", original_start_time=" + getOriginalStartTime() + "]";
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
		Maps.putIfNotNull(params, "recurrence", getRecurrence());
		return params;
	}

	public static class Recurrence {
		private String timezone;
		private List<String> rrule;
		
		@Override
		public String toString() {
			return "Recurrence [timezone=" + timezone + ", rrule=" + rrule + "]";
		}
	}
	
	public static interface When extends JsonObject {}
	
	public static class Time implements When {
		
		private long time;
		
		/** For deserialization only */ public Time() {}
		
		public Time(Instant time) {
			this.time = time.getEpochSecond();
		}
		
		public Instant getTime() {
			return Instant.ofEpochSecond(time);
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

	public static class Timespan implements When {
		
		private long start_time;
		private long end_time;

		/** For deserialization only */ public Timespan() {}

		public Timespan(Instant startTime, Instant endTime) {
			this.start_time = startTime.getEpochSecond();
			this.end_time = endTime.getEpochSecond();
		}

		public Instant getStartTime() {
			return Instant.ofEpochSecond(start_time);
		}

		public Instant getEndTime() {
			return Instant.ofEpochSecond(end_time);
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
