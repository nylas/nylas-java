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
import com.nylas.RemoteCollection;
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
//				.offset(0)
//				.limit(2990)
				.hasAttachment(true)
//				.receivedAfter(start)
//				.receivedBefore(end)
				//.anyEmail("info@twitter.com")
//				.in("Example Label 2")
				;

		
		RemoteCollection<String> ids = messages.ids(query);
//		ids.chunkSize(2000);
		
		for (String id : ids) {
			System.out.println(id);
		}
		
//		List<String> allIds = ids.fetchAll();
//		System.out.println("size = " + allIds.size());
		
		
		
		
		
//		long count = messages.count(query);
//		System.out.println("Count: " + count);
//		
//		List<String> ids = messages.ids(query).fetchAll();
//		System.out.println("ids size: " + ids.size());
//		
//		List<Message> allMessages = messages.list(query).fetchAll();
//		if (allMessages.isEmpty()) {
//			System.out.println("No messages");
//			return;
//		}
//		
//		System.out.println("collection size: " + allMessages.size());
//		
//		Message firstMessage  = allMessages.get(0);
//		for (Message message : allMessages) {
//			System.out.println(message);
//		}
		
		
		
//		Message message = messages.get(firstMessage.getId());
//		System.out.println("got by id: " + message);
//		
//		message = messages.setUnread(firstMessage.getId(), !firstMessage.getUnread());
//		System.out.println("toggled unread: " + message);
//		
//		message = messages.setStarred(firstMessage.getId(), !firstMessage.getStarred());
//		System.out.println("toggled starred: " + message);
//		
//		String raw = messages.getRaw(firstMessage.getId());
//		System.out.println("raw: " + raw);
	}
}
