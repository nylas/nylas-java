package com.nylas;

import java.time.Instant;
import java.util.*;

/**
 * Query builder for checking for calendar free/busy
 * @see <a href="https://developer.nylas.com/docs/api/#post/calendars/free-busy">Calendar Free or Busy</a>
 */
public class FreeBusyQuery {

	private Long startTime;
	private Long endTime;
	private List<String> emails;
	private List<FreeBusyCalendars> calendars;

	/**
	 * The timestamp for the beginning of the event
	 */
	public FreeBusyQuery startTime(Instant startTime) {
		this.startTime = startTime.getEpochSecond();
		return this;
	}

	/**
	 * The timestamp for the beginning of the event
	 */
	public FreeBusyQuery startTime(Long startTime) {
		this.startTime = startTime;
		return this;
	}

	/**
	 * The timestamp for the end of the event
	 */
	public FreeBusyQuery endTime(Instant endTime) {
		this.endTime = endTime.getEpochSecond();
		return this;
	}

	/**
	 * The timestamp for the end of the event
	 */
	public FreeBusyQuery endTime(Long endTime) {
		this.endTime = endTime;
		return this;
	}

	/**
	 * Emails on the same domain to check
	 */
	public FreeBusyQuery emails(String... emails) {
		this.emails = Arrays.asList(emails);
		return this;
	}

	/**
	 * Check account and calendar IDs for free/busy status
	 */
	public FreeBusyQuery calendars(FreeBusyCalendars... calendars) {
		this.calendars = Arrays.asList(calendars);
		return this;
	}

	/**
	 * Checks the validity of the availability query
	 * @return If the query is valid
	 */
	public boolean isValid() {
		return startTime != null && endTime != null && (emails != null || this.calendars != null);
	}

	/**
	 * Converts the availability query to a map
	 * @return The query as a map
	 */
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<>();
		Maps.putIfNotNull(map, "start_time", startTime);
		Maps.putIfNotNull(map, "end_time", endTime);
		Maps.putIfNotNull(map, "calendars", calendars);
		Maps.putValueOrDefault(map, "emails", emails, Collections.emptyList());
		return map;
	}
}
