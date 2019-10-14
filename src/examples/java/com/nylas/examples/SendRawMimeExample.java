package com.nylas.examples;

import java.util.Properties;

import com.nylas.Drafts;
import com.nylas.Message;
import com.nylas.NylasAccount;
import com.nylas.NylasClient;

public class SendRawMimeExample {

	public static void main(String[] args) throws Exception {
		Properties props = Examples.loadExampleProperties();
		String accessToken = props.getProperty("access.token");
		String rawMime = props.getProperty("send.rawmime");
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(accessToken);
		Drafts drafts = account.drafts();
		System.out.println("Sending raw mime:");
		System.out.println(rawMime);
		System.out.println();
		Message sentMessage = drafts.sendRawMime(rawMime);
		System.out.println("Sent message: " + sentMessage);
	}

}
