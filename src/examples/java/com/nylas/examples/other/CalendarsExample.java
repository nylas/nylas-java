package com.nylas.examples.other;

import java.io.IOException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.nylas.*;
import com.nylas.examples.ExampleConf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CalendarsExample {

	private static final Logger log = LogManager.getLogger(CalendarsExample.class);

	public static void main(String[] args) throws Exception {
		ExampleConf conf = new ExampleConf();
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(conf.get("access.token"));
		Calendars calendars = account.calendars();
		String calendarEmail = null;
		String accountId = null;
		String calendarId = null;
		for (Calendar calendar : calendars.list()) {
			log.info(calendar);
			if (calendar.getName().contains("@")) {
				calendarEmail = calendar.getName();
				accountId = calendar.getAccountId();
				calendarId = calendar.getId();
			}
		}
		if (calendarEmail == null) {
			log.info("No calendar found with a name that looks like an email");
		} else {
			Instant end = ZonedDateTime.now().toInstant();
			Instant start = end.minus(30, ChronoUnit.DAYS);
			FreeBusyQuery query = new FreeBusyQuery()
					.startTime(start.getEpochSecond())
					.endTime(end.getEpochSecond())
					.emails(calendarEmail);
			List<FreeBusy> freeBusyInfo = calendars.checkFreeBusy(query);
			for (FreeBusy freeBusy : freeBusyInfo) {
				log.info(freeBusy);
			}
		}
		
		Calendar newCal = new Calendar();
		newCal.setName("New Test Calendar");
		newCal.setDescription("Testing calendar creation");
		newCal.setLocation("far, far away");
		newCal.setTimezone("America/Los_Angeles");
		Calendar created = calendars.create(newCal);
		log.info("Created: " + created + " status: " + created.getJobStatusId());

		created.setName("New Test Calendar (changed)");
		created.setDescription("this calendar has been updated!");
		created.setLocation("nearby");
		created.setTimezone("America/New_York");
		Map<String, String> metadata = new HashMap<>();
		metadata.put("calendar_type", "test");
		created.setMetadata(metadata);
		Calendar updated = calendars.update(created);
		log.info("Updated: " + updated + " status: " + updated.getJobStatusId());

		String deleteJobStatusId = calendars.delete(updated.getId());
		log.info("Deleted, deleted job status id: " + deleteJobStatusId);

		JobStatus deleteStatus = account.jobStatuses().get(deleteJobStatusId);
		log.info("Deletion status: " + deleteStatus);

		availability(calendars, accountId, calendarId, calendarEmail);
	}

	protected static void availability(Calendars calendars, String accountId, String calendarId, String email)
			throws RequestFailedException, IOException {
		FreeBusyCalendars freeBusyCalendars = new FreeBusyCalendars(accountId, Collections.singletonList(calendarId));
		SingleAvailabilityQuery query = new SingleAvailabilityQuery()
				.durationMinutes(30)
				.startTime(Instant.now())
				.endTime(Instant.now().plus(1, ChronoUnit.HOURS))
				.intervalMinutes(10)
				.calendars(freeBusyCalendars);

		Availability availability = calendars.availability(query);
		log.info(availability.toString());

		MultipleAvailabilityQuery consecutiveQuery = new MultipleAvailabilityQuery()
				.durationMinutes(30)
				.startTime(Instant.now())
				.endTime(Instant.now().plus(1, ChronoUnit.HOURS))
				.intervalMinutes(10)
				.emails(Collections.singletonList(Collections.singletonList(email)));

		List<List<ConsecutiveAvailability>> consecutiveAvailability = calendars.consecutiveAvailability(consecutiveQuery);
		log.info(consecutiveAvailability.toString());
	}
}
