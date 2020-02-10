package com.nylas.examples.other;

import com.nylas.Drafts;
import com.nylas.Message;
import com.nylas.NylasAccount;
import com.nylas.NylasClient;
import com.nylas.examples.ExampleConf;

public class SendRawMimeExample {

	public static void main(String[] args) throws Exception {
		ExampleConf conf = new ExampleConf();
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(conf.get("access.token"));
		Drafts drafts = account.drafts();
		String rawMime = conf.get("send.rawmime");
		System.out.println("Sending raw mime:");
		System.out.println(rawMime);
		System.out.println();
		Message sentMessage = drafts.sendRawMime(rawMime);
		System.out.println("Sent message: " + sentMessage);
	}

}
