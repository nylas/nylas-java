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

public class EventsExample {

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
			System.out.println("Unable to find primary calendar");
			return;
		}
		System.out.println("Primary calendar: " + primary);
		
		
		EventQuery query = new EventQuery()
				.calendarId(primary.getId())
				.startsAfter(Instant.now())
				.startsBefore(Instant.now().plus(30, ChronoUnit.DAYS ))
				.limit(50);

		for (Event event : events.list(query)) {
			System.out.println("event: " + event);
		}
		
		basicEventCrud(events, primary);
		//overrideRecurringEvent(primary, events);
		//fetchRoomResources(events);
		
		//events.rsvp("2ou66ruo7skqqc85g3za5yx8x", "yes", "7tc4ldy90u7gkbys880h8j2al", "totes!", true);
	}

	protected static void basicEventCrud(Events events, Calendar primary) throws IOException, RequestFailedException {
		ZoneId startTz = ZoneId.of("America/Los_Angeles");
		ZoneId endTz = ZoneId.of("America/New_York");
		ZonedDateTime startTime = ZonedDateTime.now(startTz);
		ZonedDateTime endTime = startTime.plusHours(2).withZoneSameInstant(endTz);

		Event event = new Event(primary.getId(), new Timespan(startTime, endTime));
		event.setTitle("Surprise Party");
		Participant partier = new Participant("hamilton@example.com");
		partier.name("Alexander Hamilton");

		Event created = events.create(event, true);
		System.out.println("Created: " + created);
		
		Participant partier1 = new Participant("hmulligan@example.com");
		partier1.name("Hercules Mulligan");

		Participant partier2 = new Participant("jlaurens@example.com");
		partier2.name("John Laurens");

		Participant partier3 = new Participant("lafayette@example.com");
		partier3.name("Marquis de Lafayette");

		created.setDescription("hopping good fun");
		created.setWhen(new Time(startTime)); // this party never ends
		created.setTitle("Nonsurprise Party");
		created.setBusy(false);
		created.setLocation("Lake Merritt");
		created.setParticipants(Arrays.asList(partier, partier1, partier2, partier3));
		created.setRecurrence(new Recurrence(startTz.getId(), Arrays.asList("RRULE:FREQ=WEEKLY;BYDAY=TH")));

		Map<String, String> metadata = new HashMap<>();
		metadata.put("event_category", "gathering");
		created.setMetadata(metadata);

		EventConferencing conferencing = new EventConferencing();
		conferencing.setProvider("Zoom Meeting");
		EventConferencing.Details details = new EventConferencing.Details();
		details.setMeetingCode("213");
		details.setPassword("xyz");
		details.setUrl("https://us02web.zoom.us/j/****************");
		details.setPhone(Collections.singletonList("+11234567890"));
		conferencing.setDetails(details);
		event.setConferencing(conferencing);

		Event updated = events.update(created, true);
		System.out.println("Updated: " + updated);
		
		events.delete(created.getId(), true);
		System.out.println("Deleted");
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
		System.out.println("Event to override: " + eventToOverride);
		
		if (eventToOverride != null) {
			eventToOverride.setTitle("Altered " + eventToOverride.getTitle());
			eventToOverride = events.update(eventToOverride, false);
			System.out.println("Overrode event instance:" + eventToOverride);
		}
	}

}
