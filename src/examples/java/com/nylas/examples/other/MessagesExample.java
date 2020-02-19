package com.nylas.examples.other;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.nylas.Message;
import com.nylas.MessageQuery;
import com.nylas.Messages;
import com.nylas.NylasAccount;
import com.nylas.NylasClient;
import com.nylas.examples.ExampleConf;

public class MessagesExample {

	public static void main(String[] args) throws Exception {
		ExampleConf conf = new ExampleConf();
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(conf.get("access.token"));

		Messages messages = account.messages();
		
		Instant end = ZonedDateTime.now().toInstant();
		Instant start = end.minus(1, ChronoUnit.DAYS);
		MessageQuery query = new MessageQuery()
				.limit(10)
				//.hasAttachment(true)
				.receivedAfter(start)
				.receivedBefore(end)
				//.anyEmail("info@twitter.com")
//				.in("Example Label 2")
				;

		
		List<Message> allMessages = messages.expanded(query);
		if (allMessages.isEmpty()) {
			System.out.println("No messages");
			return;
		}
		Message firstMessage  = allMessages.get(0);
		for (Message message : allMessages) {
			System.out.println(message);
		}
		
		
		Message message = messages.get(firstMessage.getId());
		System.out.println("got by id: " + message);
		
		message = messages.setUnread(firstMessage.getId(), !firstMessage.getUnread());
		System.out.println("toggled unread: " + message);
		
		message = messages.setStarred(firstMessage.getId(), !firstMessage.getStarred());
		System.out.println("toggled starred: " + message);
		
		String raw = messages.getRaw(firstMessage.getId());
		System.out.println("raw: " + raw);
	}
}
