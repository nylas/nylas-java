package com.nylas.scheduler;

import com.nylas.Maps;
import com.nylas.Model;

import java.util.*;

public class TimeSlot extends Model {

	private String account_id;
	private String calendar_id;
	private String host_name;
	private Long start;
	private Long end;
	private List<String> emails = new ArrayList<>();

	public String getAccountId() {
		return account_id;
	}

	public String getCalendarId() {
		return calendar_id;
	}

	public String getHostName() {
		return host_name;
	}

	public Long getStart() {
		return start;
	}

	public Long getEnd() {
		return end;
	}

	public List<String> getEmails() {
		return emails;
	}

	public void setAccountId(String accountId) {
		this.account_id = accountId;
	}

	public void setCalendarId(String calendarId) {
		this.calendar_id = calendarId;
	}

	public void setHostName(String hostName) {
		this.host_name = hostName;
	}

	public void setStart(Long start) {
		this.start = start;
	}

	public void setEnd(Long end) {
		this.end = end;
	}

	public void setEmails(List<String> emails) {
		this.emails = emails;
	}

	@Override
	protected Map<String, Object> getWritableFields(boolean creation) {
		Map<String, Object> params = new HashMap<>();
		Maps.putIfNotNull(params, "account_id", account_id);
		Maps.putIfNotNull(params, "calendar_id", calendar_id);
		Maps.putIfNotNull(params, "host_name", host_name);
		Maps.putIfNotNull(params, "start", start);
		Maps.putIfNotNull(params, "end", end);
		Maps.putIfNotNull(params, "emails", emails);
		return params;
	}

	@Override
	public String toString() {
		return "TimeSlot [" +
				"account_id='" + account_id + '\'' +
				", calendar_id='" + calendar_id + '\'' +
				", host_name='" + host_name + '\'' +
				", start=" + start +
				", end=" + end +
				", emails=" + emails +
				']';
	}
}
