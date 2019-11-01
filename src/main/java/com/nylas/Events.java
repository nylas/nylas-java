package com.nylas;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;

/**
 * @see https://docs.nylas.com/reference#events
 */
public class Events extends RestfulCollection<Event, EventQuery>{

	Events(NylasClient client, String accessToken) {
		super(client, Event.class, "events", accessToken);
	}

	@Override
	public List<Event> list() throws IOException, RequestFailedException {
		return super.list();
	}

	@Override
	public List<Event> list(EventQuery query) throws IOException, RequestFailedException {
		return super.list(query);
	}

	@Override
	public Event get(String id) throws IOException, RequestFailedException {
		return super.get(id);
	}

	@Override
	public List<String> ids(EventQuery query) throws IOException, RequestFailedException {
		return super.ids(query);
	}

	@Override
	public long count(EventQuery query) throws IOException, RequestFailedException {
		return super.count(query);
	}
	
	public Event create(Event event, boolean notifyParticipants) throws IOException, RequestFailedException {
		return super.create(event, getExtraQueryParams(notifyParticipants));
	}
	
	public Event update(Event event, boolean notifyParticipants) throws IOException, RequestFailedException {
		return super.update(event, getExtraQueryParams(notifyParticipants));
	}
	
	public void delete(String id, boolean notifyParticipants) throws IOException, RequestFailedException {
		super.delete(id, getExtraQueryParams(notifyParticipants));
	}

	/**
	 * Convenience method to create or update the given event.
	 * If it has an existing ID, update it, otherwise create it.
	 * Returns the event as returned by the server.
	 */
	public Event createOrUpdate(Event event, boolean notifyParticipants) throws IOException, RequestFailedException {
		return event.hasId() ? update(event, notifyParticipants) : create(event, notifyParticipants);
	}
	
	public Event rsvp(String eventId, String status, String accountId, String comment, boolean notifyParticipants)
			 throws IOException, RequestFailedException {
		Map<String, Object> params = new HashMap<>();
		params.put("event_id", eventId);
		params.put("status", status);
		params.put("account_id", accountId);
		if (comment != null && !comment.isEmpty()) {
			params.put("comment", comment);
		}
		HttpUrl.Builder url = client.newUrlBuilder().addPathSegment("send-rsvp");
		addQueryParams(url, getExtraQueryParams(notifyParticipants));
		return client.executePost(authUser, url, params, modelClass);
	}
	
	private static final Map<String, String> NOTIFY_PARTICIPANTS_PARAMS
		= Collections.unmodifiableMap(Maps.of("notify_participants", "true"));
	private static Map<String, String> getExtraQueryParams(boolean notifyParticipants) {
		return notifyParticipants ? NOTIFY_PARTICIPANTS_PARAMS : null;
	}
}
