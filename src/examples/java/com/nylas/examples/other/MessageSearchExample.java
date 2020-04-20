package com.nylas.examples.other;

import com.nylas.Message;
import com.nylas.Messages;
import com.nylas.NylasAccount;
import com.nylas.NylasClient;
import com.nylas.RemoteCollection;
import com.nylas.examples.ExampleConf;

public class MessageSearchExample {
	
	public static void main(String[] args) throws Exception {
		ExampleConf conf = new ExampleConf();
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(conf.get("access.token"));

		Messages messages = account.messages();
		
		// search
		RemoteCollection<Message> results = messages.search("twitter", 5, 0);
		for (Message message : results) {
			System.out.println(message);
		}
		
	}
}
