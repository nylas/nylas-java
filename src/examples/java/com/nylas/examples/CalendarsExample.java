package com.nylas.examples;

import java.util.Properties;

import com.nylas.Calendar;
import com.nylas.Calendars;
import com.nylas.NylasAccount;
import com.nylas.NylasClient;

public class CalendarsExample {

	public static void main(String[] args) throws Exception {
		Properties props = Examples.loadExampleProperties();
		String accessToken = props.getProperty("access.token");
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(accessToken);
		Calendars calendars = account.calendars();
		for (Calendar calendar : calendars.list()) {
			System.out.println(calendar);
		}
	}
}
