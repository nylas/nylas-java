package com.nylas;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Query builder for checking availability for a single meeting
 * @see <a href="https://developer.nylas.com/docs/api/#post/calendars/availability">Availability for a Single Meeting</a>
 */
public class SingleAvailabilityQuery extends AvailabilityQuery<SingleAvailabilityQuery> {

	private List<String> emails;
	private String roundRobin;
	private String eventCollectionId;

	/**
	 * Available round-robin options.
	 * <br>
	 * {@link #MAX_AVAILABILITY} will return all time slots where at least one person specified in free/busy, calendars, or emails is available.
	 * <br>
	 * {@code #MAX_FAIRNESS} will return time slots where the least recently booked 50% of people are available.
	 */
	public enum RoundRobin {
		MAX_AVAILABILITY("max-availability"),
		MAX_FAIRNESS("max-fairness"),

		;

		private final String name;

		RoundRobin(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	/**
	 * Emails on the same domain to check
	 */
	public SingleAvailabilityQuery emails(List<String> emails) {
		this.emails = emails;
		return this;
	}

	/**
	 * Finds available meeting times in a round-robin style. Unset returns collective availability.
	 */
	public SingleAvailabilityQuery roundRobin(RoundRobin roundRobin) {
		this.roundRobin = roundRobin.getName();
		return this;
	}

	/**
	 * Unique identifier for a collection of events that are created specific for group meeting
	 */
	public SingleAvailabilityQuery eventCollectionId(String eventCollectionId) {
		this.eventCollectionId = eventCollectionId;
		return this;
	}

	@Override
	public boolean isValid() {
		return super.isValid() && (emails != null || this.calendars != null);
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> map = super.toMap();
		Maps.putIfNotNull(map, "emails", emails);
		Maps.putIfNotNull(map, "round_robin", roundRobin);
		Maps.putIfNotNull(map, "event_collection_id", eventCollectionId);
		return map;
	}

	@Override
	StringJoiner missingParameters() {
		StringJoiner missingParams = super.missingParameters();
		if(emails == null && calendars == null) {
			missingParams.add("one of emails or calendars");
		}
		return missingParams;
	}
}
