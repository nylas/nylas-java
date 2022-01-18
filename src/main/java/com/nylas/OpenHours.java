package com.nylas;

import java.util.List;

public class OpenHours {

	private String timezone;
	private String start;
	private String end;
	private List<String> emails;
	private List<Integer> days;
	private final String object_type = "open_hours";

	public String getTimezone() {
		return timezone;
	}

	public String getStart() {
		return start;
	}

	public String getEnd() {
		return end;
	}

	public List<String> getEmails() {
		return emails;
	}

	public List<Integer> getDays() {
		return days;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public void setEmails(List<String> emails) {
		this.emails = emails;
	}

	public void setDays(List<Integer> days) {
		this.days = days;
	}

	@Override
	public String toString() {
		return "OpenHours [" +
				"timezone='" + timezone + '\'' +
				", start='" + start + '\'' +
				", end='" + end + '\'' +
				", emails=" + emails +
				", days=" + days +
				']';
	}
}
