package com.nylas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FreeBusyCalendars {

	private String account_id;
	private List<String> calendar_ids = new ArrayList<>();

	public FreeBusyCalendars(String accountId, List<String> calendarIds) {
		this.account_id = accountId;
		this.calendar_ids = calendarIds;
	}

	public String getAccountId() {
		return account_id;
	}

	public void setAccountId(String accountId) {
		this.account_id = accountId;
	}

	public List<String> getCalendarIds() {
		return calendar_ids;
	}

	public void setCalendarIds(List<String> calendarIds) {
		this.calendar_ids = calendarIds;
	}

	public void addCalendarIds(String... calendarIds) {
		this.calendar_ids.addAll(Arrays.asList(calendarIds));
	}
}
