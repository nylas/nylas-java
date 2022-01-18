package com.nylas.examples.other;

import com.nylas.Drafts;
import com.nylas.Message;
import com.nylas.NylasAccount;
import com.nylas.NylasClient;
import com.nylas.examples.ExampleConf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SendRawMimeExample {

	private static final Logger log = LogManager.getLogger(SendRawMimeExample.class);

	public static void main(String[] args) throws Exception {
		ExampleConf conf = new ExampleConf();
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(conf.get("access.token"));
		Drafts drafts = account.drafts();
		String rawMime = conf.get("send.rawmime");
		log.info("Sending raw mime:");
		log.info(rawMime);
		Message sentMessage = drafts.sendRawMime(rawMime);
		log.info("Sent message: " + sentMessage);
	}

}
