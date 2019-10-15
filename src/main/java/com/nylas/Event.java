package com.nylas;

import java.util.List;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory;

public class Event extends RestfulModel {

	private String calendar_id;
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
	
	@Override
	public String toString() {
		return "Event [calendar_id=" + calendar_id + ", title=" + title + ", description=" + description + ", when="
				+ when + ", location=" + location + ", owner=" + owner + ", participants=" + participants + ", status="
				+ status + ", read_only=" + read_only + ", busy=" + busy + ", recurrence=" + recurrence
				+ ", master_event_id=" + master_event_id + ", original_start_time=" + original_start_time + "]";
	}

	public static class Recurrence {
		private String timezone;
		private List<String> rrule;
		
		@Override
		public String toString() {
			return "Recurrence [timezone=" + timezone + ", rrule=" + rrule + "]";
		}
	}
	
	public static interface When {
		String getObjectType();
	}
	
	public static class Time implements When {
		
		private Long time;
		
		public Long getTime() {
			return time;
		}

		public String getObjectType() {
			return "time";
		}

		@Override
		public String toString() {
			return "Time [time=" + time + "]";
		}
	}

	public static class Timespan implements When {
		
		private Long start_time;
		private Long end_time;
		
		public Long getStartTime() {
			return start_time;
		}

		public Long getEndTime() {
			return end_time;
		}

		public String getObjectType() {
			return "timespan";
		}

		@Override
		public String toString() {
			return "Timespan [start_time=" + start_time + ", end_time=" + end_time + "]";
		}
	}

	public static class Date implements When {
		
		private String date;
		
		public String getDate() {
			return date;
		}

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
		
		public String getStartDate() {
			return start_date;
		}

		public String getEndDate() {
			return end_date;
		}

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
