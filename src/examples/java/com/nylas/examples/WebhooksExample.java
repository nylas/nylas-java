package com.nylas.examples;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import com.nylas.NylasApplication;
import com.nylas.NylasClient;
import com.nylas.Webhook;
import com.nylas.Webhooks;

public class WebhooksExample {

	public static void main(String[] args) throws Exception {
		Properties props = Examples.loadExampleProperties();
		NylasClient client = new NylasClient();
		NylasApplication application = client.application(props.getProperty("nylas.client.id"),
				props.getProperty("nylas.client.secret"));
		Webhooks webhooks = application.webhooks();
		List<Webhook> webhookList = webhooks.list();
		for (Webhook webhook : webhookList) {
			System.out.println(webhook);
		}
		
		//Webhook webhook = webhooks.get("5jxt7iuly3oi2h7j6d9yehnr9");
		//System.out.println(webhook);
		
		String callbackUrl = props.getProperty("webhook.callback.url");
		Webhook newWebhook = new Webhook();
		newWebhook.setCallbackUrl(callbackUrl);
		newWebhook.setTriggers(Arrays.asList(
				"account.connected",
				"account.running",
				"account.stopped",
				"account.invalid",
				"account.sync_error",
				"message.created",
				"message.opened",
				"message.link_clicked",
				"thread.replied",
				"contact.created",
				"contact.updated",
				"contact.deleted",
				"calendar.created",
				"calendar.updated",
				"calendar.deleted",
				"event.created",
				"event.updated",
				"event.deleted"));
		
		
		Webhook created = webhooks.create(newWebhook);
		System.out.println("Created: " + created);
		
//		created.setState("inactive");
//		Webhook updated = webhooks.update(created);
//		System.out.println("Updated: " + updated);
//		
//		webhooks.delete(updated.getId());
//		System.out.println("Deleted.");
	}

}
