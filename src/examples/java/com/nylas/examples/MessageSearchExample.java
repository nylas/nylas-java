package com.nylas.examples;

import java.util.List;
import java.util.Properties;

import com.nylas.Message;
import com.nylas.Messages;
import com.nylas.NylasAccount;
import com.nylas.NylasClient;

public class MessageSearchExample {
	
	public static void main(String[] args) throws Exception {
		Properties props = Examples.loadExampleProperties();
		String accessToken = props.getProperty("access.token");
		
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(accessToken);

		Messages messages = account.messages();
		
		// search
		List<Message> results = messages.search("twitter", 5, 0);
		for (Message message : results) {
			System.out.println(message);
		}
		
	}
}
