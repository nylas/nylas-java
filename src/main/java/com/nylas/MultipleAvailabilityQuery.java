package com.nylas;

import java.util.List;
import java.util.Map;

public class MultipleAvailabilityQuery extends AvailabilityQuery<MultipleAvailabilityQuery> {

	private List<List<String>> emails;

	public MultipleAvailabilityQuery emails(List<List<String>> emails) {
		this.emails = emails;
		return this;
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> map = super.toMap();
		Maps.putIfNotNull(map, "emails", emails);
		return map;
	}

	@Override
	public boolean isValid() {
		return super.isValid() && (emails != null || this.calendars != null);
	}
}
