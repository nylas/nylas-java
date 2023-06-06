package com.nylas.resources;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.nylas.models.EventQuery;
import com.nylas.NylasClient;
import com.nylas.models.Event;
import com.nylas.util.Maps;
import com.nylas.util.Validations;
import okhttp3.HttpUrl;

/**
 * <a href="https://docs.nylas.com/reference#events">https://docs.nylas.com/reference#events</a>
 */
public class Events extends RestfulDAO<Event> {

	public Events(NylasClient client, String accessToken) {
		super(client, Event.class, "events", accessToken);
	}

	public RemoteCollection<Event> list() throws IOException, RequestFailedException {
		return list(new EventQuery());
	}
	
	public RemoteCollection<Event> list(EventQuery query) throws IOException, RequestFailedException {
		return super.list(query);
	}

	@Override
	public Event get(String id) throws IOException, RequestFailedException {
		return super.get(id);
	}

	public RemoteCollection<String> ids(EventQuery query) throws IOException, RequestFailedException {
		return super.ids(query);
	}

	public long count(EventQuery query) throws IOException, RequestFailedException {
		return super.count(query);
	}
	
	public Event create(Event event, boolean notifyParticipants) throws IOException, RequestFailedException {
		event.validate();
		return super.create(event, getExtraQueryParams(notifyParticipants));
	}
	
	public Event update(Event event, boolean notifyParticipants) throws IOException, RequestFailedException {
		event.validate();
		return super.update(event, getExtraQueryParams(notifyParticipants));
	}
	
	/**
	 * Delete the event with the given id.
	 * Returns the id of job status for the deletion.
	 */
	public String delete(String id, boolean notifyParticipants) throws IOException, RequestFailedException {
		return super.delete(id, getExtraQueryParams(notifyParticipants));
	}

	/**
	 * Convenience method to create or update the given event.
	 * If it has an existing ID, update it, otherwise create it.
	 * Returns the event as returned by the server.
	 */
	public Event save(Event event, boolean notifyParticipants) throws IOException, RequestFailedException {
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


	/**
	 * Generate an ICS file server-side, from an Event
	 * {@code icsOptions} is set to null.
	 * @see Events#generateICS(String, ICSOptions)
	 */
	public String generateICS(String eventId) throws RequestFailedException, IOException {
		return generateICS(eventId, null);
	}

	/**
	 * Generate an ICS file server-side, from an Event ID
	 * @param eventId The ID of the existing Event to generate an ICS file for
	 * @param icsOptions Configuration for the ICS file
	 * @return String for writing directly into an ICS file
	 * @see <a href="https://developer.nylas.com/docs/api/#post/events/to-ics">Generate ICS File</a>
	 */
	public String generateICS(String eventId, ICSOptions icsOptions) throws RequestFailedException, IOException {
		if(Validations.nullOrEmpty(eventId)) {
			throw new IllegalArgumentException("Must pass in an Event ID to generate an ICS.");
		}
		Map<String, Object> params = new HashMap<>();
		params.put("event_id", eventId);
		return toICS(params, icsOptions);
	}

	/**
	 * Generate an ICS file server-side, from an Event
	 * {@code icsOptions} is set to null.
	 * @see Events#generateICS(String, ICSOptions)
	 */
	public String generateICS(Event event) throws RequestFailedException, IOException {
		return generateICS(event, null);
	}

	/**
	 * Generate an ICS file server-side, from an Event
	 * @param event The Event to generate an ICS file for
	 * @param icsOptions Configuration for the ICS file
	 * @return String for writing directly into an ICS file
	 * @see <a href="https://developer.nylas.com/docs/api/#post/events/to-ics">Generate ICS File</a>
	 */
	public String generateICS(Event event, ICSOptions icsOptions) throws RequestFailedException, IOException {
		if(event == null || event.getCalendarId() == null) {
			throw new IllegalArgumentException("Must pass in an Event with a Calendar ID.");
		}
		Map<String, Object> eventMap = event.getWritableFields(true);
		return toICS(eventMap, icsOptions);
	}

	/**
	 * Generate an ICS file server-side, from an Event payload
	 * @param params The Event payload
	 * @param icsOptions Configuration for the ICS file
	 * @return String for writing directly into an ICS file
	 * @see <a href="https://developer.nylas.com/docs/api/#post/events/to-ics">Generate ICS File</a>
	 */
	private String toICS(Map<String, Object> params, ICSOptions icsOptions) throws RequestFailedException, IOException {
		HttpUrl.Builder url = getCollectionUrl().addPathSegment("to-ics");
		Maps.putIfNotNull(params, "ics_options", icsOptions);
		Map<String, String> response = client.executePost(authUser, url, params, Map.class);
		return response.get("ics");
	}
	
	private static final Map<String, String> NOTIFY_PARTICIPANTS_PARAMS
		= Collections.unmodifiableMap(Maps.of("notify_participants", "true"));
	private static final Map<String, String> DONT_NOTIFY_PARTICIPANTS_PARAMS
		= Collections.unmodifiableMap(Maps.of("notify_participants", "false"));
	private static Map<String, String> getExtraQueryParams(boolean notifyParticipants) {
		return notifyParticipants ? NOTIFY_PARTICIPANTS_PARAMS : DONT_NOTIFY_PARTICIPANTS_PARAMS;
	}

	/**
	 * Class representation of the ics_options object used
	 * for optional configuration during ICS file generation
	 * @see <a href="https://developer.nylas.com/docs/api/#post/events/to-ics">Generate ICS File</a>
	 */
	public static class ICSOptions {
		private String ical_uid;
		private String method;
		private String prodid;

		public enum ICSMethod {
			REQUEST,
			PUBLISH,
			REPLY,
			ADD,
			CANCEL,
			REFRESH,

			;

			@Override
			public String toString() {
				return super.toString().toLowerCase();
			}
		}

		public String getICalUID() {
			return ical_uid;
		}

		public String getMethod() {
			return method;
		}

		public String getProdId() {
			return prodid;
		}

		public void setIcal_uid(String iCalUID) {
			this.ical_uid = iCalUID;
		}

		public void setMethod(ICSMethod method) {
			this.method = method.toString();
		}

		public void setProdid(String prodId) {
			this.prodid = prodId;
		}
	}
}
