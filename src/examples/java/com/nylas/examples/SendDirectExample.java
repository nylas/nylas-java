package com.nylas.examples;

import com.nylas.Draft;
import com.nylas.Drafts;
import com.nylas.Message;
import com.nylas.NylasAccount;
import com.nylas.NylasClient;
import com.nylas.Tracking;

public class SendDirectExample {

	public static void main(String[] args) throws Exception {
		ExampleConf conf = new ExampleConf();
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(conf.get("access.token"));
		Drafts drafts = account.drafts();

		Draft draft = new Draft();
		draft.setFrom(conf.getNameEmail("send.from"));
		draft.setTo(conf.getNameEmailList("send.to"));
		draft.setCc(conf.getNameEmailList("send.cc"));
		draft.setBcc(conf.getNameEmailList("send.bcc"));
		draft.setSubject(conf.get("send.subject"));
		draft.setBody(conf.get("send.body"));
		
		Tracking tracking = new Tracking();
		tracking.setOpens(true);
		tracking.setLinks(true);
		tracking.setThreadReplies(true);
		tracking.setPayload("send-direct-example");
		draft.setTracking(tracking);
		
		Message sentMessage = drafts.send(draft);
		System.out.println("Sent message: " + sentMessage);
	}

}
