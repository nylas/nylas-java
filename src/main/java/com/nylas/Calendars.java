package com.nylas;

import java.io.IOException;
import java.util.List;

/**
 * <a href="https://docs.nylas.com/reference#calendars">https://docs.nylas.com/reference#calendars</a>
 */
public class Calendars extends RestfulCollection<Calendar, CalendarQuery>{

	Calendars(NylasClient client, String accessToken) {
		super(client, Calendar.class, "calendars", accessToken);
	}

	@Override
	public List<Calendar> list() throws IOException, RequestFailedException {
		return super.list();
	}

	@Override
	public List<Calendar> list(CalendarQuery query) throws IOException, RequestFailedException {
		return super.list(query);
	}

	@Override
	public Calendar get(String id) throws IOException, RequestFailedException {
		return super.get(id);
	}

	@Override
	public List<String> ids(CalendarQuery query) throws IOException, RequestFailedException {
		return super.ids(query);
	}

	@Override
	public long count(CalendarQuery query) throws IOException, RequestFailedException {
		return super.count(query);
	}
}
