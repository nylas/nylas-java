package com.nylas.examples.other;

import java.util.Arrays;
import java.util.List;

import com.nylas.Label;
import com.nylas.Labels;
import com.nylas.Message;
import com.nylas.MessageQuery;
import com.nylas.Messages;
import com.nylas.NylasAccount;
import com.nylas.NylasClient;
import com.nylas.RemoteCollection;
import com.nylas.examples.ExampleConf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LabelsExample {

	private static final Logger log = LogManager.getLogger(LabelsExample.class);

	public static void main(String[] args) throws Exception {
		ExampleConf conf = new ExampleConf();
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(conf.get("access.token"));

		Labels labels = account.labels();
		RemoteCollection<Label> allLabels = labels.list();
		Label inbox = null;
		for (Label label : allLabels) {
			log.info(label);
			if ("inbox".equals(label.getName())) {
				inbox = label;
			}
		}
		log.info("Inbox label: " + inbox);
		
		Label newLabel = labels.create("Example Label!!");
		log.info("created: " + newLabel);
		
		
		Messages messages = account.messages();
		List<Message> messageList = messages.list(new MessageQuery().limit(1)).fetchAll();
		if (messageList.isEmpty()) {
			log.info("No messages");
			return;
		}
		
		String messageId = messageList.get(0).getId();
		Message message = messages.setLabelIds(messageId, Arrays.asList(newLabel.getId()));
		log.info("labelled message: " + message);
		
		Label newLabel2 = labels.create("Another Example Label");
		log.info("created: " + newLabel2);
		boolean result = messages.addLabel(messageId, newLabel2.getId());
		log.info("attempted to add another label.  success=" + result);
		
		result = messages.addLabel(messageId, newLabel2.getId());
		log.info("attempted to add the same one again.  success=" + result);
		
		message = messages.get(messageId);
		log.info("added another label to message: " + message);
		
		labels.delete(newLabel.getId());
		labels.delete(newLabel2.getId());
		log.info("deleted the new labels");
	}
}
