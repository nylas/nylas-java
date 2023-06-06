package com.nylas.scheduler;

import com.nylas.Calendar;
import java.util.List;

public class AvailableCalendars {

	private String id;
	private String name;
	private String email;
	private List<Calendar> calendars;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public List<Calendar> getCalendars() {
		return calendars;
	}

	@Override
	public String toString() {
		return "AvailableCalendars [" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", email='" + email + '\'' +
				", calendars=" + calendars +
				']';
	}
}
