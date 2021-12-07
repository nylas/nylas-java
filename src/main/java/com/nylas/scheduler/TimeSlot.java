package com.nylas.scheduler;

import java.util.*;

public class TimeSlot {

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

	public Date getStartAsDate() {
		return start != null ? new Date(start * 1000) : null;
	}

	public Long getEnd() {
		return end;
	}

	public Date getEndAsDate() {
		return end != null ? new Date(end * 1000) : null;
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

	public void setStart(Date start) {
		this.start = start.getTime() / 1000;
	}

	public void setEnd(Long end) {
		this.end = end;
	}

	public void setEnd(Date end) {
		this.end = end.getTime() / 1000;
	}

	public void setEmails(List<String> emails) {
		this.emails = emails;
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
