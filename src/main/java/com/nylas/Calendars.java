package com.nylas;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;

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
	
	public List<FreeBusy> checkFreeBusy(long startTime, long endTime, String email)
			throws IOException, RequestFailedException {
		return checkFreeBusy(startTime, endTime, Arrays.asList(email));
	}
	
	public List<FreeBusy> checkFreeBusy(long startTime, long endTime, List<String> emails)
			throws IOException, RequestFailedException {
		HttpUrl.Builder url = getCollectionUrl().addPathSegment("free-busy");
		Map<String, Object> params = new HashMap<>();
		params.put("start_time", startTime);
		params.put("end_time", endTime);
		params.put("emails", emails);
		return client.executePost(authUser, url, params, JsonHelper.listTypeOf(FreeBusy.class));
	}
}
