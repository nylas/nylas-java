package com.nylas;

import java.util.*;

public class FreeBusyQuery {

	private Long startTime;
	private Long endTime;
	private List<String> emails;
	private FreeBusyCalendars calendars;

	public FreeBusyQuery startTime(Long startTime) {
		this.startTime = startTime;
		return this;
	}

	public FreeBusyQuery endTime(Long endTime) {
		this.endTime = endTime;
		return this;
	}

	public FreeBusyQuery emails(String... emails) {
		this.emails = Arrays.asList(emails);
		return this;
	}

	public FreeBusyQuery calendars(FreeBusyCalendars calendars) {
		this.calendars = calendars;
		return this;
	}

	public boolean isValid() {
		return startTime != null && endTime != null && (emails != null || this.calendars != null);
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<>();
		Maps.putIfNotNull(map, "start_time", startTime);
		Maps.putIfNotNull(map, "end_time", endTime);
		Maps.putIfNotNull(map, "calendars", calendars);
		Maps.putValueOrDefault(map, "emails", emails, Collections.emptyList());
		return map;
	}
}
