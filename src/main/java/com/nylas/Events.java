package com.nylas;

import java.io.IOException;
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
	public List<String> ids(EventQuery query) throws IOException, RequestFailedException {
		return super.ids(query);
	}

	@Override
	public long count(EventQuery query) throws IOException, RequestFailedException {
		return super.count(query);
	}
	
	public Event create(Event event, boolean notifyParticipants) throws IOException, RequestFailedException {
		if (event.hasId()) {
			throw new UnsupportedOperationException("Cannot create event with an existing id.  Use update instead.");
		}
		Map<String, Object> params = event.getWritableFields(true);
		HttpUrl url = getCollectionUrl();
		if (notifyParticipants) {
			url = notifyUrl(url);
		}
		return client.executePost(authUser, url, params, modelClass);
	}
	
	public Event update(Event event, boolean notifyParticipants) throws IOException, RequestFailedException {
		if (!event.hasId()) {
			throw new UnsupportedOperationException("Cannot update event without an existing id.  Use create instead.");
		}
		Map<String, Object> params = event.getWritableFields(false);
		HttpUrl url = getInstanceUrl(event.getId());
		if (notifyParticipants) {
			url = notifyUrl(url);
		}
		return client.executePut(authUser, url, params, modelClass);
	}
	
	public void delete(String id, boolean notifyParticipants) throws IOException, RequestFailedException {
		HttpUrl url = getInstanceUrl(id);
		if (notifyParticipants) {
			url = notifyUrl(url);
		}
		client.executeDelete(authUser, url, null);
	}

	/**
	 * Convenience method to create or update the given event.
	 * If it has an existing ID, update it, otherwise create it.
	 * Returns the event as returned by the server.
	 */
	public Event createOrUpdate(Event event, boolean notifyParticipants) throws IOException, RequestFailedException {
		return event.hasId() ? update(event, notifyParticipants) : create(event, notifyParticipants);
	}
	
	public Event rsvp(String eventId, String status, String acountId, String comment)
			 throws IOException, RequestFailedException {
		// TODO
		return null;
	}
	
	private static HttpUrl notifyUrl(HttpUrl url) {
		return url.newBuilder().addQueryParameter("notify_participants", "true").build();
	}
}
