package com.nylas.examples;

import java.util.Properties;

import com.nylas.Draft;
import com.nylas.Drafts;
import com.nylas.NylasClient;

public class SendDraftExample {

	public static void main(String[] args) throws Exception {
		Properties props = Examples.loadExampleProperties();
		String accessToken = props.getProperty("access.token");
		
		NylasClient client = new NylasClient();
		Drafts drafts = client.drafts(accessToken);

		Draft draft = new Draft();
		draft.setFrom(Examples.getNameEmail(props, "send.from.name", "send.from.email"));
		draft.setTo(Examples.getNameEmailList(props, "send.to.name","send.to.email"));
		draft.setCc(Examples.getNameEmailList(props, "send.cc.name", "send.cc.email"));
		draft.setBcc(Examples.getNameEmailList(props, "send.bcc.name", "send.bcc.email"));
		draft.setSubject("temp subject");
		draft.setBody(props.getProperty("send.body"));
		
		Draft saved = drafts.create(draft);
		System.out.println("Initial saved draft: " + saved);
		
		saved.setSubject(props.getProperty("send.subject"));
		saved = drafts.update(saved);
		System.out.println("Updated saved draft: " + saved);
		
		drafts.send(saved);
		System.out.println("Sent");
	}
	

	
}
