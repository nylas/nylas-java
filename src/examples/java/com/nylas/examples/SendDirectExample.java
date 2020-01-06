package com.nylas.examples;

import java.util.Properties;

import com.nylas.Draft;
import com.nylas.Drafts;
import com.nylas.Message;
import com.nylas.NylasAccount;
import com.nylas.NylasClient;
import com.nylas.Tracking;

public class SendDirectExample {

	public static void main(String[] args) throws Exception {
		Properties props = Examples.loadExampleProperties();
		String accessToken = props.getProperty("access.token");
		
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(accessToken);
		Drafts drafts = account.drafts();

		Draft draft = new Draft();
		draft.setFrom(Examples.getNameEmail(props, "send.from.name", "send.from.email"));
		draft.setTo(Examples.getNameEmailList(props, "send.to.name","send.to.email"));
		draft.setCc(Examples.getNameEmailList(props, "send.cc.name", "send.cc.email"));
		draft.setBcc(Examples.getNameEmailList(props, "send.bcc.name", "send.bcc.email"));
		draft.setSubject(props.getProperty("send.subject"));
		draft.setBody(props.getProperty("send.body"));
		
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
