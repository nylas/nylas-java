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

	public Q durationMinutes(int durationMinutes) {
		this.durationMinutes = durationMinutes;
		return self();
	}

	public Q intervalMinutes(int intervalMinutes) {
		this.intervalMinutes = intervalMinutes;
		return self();
	}

	public Q buffer(int buffer) {
		this.buffer = buffer;
		return self();
	}

	public Q tentativeBusy(boolean tentativeBusy) {
		this.tentativeBusy = tentativeBusy;
		return self();
	}

	public Q startTime(Instant startTime) {
		this.startTime = startTime.getEpochSecond();
		return self();
	}

	public Q endTime(Instant endTime) {
		this.endTime = endTime.getEpochSecond();
		return self();
	}

	public Q freeBusy(List<FreeBusy> freeBusy) {
		this.freeBusy = freeBusy;
		return self();
	}

	public Q openHours(List<OpenHours> openHours) {
		this.openHours = openHours;
		return self();
	}

	public Q calendars(FreeBusyCalendars... calendars) {
		this.calendars = Arrays.asList(calendars);
		return self();
	}

	public boolean isValid() {
		return durationMinutes != null &&
				intervalMinutes != null &&
				startTime != null &&
				endTime != null;
	}

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

