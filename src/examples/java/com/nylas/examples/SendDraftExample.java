package com.nylas.examples;

import com.nylas.Draft;
import com.nylas.Drafts;
import com.nylas.NylasAccount;
import com.nylas.NylasClient;
import com.nylas.Tracking;

public class SendDraftExample {

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
		draft.setSubject("temp subject");
		draft.setBody(conf.get("send.body"));
		
		Draft saved = drafts.create(draft);
		System.out.println("Initial saved draft: " + saved);
		
		saved.setSubject(conf.get("send.subject"));
		saved = drafts.update(saved);
		System.out.println("Updated saved draft: " + saved);
		
		Tracking tracking = new Tracking();
		tracking.setOpens(true);
		tracking.setPayload("send-draft-example");
		saved.setTracking(tracking);
		
		drafts.send(saved);
		System.out.println("Sent");
	}
	

	
}
