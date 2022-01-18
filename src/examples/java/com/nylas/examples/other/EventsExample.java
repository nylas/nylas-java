package com.nylas.examples.other;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.nylas.*;
import com.nylas.Event.Recurrence;
import com.nylas.Event.Time;
import com.nylas.Event.Timespan;
import com.nylas.examples.ExampleConf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EventsExample {

	private static final Logger log = LogManager.getLogger(EventsExample.class);

	public static void main(String[] args) throws Exception {
		
		ExampleConf conf = new ExampleConf();
		String accessToken = conf.get("access.token");
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(accessToken);
		Events events = account.events();
		
		RemoteCollection<Calendar> calendars = account.calendars().list();
		Calendar primary = null;
		for (Calendar c : calendars) {
			// primary calendar should be writable and have an email address
			if (!c.isReadOnly() && c.getName().contains("@")) {
				primary = c;
			}
		}
		if (primary == null) {
			log.info("Unable to find primary calendar");
			return;
		}
		log.info("Primary calendar: " + primary);
		
		
		EventQuery query = new EventQuery()
				.calendarId(primary.getId())
				.startsAfter(Instant.now())
				.startsBefore(Instant.now().plus(30, ChronoUnit.DAYS ))
				.limit(50);

		for (Event event : events.list(query)) {
			log.info("event: " + event);
		}
		
		basicEventCrud(events, primary);
		autocreateEvents(events, primary);

		generateICS(events, primary);
		//overrideRecurringEvent(primary, events);
		//fetchRoomResources(events);
		
		//events.rsvp("2ou66ruo7skqqc85g3za5yx8x", "yes", "7tc4ldy90u7gkbys880h8j2al", "totes!", true);
	}

	protected static void basicEventCrud(Events events, Calendar primary) throws IOException, RequestFailedException {
		Event event = createBasicEvent(events, primary);
		Event created = events.create(event, true);
		log.info("Created: " + created);

		Participant partier = new Participant("hamilton@example.com");
		partier.name("Alexander Hamilton");
		
		Participant partier1 = new Participant("hmulligan@example.com");
		partier1.name("Hercules Mulligan");

		Participant partier2 = new Participant("jlaurens@example.com");
		partier2.name("John Laurens");

		Participant partier3 = new Participant("lafayette@example.com");
		partier3.name("Marquis de Lafayette");

		ZoneId startTz = ZoneId.of("America/Los_Angeles");
		ZonedDateTime startTime = ZonedDateTime.now(startTz);

		created.setDescription("hopping good fun");
		created.setWhen(new Time(startTime)); // this party never ends
		created.setTitle("Nonsurprise Party");
		created.setBusy(false);
		created.setLocation("Lake Merritt");
		created.setRecurrence(new Recurrence(startTz.getId(), Collections.singletonList("RRULE:FREQ=WEEKLY;BYDAY=TH")));

		Map<String, String> metadata = new HashMap<>();
		metadata.put("event_category", "gathering");
		created.setMetadata(metadata);

		Event.Conferencing conferencing = new Event.Conferencing();
		conferencing.setProvider(Event.Conferencing.ConferencingProviders.ZOOM);
		Event.Conferencing.Details details = new Event.Conferencing.Details();
		details.setMeetingCode("213");
		details.setPassword("xyz");
		details.setUrl("https://us02web.zoom.us/j/****************");
		details.setPhone(Collections.singletonList("+11234567890"));
		conferencing.setDetails(details);
		created.setConferencing(conferencing);

		Event updated = events.update(created, true);
		log.info("Updated: " + updated);

		Event.EmailNotification notification = new Event.EmailNotification();
		notification.setMinutesBeforeEvent(60);
		notification.setSubject("Test Event Notification");
		notification.setBody("Reminding you about our meeting.");
		updated.setNotifications(Collections.singletonList(notification));
		updated.setParticipants(Arrays.asList(partier, partier1, partier2, partier3));

		updated = events.update(updated, true);
		log.info("Updated: " + updated);
		
		events.delete(updated.getId(), true);
		log.info("Deleted");
	}

	protected static void autocreateEvents(Events events, Calendar primary) throws IOException, RequestFailedException {
		Event event = createBasicEvent(events, primary);
		Event created = events.create(event, true);
		log.info("Created: " + created);

		Event.Conferencing conferencing = new Event.Conferencing();
		conferencing.setProvider("Zoom Meeting");
		Event.Conferencing.Autocreate autocreate = new Event.Conferencing.Autocreate();
		conferencing.setAutocreate(autocreate);
		created.setConferencing(conferencing);

		Event updated = events.update(created, true);
		log.info("Updated: " + updated);

		events.delete(updated.getId(), true);
		log.info("Deleted");
	}

	protected static void overrideRecurringEvent(Calendar calendar, Events events) throws IOException, RequestFailedException {
		EventQuery query = new EventQuery()
				.expandRecurring(true)
				.calendarId(calendar.getId())
				.startsAfter(Instant.now())
				.startsBefore(Instant.now().plus(30, ChronoUnit.DAYS ))
				.limit(50);
		
		Event eventToOverride = null;
		for (Event event : events.list(query)) {
			if (event.getMasterEventId() != null) {
				eventToOverride = event;
				break;
			}
		}
		log.info("Event to override: " + eventToOverride);
		
		if (eventToOverride != null) {
			eventToOverride.setTitle("Altered " + eventToOverride.getTitle());
			eventToOverride = events.update(eventToOverride, false);
			log.info("Overrode event instance:" + eventToOverride);
		}
	}

	protected static void generateICS(Events events, Calendar primary) throws RequestFailedException, IOException {
		Event event = createBasicEvent(events, primary);

		// You can make an Event from an event that hasn't been created on the API yet
		String icsFromLocalEvent = events.generateICS(event);
		log.info(icsFromLocalEvent);

		// Or, from a pre-existing event on the API server
		Event created = events.create(event, true);
		log.info("Created: " + created);
		String icsFromExistingEventID = events.generateICS(created.getId());
		log.info(icsFromExistingEventID);

		// You can also pass ICS Options for more configuration
		Events.ICSOptions icsOptions = new Events.ICSOptions();
		icsOptions.setIcal_uid("test_uuid");
		icsOptions.setMethod(Events.ICSOptions.ICSMethod.ADD);
		icsOptions.setProdid("test_prodid");
		String icsFromExistingEventWithOptions = events.generateICS(created, icsOptions);
		log.info(icsFromExistingEventWithOptions);
	}

	private static Event createBasicEvent(Events events, Calendar primary) throws IOException, RequestFailedException {
		ZoneId startTz = ZoneId.of("America/Los_Angeles");
		ZoneId endTz = ZoneId.of("America/New_York");
		ZonedDateTime startTime = ZonedDateTime.now(startTz);
		ZonedDateTime endTime = startTime.plusHours(2).withZoneSameInstant(endTz);

		Event event = new Event(primary.getId(), new Timespan(startTime, endTime));
		event.setTitle("Surprise Party");

		return event;
	}

}
