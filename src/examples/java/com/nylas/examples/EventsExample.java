package com.nylas.examples;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import com.nylas.Calendar;
import com.nylas.Event;
import com.nylas.Event.Time;
import com.nylas.Event.Timespan;
import com.nylas.EventQuery;
import com.nylas.Events;
import com.nylas.NylasAccount;
import com.nylas.NylasClient;
import com.nylas.Participant;

public class EventsExample {

	public static void main(String[] args) throws Exception {
		Properties props = Examples.loadExampleProperties();
		String accessToken = props.getProperty("access.token");
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(accessToken);
		Events events = account.events();
		
		EventQuery query = new EventQuery()
				.expandRecurring(true)
				.startsAfter(1571241599L)
				.limit(10);
		
		
		for (Event event : events.list(query)) {
			System.out.println(event);
		}
		
		
		List<Calendar> calendars = account.calendars().list();
		Calendar cal = null;
		for (Calendar c : calendars) {
			if (!c.isReadOnly()) {
				cal = c;
				break;
			}
		}
		if (cal == null) {
			System.out.println("Account has no writable calendars.");
			return;
		}
		
		
		
		long time = LocalDate.now().plusDays(2).atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
		Event event = new Event(cal.getId(), new Time(time));
		event.setTitle("Surprise Party");
		Participant partier = new Participant();
		partier.setName("Alexander Hamilton");
		partier.setEmail("hamilton@davelink.net");

		Event created = events.create(event, true);
		System.out.println("Created: " + created);
		
		Participant partier1 = new Participant();
		partier1.setName("Hercules Mulligan");
		partier1.setEmail("hmulligan@avelink.net");

		Participant partier2 = new Participant();
		partier2.setName("John Laurens");
		partier2.setEmail("jlaurens@example.com");

		Participant partier3 = new Participant();
		partier3.setName("Marquis de Lafayette");
		partier3.setEmail("lafayette@example.com");

		created.setDescription("hopping good fun");
		created.setWhen(new Timespan(time, time+86400));
		created.setTitle("Nonsurprise Party");
		created.setBusy(false);
		created.setLocation("Lake Merritt");
		created.setParticipants(Arrays.asList(partier, partier1, partier2, partier3));
		Event updated = events.update(created, true);
		System.out.println("Updated: " + updated);
		
		events.delete(updated.getId(), true);
		System.out.println("Deleted");
		
	}

}
