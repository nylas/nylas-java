package com.nylas.examples;

import com.nylas.Calendar;
import com.nylas.Calendars;
import com.nylas.NylasAccount;
import com.nylas.NylasClient;

public class CalendarsExample {

	public static void main(String[] args) throws Exception {
		ExampleConf conf = new ExampleConf();
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(conf.get("access.token"));
		Calendars calendars = account.calendars();
		for (Calendar calendar : calendars.list()) {
			System.out.println(calendar);
		}
	}
}
