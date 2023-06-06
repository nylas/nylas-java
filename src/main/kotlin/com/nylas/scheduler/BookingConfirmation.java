package com.nylas.scheduler;

import java.util.Map;

public class BookingConfirmation {

	private String account_id;
	private String calendar_id;
	private String calendar_event_id;
	private String edit_hash;
	private String location;
	private String title;
	private String recipient_email;
	private String recipient_locale;
	private String recipient_name;
	private String recipient_tz;
	private Integer id;
	private Long start_time;
	private Long end_time;
	private Boolean is_confirmed;
	private Map<String, Object> additional_field_values;

	public String getAccountId() {
		return account_id;
	}

	public String getCalendarId() {
		return calendar_id;
	}

	public String getCalendarEventId() {
		return calendar_event_id;
	}

	public String getEditHash() {
		return edit_hash;
	}

	public String getLocation() {
		return location;
	}

	public String getTitle() {
		return title;
	}

	public String getRecipientEmail() {
		return recipient_email;
	}

	public String getRecipientLocale() {
		return recipient_locale;
	}

	public String getRecipientName() {
		return recipient_name;
	}

	public String getRecipientTz() {
		return recipient_tz;
	}

	public Integer getId() {
		return id;
	}

	public Long getStartTime() {
		return start_time;
	}

	public Long getEndTime() {
		return end_time;
	}

	public Boolean isConfirmed() {
		return is_confirmed;
	}

	public Map<String, Object> getAdditionalFieldValues() {
		return additional_field_values;
	}

	@Override
	public String toString() {
		return "BookingConfirmation [" +
				"account_id='" + account_id + '\'' +
				", calendar_id='" + calendar_id + '\'' +
				", calendar_event_id='" + calendar_event_id + '\'' +
				", edit_hash='" + edit_hash + '\'' +
				", location='" + location + '\'' +
				", title='" + title + '\'' +
				", recipient_email='" + recipient_email + '\'' +
				", recipient_locale='" + recipient_locale + '\'' +
				", recipient_name='" + recipient_name + '\'' +
				", recipient_tz='" + recipient_tz + '\'' +
				", id=" + id +
				", start_time=" + start_time +
				", end_time=" + end_time +
				", is_confirmed=" + is_confirmed +
				", additional_field_values=" + additional_field_values +
				']';
	}
}
