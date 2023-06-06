package com.nylas;

import java.io.IOException;

/**
 * <a href="https://docs.nylas.com/reference#webhooks">https://docs.nylas.com/reference#webhooks</a>
 */
public class Webhooks extends RestfulDAO<Webhook> {

	Webhooks(NylasClient client, NylasApplication application) {
		super(client, Webhook.class, "a/" + application.getClientId() + "/webhooks", application.getClientSecret());
	}
	
	public RemoteCollection<Webhook> list() throws IOException, RequestFailedException {
		return list(new WebhookQuery());
	}
	
	public RemoteCollection<Webhook> list(WebhookQuery query) throws IOException, RequestFailedException {
		return super.list(query);
	}

	@Override
	public Webhook get(String id) throws IOException, RequestFailedException {
		return super.get(id);
	}

	@Override
	public Webhook create(Webhook webhook) throws IOException, RequestFailedException {
		return super.create(webhook);
	}

	@Override
	public Webhook update(Webhook webhook) throws IOException, RequestFailedException {
		return super.update(webhook);
	}

	/**
	 * Delete the webhook with the given id.
	 * The server does not yet return a job status for webhook deletion,
	 * so this returns null.
	 */
	@Override
	public String delete(String id) throws IOException, RequestFailedException {
		return super.delete(id);
	}
	
}
