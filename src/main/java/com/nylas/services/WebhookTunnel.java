package com.nylas.services;

import com.nylas.*;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Sets up and registers a websocket connection for local webhook testing
 */
public class WebhookTunnel extends WebSocketClient {

	private final NylasApplication app;
	private final String tunnelId;
	private final WebhookHandler webhookHandler;
	private final List<String> triggers;
	private final String region;
	private static final String websocketDomain = "tunnel.nylas.com";
	private static final String callbackDomain = "cb.nylas.com";
	private static final Logger log = LoggerFactory.getLogger(WebhookTunnel.class);

	public WebhookTunnel(Builder builder) throws URISyntaxException {
		super(new URI("wss://" + websocketDomain));
		this.webhookHandler = builder.webhookHandler;
		this.app = builder.app;
		this.tunnelId = UUID.randomUUID().toString();
		this.triggers = builder.triggers;
		this.region = builder.region;
		this.setHeaders();
	}

	/**
	 * {@inheritDoc}
	 * Also registers the webhook with the Nylas API.
	 */
	@Override
	public void connect() {
		super.connect();
		try {
			registerWebhookCallback(app, tunnelId, triggers);
		} catch (RequestFailedException | IOException e) {
			log.trace("Error encountered while trying to register webhook with the Nylas API");
			throw new RuntimeException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 * Calls {@link WebhookHandler#onOpen(short)}
	 */
	@Override
	public void onOpen(ServerHandshake handshakedata) {
		log.trace("Opening websocket connection");
		webhookHandler.onOpen(handshakedata.getHttpStatus());
	}

	/**
	 * {@inheritDoc}
	 * Calls {@link WebhookHandler#onMessage(Notification)}
	 */
	@Override
	public void onMessage(String message) {
		log.trace("Messaged received from websocket");
		Map<String, Object> response = JsonHelper.jsonToMap(message);
		String jsonBody = (String) response.get("body");
		if(jsonBody != null) {
			Notification notification = Notification.parseNotification(jsonBody);
			webhookHandler.onMessage(notification);
		}
		log.trace("Not a valid delta response, skipping.");
	}

	/**
	 * {@inheritDoc}
	 * Calls {@link WebhookHandler#onClose(int, String, boolean)}
	 */
	@Override
	public void onClose(int code, String reason, boolean remote) {
		log.trace("Closing websocket connection");
		webhookHandler.onClose(code, reason, remote);
	}

	/**
	 * {@inheritDoc}
	 * Calls {@link WebhookHandler#onError(Exception)}
	 */
	@Override
	public void onError(Exception ex) {
		log.trace("Error encountered during websocket connection");
		webhookHandler.onError(ex);
	}

	/**
	 * Sets the headers necessary for setting up the websocket tunnel
 	 */
	private void setHeaders() {
		this.addHeader("Client-Id", app.getClientId());
		this.addHeader("Client-Secret", app.getClientSecret());
		this.addHeader("Tunnel-Id", tunnelId);
		this.addHeader("Region", region);
	}

	/**
	 * Registers the websocket connection and the callback with your Nylas application
	 * @param app The configured Nylas application
	 * @param tunnelId The UUID generated for the websocket tunnel
	 * @param triggers The triggers to subscribe to
	 */
	private void registerWebhookCallback(NylasApplication app, String tunnelId, List<String> triggers)
			throws RequestFailedException, IOException {
		Webhook webhook = new Webhook();
		webhook.setCallbackUrl(String.format("https://%s/%s", callbackDomain, tunnelId));
		webhook.setState(Webhook.State.ACTIVE);
		webhook.setTriggers(triggers);
		app.webhooks().create(webhook);
	}

	/**
	 * A builder for {@link WebhookTunnel}
	 */
	public static class Builder {
		private final NylasApplication app;
		private final WebhookHandler webhookHandler;
		private String region = "us";
		private List<String> triggers = convertTriggersToString(Webhook.Trigger.values());

		public Builder(NylasApplication app, WebhookHandler webhookHandler) {
			this.app = app;
			this.webhookHandler = webhookHandler;
		}

		/**
		 * Set the region to configure the websocket to
		 * @param region The Nylas region
		 * @return The builder with the region set
		 */
		public Builder region(String region) {
			this.region = region;
			return this;
		}

		/**
		 * Set the webhook trigger(s) to subscribe to
		 * @param triggers The webhook trigger(s) to subscribe to
		 * @return The builder with the trigger(s) set
		 */
		public Builder triggers(Webhook.Trigger... triggers) {
			this.triggers = convertTriggersToString(triggers);
			return this;
		}

		/**
		 * Builds the WebhookTunnel
		 * @return The configured WebhookTunnel
		 */
		public WebhookTunnel build() throws URISyntaxException {
			return new WebhookTunnel(this);
		}

		/**
		 * Helper method that converts a list of triggers to their string values
		 * @param triggers The list of triggers to convert
		 * @return A list of strings with the value of the provided triggers
		 */
		private static List<String> convertTriggersToString(Webhook.Trigger[] triggers) {
			return Stream.of(triggers)
					.map(Webhook.Trigger::getName)
					.collect(Collectors.toList());
		}
	}

	/**
	 * An interface for implementing classes to handle events from the {@link WebhookTunnel}
	 */
	public interface WebhookHandler {
		void onOpen(short httpStatusCode);
		void onClose(int code, String reason, boolean remote);
		void onMessage(Notification notification);
		void onError(Exception ex);
	}
}
