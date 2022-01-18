package com.nylas;

import java.time.Instant;
import java.util.List;

public class ConsecutiveAvailability {
	private List<String> emails;
	private long start_time;
	private long end_time;

	public List<String> getEmails() {
		return emails;
	}

	public Instant getStartTime() {
		return Instants.toNullableInstant(start_time);
	}

	public Instant getEndTime() {
		return Instants.toNullableInstant(end_time);
	}

	@Override
	public String toString() {
		return "ConsecutiveAvailability [emails=" + emails + ", start_time=" + getStartTime() + ", end_time=" + getEndTime() + "]";
	}
}
