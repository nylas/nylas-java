package com.nylas;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Query builder for checking availability for multiple meetings
 * @see <a href="https://developer.nylas.com/docs/api/#post/calendars/availability/consecutive">Availability for Multiple Meetings</a>
 */
public class MultipleAvailabilityQuery extends AvailabilityQuery<MultipleAvailabilityQuery> {

	private List<List<String>> emails;

	/**
	 * Emails on the same domain to check
	 */
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

	@Override
	StringJoiner missingParameters() {
		StringJoiner missingParams = super.missingParameters();
		if(emails == null && calendars == null) {
			missingParams.add("one of emails or calendars");
		}
		return missingParams;
	}
}
