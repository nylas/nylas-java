package com.nylas.examples.other;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.nylas.Calendar;
import com.nylas.Calendars;
import com.nylas.FreeBusy;
import com.nylas.JobStatus;
import com.nylas.NylasAccount;
import com.nylas.NylasClient;
import com.nylas.examples.ExampleConf;

public class CalendarsExample {

	public static void main(String[] args) throws Exception {
		ExampleConf conf = new ExampleConf();
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(conf.get("access.token"));
		Calendars calendars = account.calendars();
		String calendarEmail = null;
		for (Calendar calendar : calendars.list()) {
			System.out.println(calendar);
			if (calendar.getName().contains("@")) {
				calendarEmail = calendar.getName();
			}
		}
		if (calendarEmail == null) {
			System.out.println("No calendar found with a name that looks like an email");
		} else {
			Instant end = ZonedDateTime.now().toInstant();
			Instant start = end.minus(30, ChronoUnit.DAYS);
			List<FreeBusy> freeBusyInfo = calendars.checkFreeBusy(start, end, calendarEmail);
			for (FreeBusy freeBusy : freeBusyInfo) {
				System.out.println(freeBusy);
			}
		}
		
		Calendar newCal = new Calendar();
		newCal.setName("New Test Calendar");
		newCal.setDescription("Testing calendar creation");
		newCal.setLocation("far, far away");
		newCal.setTimezone("America/Los_Angeles");
		Calendar created = calendars.create(newCal);
		System.out.println("Created: " + created + " status: " + created.getJobStatusId());
		
		created.setName("New Test Calendar (changed)");
		created.setDescription("this calendar has been updated!");
		created.setLocation("nearby");
		created.setTimezone("America/New_York");
		Calendar updated = calendars.update(created);
		System.out.println("Updated: " + updated + " status: " + updated.getJobStatusId());
		
		String deleteJobStatusId = calendars.delete(updated.getId());
		System.out.println("Deleted, deleted job status id: " + deleteJobStatusId);
		
		JobStatus deleteStatus = account.jobStatuses().get(deleteJobStatusId);
		System.out.println("Deletion status: " + deleteStatus);
	}
}
