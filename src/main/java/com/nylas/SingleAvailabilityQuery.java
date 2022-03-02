package com.nylas;

import java.util.List;
import java.util.Map;

public class SingleAvailabilityQuery extends AvailabilityQuery<SingleAvailabilityQuery> {

	private List<String> emails;
	private String roundRobin;

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

	public SingleAvailabilityQuery emails(List<String> emails) {
		this.emails = emails;
		return this;
	}

	public SingleAvailabilityQuery roundRobin(RoundRobin roundRobin) {
		this.roundRobin = roundRobin.getName();
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
		return map;
	}
}
