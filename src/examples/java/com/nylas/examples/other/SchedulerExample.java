package com.nylas.examples.other;

import com.nylas.*;
import com.nylas.examples.ExampleConf;
import com.nylas.scheduler.*;
import com.nylas.Schedulers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchedulerExample {

	public static void main(String[] args) throws Exception {

		ExampleConf conf = new ExampleConf();
		String accessToken = conf.get("access.token");
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(accessToken);
		Schedulers schedulers = account.schedulers();

		Scheduler scheduler = new Scheduler();
		scheduler.addAccessTokens(conf.get("access.token"));
		scheduler.setName("Java SDK Example");
		scheduler.setSlug("java_example_" + new Date().getTime() / 1000);
		Scheduler created = schedulers.save(scheduler);

		created.setName("Updated Java SDK Name");
		Scheduler updated = schedulers.save(created);

		List<AvailableCalendars> availableCalendars = schedulers.getAvailableCalendars(updated.getId());
		UploadImageResponse uploadImageResponse = schedulers.uploadImage(updated.getId(), "image/png", "test.png");

		scheduler = schedulers.get(updated.getId());

		try {
			List<ProviderAvailability> googleAvailability = schedulers.getGoogleAvailability();
			List<ProviderAvailability> office365Availability = schedulers.getOffice365Availability();
		} catch (RequestFailedException e) {
			System.out.println(e.getErrorMessage());
		}

		Scheduler page = schedulers.getPageBySlug(scheduler.getSlug());
		List<TimeSlot> timeSlots = schedulers.getAvailableTimeSlots(scheduler.getSlug());

		BookingRequest bookingRequest = new BookingRequest();
		Map<String, Object> additionalValues = new HashMap<>();
		additionalValues.put("important", true);
		bookingRequest.setAdditionalValues(additionalValues);
		bookingRequest.setEmail("recipient@example.com");
		bookingRequest.setLocale("en_US");
		bookingRequest.setName("John Doe");
		bookingRequest.setTimezone("America/New_York");
		bookingRequest.setSlot(timeSlots.get(timeSlots.size() - 1));
		BookingConfirmation bookingConfirmation = schedulers.bookTimeSlot(scheduler.getSlug(), bookingRequest);

		boolean success = schedulers.cancelBooking(scheduler.getSlug(), bookingConfirmation.getEditHash(), "This was a test.");

		schedulers.delete(scheduler.getId());
	}
}
