package com.nylas.examples.webhooks;

import java.util.Arrays;

import com.nylas.NylasApplication;
import com.nylas.NylasClient;
import com.nylas.RemoteCollection;
import com.nylas.Webhook;
import com.nylas.Webhooks;
import com.nylas.examples.ExampleConf;

public class WebhooksExample {

	public static void main(String[] args) throws Exception {
		ExampleConf conf = new ExampleConf();
		NylasClient client = new NylasClient();
		NylasApplication application = client.application(conf.get("nylas.client.id"), conf.get("nylas.client.secret"));
		Webhooks webhooks = application.webhooks();
		RemoteCollection<Webhook> webhookList = webhooks.list();
		for (Webhook webhook : webhookList) {
			System.out.println(webhook);
		}
		
		//Webhook webhook = webhooks.get("5jxt7iuly3oi2h7j6d9yehnr9");
		//System.out.println(webhook);
		
		Webhook newWebhook = new Webhook();
		newWebhook.setCallbackUrl(conf.get("webhook.callback.url"));
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
				"event.deleted",
				"job.successful",
				"job.failed"));
		
		
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
