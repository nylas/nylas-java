package com.nylas.examples;

import java.util.Properties;

import com.nylas.Event;
import com.nylas.Events;
import com.nylas.NylasAccount;
import com.nylas.NylasClient;

public class EventsExample {

	public static void main(String[] args) throws Exception {
		Properties props = Examples.loadExampleProperties();
		String accessToken = props.getProperty("access.token");
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(accessToken);
		Events events = account.events();
		
		for (Event event : events.list()) {
			System.out.println(event);
		}
	}

}
