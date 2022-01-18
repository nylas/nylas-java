package com.nylas;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;

/**
 * <a href="https://docs.nylas.com/reference#calendars">https://docs.nylas.com/reference#calendars</a>
 */
public class Calendars extends RestfulDAO<Calendar> {

	Calendars(NylasClient client, String accessToken) {
		super(client, Calendar.class, "calendars", accessToken);
	}

	public RemoteCollection<Calendar> list() throws IOException, RequestFailedException {
		return list(new CalendarQuery());
	}
	
	public RemoteCollection<Calendar> list(CalendarQuery query) throws IOException, RequestFailedException {
		return super.list(query);
	}

	@Override
	public Calendar get(String id) throws IOException, RequestFailedException {
		return super.get(id);
	}

	@Override
	public Calendar create(Calendar calendar) throws IOException, RequestFailedException {
		return super.create(calendar);
	}
	
	@Override
	public Calendar update(Calendar calendar) throws IOException, RequestFailedException {
		return super.update(calendar);
	}

	/**
	 * Delete the calendar with the given id.
	 * Returns the id of job status for the deletion.
	 */
	@Override
	public String delete(String id) throws IOException, RequestFailedException {
		return super.delete(id);
	}
	
	public RemoteCollection<String> ids(CalendarQuery query) throws IOException, RequestFailedException {
		return super.ids(query);
	}

	public long count(CalendarQuery query) throws IOException, RequestFailedException {
		return super.count(query);
	}
	
	public List<FreeBusy> checkFreeBusy(Instant startTime, Instant endTime, String email)
			throws IOException, RequestFailedException {
		return checkFreeBusy(startTime, endTime, Arrays.asList(email));
	}
	
	public List<FreeBusy> checkFreeBusy(Instant startTime, Instant endTime, List<String> emails)
			throws IOException, RequestFailedException {
		HttpUrl.Builder url = getCollectionUrl().addPathSegment("free-busy");
		Map<String, Object> params = new HashMap<>();
		params.put("start_time", startTime.getEpochSecond());
		params.put("end_time", endTime.getEpochSecond());
		params.put("emails", emails);
		return client.executePost(authUser, url, params, JsonHelper.listTypeOf(FreeBusy.class));
	}

	public Availability availability(SingleAvailabilityQuery query) throws IOException, RequestFailedException {
		if(!query.isValid()) {
			throw new IllegalArgumentException("Availability query missing one or more required parameters.");
		}
		HttpUrl.Builder url = getCollectionUrl().addPathSegment("availability");
		return client.executePost(authUser, url, query.toMap(), Availability.class);
	}

	public List<List<ConsecutiveAvailability>> consecutiveAvailability(MultipleAvailabilityQuery query)
			throws IOException, RequestFailedException {
		if(!query.isValid()) {
			throw new IllegalArgumentException("Availability query missing one or more required parameters.");
		}
		HttpUrl.Builder url = getCollectionUrl().addPathSegment("availability").addPathSegment("consecutive");
		return client.executePost(authUser, url, query.toMap(), JsonHelper.listTypeOf(JsonHelper.listTypeOf(ConsecutiveAvailability.class)));
	}
}
