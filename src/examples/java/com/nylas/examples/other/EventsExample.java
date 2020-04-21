package com.nylas.examples.other;

import java.util.List;

import com.nylas.Calendar;
import com.nylas.Event;
import com.nylas.EventQuery;
import com.nylas.Events;
import com.nylas.NylasAccount;
import com.nylas.NylasClient;
import com.nylas.RemoteCollection;
import com.nylas.RoomResource;
import com.nylas.examples.ExampleConf;

public class EventsExample {

	public static void main(String[] args) throws Exception {
		ExampleConf conf = new ExampleConf();
		String accessToken = conf.get("access.token");
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(accessToken);
		Events events = account.events();
		
		
		RemoteCollection<Calendar> calendars = account.calendars().list();
		Calendar writableCalendar = null;
		Calendar emailedEvents = null;
		for (Calendar c : calendars) {
			if (!c.isReadOnly()) {
				writableCalendar = c;
			}
			if ("Emailed events".equals(c.getName())) {
				emailedEvents = c;
			}
		}
		if (writableCalendar == null) {
			System.out.println("Account has no writable calendars.");
			return;
		}
		System.out.println("Writable calendar: " + writableCalendar);
		System.out.println("Emailed events calendar: " + emailedEvents);
		

		
		EventQuery query = new EventQuery()
				
//				.expandRecurring(true)
//				.startsAfter(1571241599L)
//				.calendarId(emailedEvents.getId())
				.limit(10);
		
		
		for (Event event : events.list(query)) {
			
			if (event.getTitle().contains("Learn")) {
				System.out.println("FOUND IT **********************************");
			}
			
			System.out.println(event);
		}
		
		List<RoomResource> resources = events.roomResources();
		for (RoomResource resource : resources) {
			System.out.println(resource);
		}
		
		
		
		//events.rsvp("2ou66ruo7skqqc85g3za5yx8x", "yes", "7tc4ldy90u7gkbys880h8j2al", "totes!", true);

//		
//		
//		long time = LocalDate.now().plusDays(2).atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
//		Event event = new Event(cal.getId(), new Time(time));
//		event.setTitle("Surprise Party");
//		Participant partier = new Participant("hamilton@davelink.net");
//		partier.name("Alexander Hamilton");
//
//		Event created = events.create(event, true);
//		System.out.println("Created: " + created);
//		
//		Participant partier1 = new Participant("hmulligan@avelink.net");
//		partier1.name("Hercules Mulligan");
//
//		Participant partier2 = new Participant("jlaurens@example.com");
//		partier2.name("John Laurens");
//
//		Participant partier3 = new Participant("lafayette@example.com");
//		partier3.name("Marquis de Lafayette");
//
//		created.setDescription("hopping good fun");
//		created.setWhen(new Timespan(time, time+86400));
//		created.setTitle("Nonsurprise Party");
//		created.setBusy(false);
//		created.setLocation("Lake Merritt");
//		created.setParticipants(Arrays.asList(partier, partier1, partier2, partier3));
//		Event updated = events.update(created, true);
//		System.out.println("Updated: " + updated);
//		
//		events.delete(updated.getId(), true);
//		System.out.println("Deleted");
		
	}

}
