package com.nylas.examples;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import com.nylas.Label;
import com.nylas.Labels;
import com.nylas.Message;
import com.nylas.MessageQuery;
import com.nylas.Messages;
import com.nylas.NylasAccount;
import com.nylas.NylasClient;

public class LabelsExample {

	
	public static void main(String[] args) throws Exception {
		Properties props = Examples.loadExampleProperties();
		String accessToken = props.getProperty("access.token");
		
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(accessToken);

		Labels labels = account.labels();
		List<Label> allLabels = labels.list();
		Label inbox = null;
		for (Label label : allLabels) {
			System.out.println(label);
			if ("inbox".equals(label.getName())) {
				inbox = label;
			}
		}
		System.out.println("Inbox label: " + inbox);
		
		Label newLabel = labels.create("Example Label!!");
		System.out.println("created: " + newLabel);
		
		
		Messages messages = account.messages();
		List<Message> messageList = messages.list(new MessageQuery().limit(1));
		if (messageList.isEmpty()) {
			System.out.println("No messages");
			return;
		}
		
		String messageId = messageList.get(0).getId();
		Message message = messages.setLabelIds(messageId, Arrays.asList(newLabel.getId()));
		System.out.println("labelled message: " + message);
	
		labels.delete(newLabel.getId());
		System.out.println("deleted");
	}
}
