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

	/**
	 * Check calendar free or busy status for a single email
	 * @deprecated Replaced by {@link #checkFreeBusy(FreeBusyQuery)}
	 */
	@Deprecated
	public List<FreeBusy> checkFreeBusy(Instant startTime, Instant endTime, String email)
			throws IOException, RequestFailedException {
		return checkFreeBusy(startTime, endTime, Arrays.asList(email));
	}

	/**
	 * Check calendar free or busy status for a list of emails
	 * @param startTime Timestamp for the beginning of the free/busy window
	 * @param endTime Timestamp for the end of the free/busy window
	 * @param emails Email addresses on the same domain to check
	 * @return The free/busy timeslots
	 * @deprecated Replaced by {@link #checkFreeBusy(FreeBusyQuery)}
	 */
	@Deprecated
	public List<FreeBusy> checkFreeBusy(Instant startTime, Instant endTime, List<String> emails)
			throws IOException, RequestFailedException {
		HttpUrl.Builder url = getCollectionUrl().addPathSegment("free-busy");
		Map<String, Object> params = new HashMap<>();
		params.put("start_time", startTime.getEpochSecond());
		params.put("end_time", endTime.getEpochSecond());
		params.put("emails", emails);
		return client.executePost(authUser, url, params, JsonHelper.listTypeOf(FreeBusy.class));
	}

	/**
	 * Check calendar free or busy status
	 * @param query The free/busy query
	 * @return The free/busy timeslots
	 */
	public List<FreeBusy> checkFreeBusy(FreeBusyQuery query) throws IOException, RequestFailedException {
		query.validate();
		HttpUrl.Builder url = getCollectionUrl().addPathSegment("free-busy");
		return client.executePost(authUser, url, query.toMap(), JsonHelper.listTypeOf(FreeBusy.class));
	}

	/**
	 * Check multiple calendars to find available time slots for a single meeting
	 * @param query The single meeting availability query
	 * @return The availability information; a list of time slots where all participants are available
	 */
	public Availability availability(SingleAvailabilityQuery query) throws IOException, RequestFailedException {
		query.validate();
		HttpUrl.Builder url = getCollectionUrl().addPathSegment("availability");
		return client.executePost(authUser, url, query.toMap(), Availability.class);
	}

	/**
	 * Check multiple calendars to find availability for multiple meetings with several participants
	 * @param query The query for multiple meeting availabilities
	 * @return The availability information; a list of all possible groupings that share time slots
	 */
	public List<List<ConsecutiveAvailability>> consecutiveAvailability(MultipleAvailabilityQuery query)
			throws IOException, RequestFailedException {
		query.validate();
		HttpUrl.Builder url = getCollectionUrl().addPathSegment("availability").addPathSegment("consecutive");
		return client.executePost(authUser, url, query.toMap(), JsonHelper.listTypeOf(JsonHelper.listTypeOf(ConsecutiveAvailability.class)));
	}
}
