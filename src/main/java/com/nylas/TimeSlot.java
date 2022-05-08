package com.nylas;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class TimeSlot {
	private String status;
	private Long start_time;
	private Long end_time;
	// The availability endpoint uses these terms instead
	private Long start;
	private Long end;
	private Capacity capacity;
	private final List<String> emails = new ArrayList<>();

	public String getStatus() {
		return status;
	}

	public Instant getStartTime() {
		if(start_time != null) {
			return Instants.toNullableInstant(start_time);
		} else {
			return Instants.toNullableInstant(start);
		}
	}

	public Instant getEndTime() {
		if(end_time != null) {
			return Instants.toNullableInstant(end_time);
		} else {
			return Instants.toNullableInstant(end);
		}
	}

	public List<String> getEmails() {
		return emails;
	}

	// The Setters use the availability notation because that's the only
	// case someone would be modifying/setting start and end times
	public void setStartTime(Instant startTime) {
		this.start = startTime.getEpochSecond();
	}

	public void setEndTime(Instant endTime) {
		this.end = endTime.getEpochSecond();
	}

	public Boolean isFree() {
		return status != null && status.equals("free");
	}

	@Override
	public String toString() {
		return "TimeSlot [status=" + status + ", start_time=" + getStartTime() + ", end_time=" + getEndTime()
				+ ", emails=" + emails + "]";
	}

	public static class Capacity {
		private String event_id;
		private Integer current_capacity;
		private Integer max_capacity;

		public String getEventId() {
			return event_id;
		}

		public Integer getCurrentCapacity() {
			return current_capacity;
		}

		public Integer getMaxCapacity() {
			return max_capacity;
		}

		@Override
		public String toString() {
			return "Capacity [" +
					"event_id='" + event_id + '\'' +
					", current_capacity=" + current_capacity +
					", max_capacity=" + max_capacity +
					']';
		}
	}
}
