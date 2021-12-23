package com.nylas;

import java.time.Instant;

public class TimeSlot {
	private String status;
	private Long start_time;
	private Long end_time;
	// The availability endpoint uses these terms instead
	private Long start;
	private Long end;

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
		return "TimeSlot [status=" + status + ", start_time=" + getStartTime() + ", end_time=" + getEndTime() + "]";
	}
}
