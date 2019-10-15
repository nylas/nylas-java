package com.nylas;

import java.io.IOException;
import java.util.List;

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
}
