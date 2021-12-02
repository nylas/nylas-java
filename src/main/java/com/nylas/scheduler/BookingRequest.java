package com.nylas.scheduler;

import com.nylas.Maps;
import com.nylas.Model;

import java.util.*;

public class BookingRequest extends Model {

	private String name;
	private String email;
	private String locale;
	private String page_hostname;
	private String replaces_booking_hash;
	private String timezone;
	private TimeSlot slot;
	private Map<String, Object> additional_values;
	private List<String> additional_emails = new ArrayList<>();;

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getLocale() {
		return locale;
	}

	public String getPageHostname() {
		return page_hostname;
	}

	public String getReplacesBookingHash() {
		return replaces_booking_hash;
	}

	public String getTimezone() {
		return timezone;
	}

	public TimeSlot getSlot() {
		return slot;
	}

	public Map<String, Object> getAdditionalValues() {
		return additional_values;
	}

	public List<String> getAdditionalEmails() {
		return additional_emails;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public void setPageHostname(String pageHostname) {
		this.page_hostname = pageHostname;
	}

	public void setReplacesBookingHash(String replacesBookingHash) {
		this.replaces_booking_hash = replacesBookingHash;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public void setSlot(TimeSlot slot) {
		this.slot = slot;
	}

	public void setAdditionalValues(Map<String, Object> additionalValues) {
		this.additional_values = additionalValues;
	}

	public void setAdditionalEmails(List<String> additionalEmails) {
		this.additional_emails = additionalEmails;
	}

	@Override
	public Map<String, Object> getWritableFields(boolean creation) {
		Map<String, Object> params = new HashMap<>();

		Map<String, Object> additionalValues = this.additional_values;
		if(additionalValues == null) {
			additionalValues = Collections.emptyMap();
		}

		Maps.putIfNotNull(params, "name", name);
		Maps.putIfNotNull(params, "email", email);
		Maps.putIfNotNull(params, "locale", locale);
		Maps.putIfNotNull(params, "page_hostname", page_hostname);
		Maps.putIfNotNull(params, "replaces_booking_hash", replaces_booking_hash);
		Maps.putIfNotNull(params, "timezone", timezone);
		Maps.putIfNotNull(params, "slot", slot.getWritableFields(true));
		Maps.putIfNotNull(params, "additional_values", additionalValues);
		Maps.putIfNotNull(params, "additional_emails", additional_emails);
		return params;
	}

	@Override
	public String toString() {
		return "BookingRequest [" +
				"name='" + name + '\'' +
				", email='" + email + '\'' +
				", locale='" + locale + '\'' +
				", page_hostname='" + page_hostname + '\'' +
				", replaces_booking_hash='" + replaces_booking_hash + '\'' +
				", timezone='" + timezone + '\'' +
				", slot=" + slot +
				", additional_values=" + additional_values +
				", additional_emails=" + additional_emails +
				']';
	}
}
