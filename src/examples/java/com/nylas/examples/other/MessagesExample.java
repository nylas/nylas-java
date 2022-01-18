package com.nylas.examples.other;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import com.nylas.Message;
import com.nylas.MessageQuery;
import com.nylas.Messages;
import com.nylas.NylasAccount;
import com.nylas.NylasClient;
import com.nylas.RemoteCollection;
import com.nylas.examples.ExampleConf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MessagesExample {

	private static final Logger log = LogManager.getLogger(MessagesExample.class);

	public static void main(String[] args) throws Exception {
		ExampleConf conf = new ExampleConf();
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(conf.get("access.token"));

		Messages messages = account.messages();
		
		Instant end = ZonedDateTime.now().toInstant();
		Instant start = end.minus(1, ChronoUnit.DAYS);
		MessageQuery query = new MessageQuery()
//				.offset(0)
				.limit(30)
//				.hasAttachment(true)
				.receivedAfter(start)
//				.receivedBefore(end)
				//.anyEmail("info@twitter.com")
//				.in("Example Label 2")
				;

		
		RemoteCollection<Message> emails = messages.list(query);
		emails.chunkSize(10);
		
		for (Message email : emails) {
			log.info(email);
		}
		
//		List<String> allIds = ids.fetchAll();
//		log.info("size = " + allIds.size());
		
		
		
		
		
//		long count = messages.count(query);
//		log.info("Count: " + count);
//		
//		List<String> ids = messages.ids(query).fetchAll();
//		log.info("ids size: " + ids.size());
//		
//		List<Message> allMessages = messages.list(query).fetchAll();
//		if (allMessages.isEmpty()) {
//			log.info("No messages");
//			return;
//		}
//		
//		log.info("collection size: " + allMessages.size());
//		
//		Message firstMessage  = allMessages.get(0);
//		for (Message message : allMessages) {
//			log.info(message);
//		}
		
		
		
//		Message message = messages.get(firstMessage.getId());
//		log.info("got by id: " + message);
//		
//		message = messages.setUnread(firstMessage.getId(), !firstMessage.getUnread());
//		log.info("toggled unread: " + message);
//		
//		message = messages.setStarred(firstMessage.getId(), !firstMessage.getStarred());
//		log.info("toggled starred: " + message);
//		
//		String raw = messages.getRaw(firstMessage.getId());
//		log.info("raw: " + raw);

//		Map<String, String> metadata = new HashMap<>();
//		metadata.put("message_type", "test");
//		message = messages.setMetadata(firstMessage.getId(), metadata);
//		log.info("set metadata " + message);
	}
}
