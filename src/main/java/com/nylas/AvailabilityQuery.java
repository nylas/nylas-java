package com.nylas;

import java.time.Instant;
import java.util.*;

abstract class AvailabilityQuery <Q extends AvailabilityQuery <Q>> {

	private Integer durationMinutes;
	private Integer intervalMinutes;
	private Integer buffer;
	private Boolean tentativeBusy;
	private Long startTime;
	private Long endTime;
	private List<FreeBusy> freeBusy;
	private List<OpenHours> openHours;
	protected List<FreeBusyCalendars> calendars;

	/**
	 * The total number of minutes the event should last
	 */
	public Q durationMinutes(int durationMinutes) {
		this.durationMinutes = durationMinutes;
		return self();
	}

	/**
	 * How many minutes it should check for availability
	 */
	public Q intervalMinutes(int intervalMinutes) {
		this.intervalMinutes = intervalMinutes;
		return self();
	}

	/**
	 * The amount of buffer time in minutes that you want around existing meetings
	 */
	public Q buffer(int buffer) {
		this.buffer = buffer;
		return self();
	}

	/**
	 * Availability of EWS calendars for tentative events
	 */
	public Q tentativeBusy(boolean tentativeBusy) {
		this.tentativeBusy = tentativeBusy;
		return self();
	}

	/**
	 * The timestamp for the beginning of the event
	 */
	public Q startTime(Instant startTime) {
		this.startTime = startTime.getEpochSecond();
		return self();
	}

	/**
	 * The timestamp for the beginning of the event
	 */
	public Q startTime(Long startTime) {
		this.startTime = startTime;
		return self();
	}

	/**
	 * The timestamp for the end of the event
	 */
	public Q endTime(Instant endTime) {
		this.endTime = endTime.getEpochSecond();
		return self();
	}

	/**
	 * The timestamp for the end of the event
	 */
	public Q endTime(Long endTime) {
		this.endTime = endTime;
		return self();
	}

	/**
	 * A list of {@link FreeBusy} data for users not in your organization
	 */
	public Q freeBusy(List<FreeBusy> freeBusy) {
		this.freeBusy = freeBusy;
		return self();
	}

	/**
	 * Additional times email accounts are available
	 */
	public Q openHours(List<OpenHours> openHours) {
		this.openHours = openHours;
		return self();
	}

	/**
	 * Check account and calendar IDs for free/busy status
	 * <br>
	 * Note, the mapping should be in the format of accountIds -> List of calendarIds
	 * @deprecated Replaced by {@link #calendars(FreeBusyCalendars...)}
	 */
	@Deprecated
	public Q calendars(List<Map<String, List<String>>> calendars) {
		this.calendars = new ArrayList<>();
		for(Map<String, List<String>> map : calendars) {
			for(Map.Entry<String, List<String>> entry : map.entrySet()) {
				this.calendars.add(new FreeBusyCalendars(entry.getKey(), entry.getValue()));
			}
		}
		return self();
	}

	/**
	 * Check account and calendar IDs for free/busy status
	 */
	public Q calendars(FreeBusyCalendars... calendars) {
		this.calendars = Arrays.asList(calendars);
		return self();
	}

	/**
	 * Checks the validity of the availability query
	 * @return If the query is valid
	 */
	public boolean isValid() {
		return durationMinutes != null &&
				intervalMinutes != null &&
				startTime != null &&
				endTime != null;
	}

	/**
	 * Converts the availability query to a map
	 * @return The query as a map
	 */
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<>();
		Maps.putIfNotNull(map, "duration_minutes", durationMinutes);
		Maps.putIfNotNull(map, "interval_minutes", intervalMinutes);
		Maps.putIfNotNull(map, "buffer", buffer);
		Maps.putIfNotNull(map, "tentative_busy", tentativeBusy);
		Maps.putIfNotNull(map, "start_time", startTime);
		Maps.putIfNotNull(map, "end_time", endTime);
		Maps.putIfNotNull(map, "open_hours", openHours);
		Maps.putIfNotNull(map, "calendars", calendars);
		Maps.putValueOrDefault(map, "free_busy", freeBusy, Collections.emptyList());
		return map;
	}

	// helper method for fluent builder style without type warnings
	@SuppressWarnings("unchecked")
	protected final Q self() {
		return (Q) this;
	}
}

