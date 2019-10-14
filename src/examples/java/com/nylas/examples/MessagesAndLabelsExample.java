package com.nylas.examples;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Properties;

import com.nylas.Message;
import com.nylas.MessageQuery;
import com.nylas.Messages;
import com.nylas.NylasAccount;
import com.nylas.NylasClient;

public class MessagesAndLabelsExample {

	public static void main(String[] args) throws Exception {
		Properties props = Examples.loadExampleProperties();
		String accessToken = props.getProperty("access.token");
		
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(accessToken);

		Messages messages = account.messages();
		
		Instant start = LocalDate.of(2019,9,7).atStartOfDay(ZoneId.systemDefault()).toInstant();
		Instant end = LocalDate.of(2019,9,8).atStartOfDay(ZoneId.systemDefault()).toInstant();
		MessageQuery query = new MessageQuery()
				.limit(10)
				.hasAttachment(true)
				//.receivedAfter(start)
				//.receivedBefore(end)
				//.anyEmail("info@twitter.com")
//				.in("Example Label 2")
				;

		
		List<Message> allMessages = messages.list(query);
		Message firstMessage = allMessages.get(0);
		for (Message message : allMessages) {
			System.out.println(message);
		}
		
		String messageId = "88hhp3qq9xin9lovkxmneqdct";
		String labelId = "89g7aoatr4u4br5ldxri5cxct";
		
		
//		List<ExpandedMessage> allExpandedMessages = messages.expanded(query);
//		for (ExpandedMessage message : allExpandedMessages) {
//			System.out.println(message);
//		}
		

//		String raw = messages.getRaw(firstMessage.getId());
//		System.out.println(raw);

		
		Message message = messages.get(firstMessage.getId());
		System.out.println(message);
		
		message = messages.setUnread(firstMessage.getId(), !firstMessage.getUnread());
		System.out.println(message);
		
		message = messages.setStarred(firstMessage.getId(), !firstMessage.getStarred());
		System.out.println(message);

//		Labels labels = client.labels(accessToken);
//		List<Label> allLabels = labels.list();
//		Label inbox = null;
//		for (Label label : allLabels) {
//			System.out.println(label);
//			if ("inbox".equals(label.getName())) {
//				inbox = label;
//			}
//			
////			if (label.getDisplayName().startsWith("Example")) {
////				labels.delete(label.getId());
////				System.out.println("Deleted: " + label);
////			}
//		}
//		System.out.println("Inbox label: " + inbox);
//		
//	//	Label newLabel1 = labels.create("Example Label 1");
//	//	System.out.println(newLabel1);
//		Label newLabel2 = labels.create("Example Label 3");
//		System.out.println(newLabel2);
////		
////		String messageId = firstMessage.getId();
//		message = messages.setLabelIds(messageId, Arrays.asList("89g7aoatr4u4br5ldxri5cxct"));
//		System.out.println(message);
		
		
		
//		message = messages.setFolderId(firstMessage.getId(), newFolder.getId());
//		try {
//			folders.delete(newFolder.getId());
//		} catch (RequestFailedException rfe) {
//			System.out.println(rfe.getErrorMessage());
//		}
//
//		message = messages.setFolderId(firstMessage.getId(), inbox.getId());
//		System.out.println(message);
//		
//		folders.delete(newFolder.getId());
	}

}
