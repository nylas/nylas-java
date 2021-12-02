package com.nylas.scheduler;

import com.nylas.Maps;
import com.nylas.Model;

import java.util.HashMap;
import java.util.Map;

public class BookingConfirmation extends Model {

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

	public void setAccountId(String accountId) {
		this.account_id = accountId;
	}

	public void setCalendarId(String calendarId) {
		this.calendar_id = calendarId;
	}

	public void setCalendarEventId(String calendarEventId) {
		this.calendar_event_id = calendarEventId;
	}

	public void setEditHash(String editHash) {
		this.edit_hash = editHash;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setRecipientEmail(String recipientEmail) {
		this.recipient_email = recipientEmail;
	}

	public void setRecipientLocale(String recipientLocale) {
		this.recipient_locale = recipientLocale;
	}

	public void setRecipientName(String recipientName) {
		this.recipient_name = recipientName;
	}

	public void setRecipientTz(String recipientTz) {
		this.recipient_tz = recipientTz;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setStartTime(Long startTime) {
		this.start_time = startTime;
	}

	public void setEndTime(Long endTime) {
		this.end_time = endTime;
	}

	public void setIsConfirmed(Boolean isConfirmed) {
		this.is_confirmed = isConfirmed;
	}

	public void setAdditionalFieldValues(Map<String, Object> additionalFieldValues) {
		this.additional_field_values = additionalFieldValues;
	}

	@Override
	protected Map<String, Object> getWritableFields(boolean creation) {
		Map<String, Object> params = new HashMap<>();
		Maps.putIfNotNull(params, "account_id", account_id);
		Maps.putIfNotNull(params, "calendar_id", calendar_id);
		Maps.putIfNotNull(params, "calendar_event_id", calendar_event_id);
		Maps.putIfNotNull(params, "edit_hash", edit_hash);
		Maps.putIfNotNull(params, "location", location);
		Maps.putIfNotNull(params, "title", title);
		Maps.putIfNotNull(params, "recipient_email", recipient_email);
		Maps.putIfNotNull(params, "recipient_locale", recipient_locale);
		Maps.putIfNotNull(params, "recipient_name", recipient_name);
		Maps.putIfNotNull(params, "recipient_tz", recipient_tz);
		Maps.putIfNotNull(params, "id", id);
		Maps.putIfNotNull(params, "start_time", start_time);
		Maps.putIfNotNull(params, "end_time", end_time);
		Maps.putIfNotNull(params, "is_confirmed", is_confirmed);
		Maps.putIfNotNull(params, "additional_field_values", additional_field_values);
		return params;
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
