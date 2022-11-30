package com.nylas.server_bindings;

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

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		log.trace("Opening websocket connection");
		webhookHandler.onOpen(handshakedata.getHttpStatus());
	}

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

	@Override
	public void onClose(int code, String reason, boolean remote) {
		log.trace("Closing websocket connection");
		webhookHandler.onClose(code, reason, remote);
	}

	@Override
	public void onError(Exception ex) {
		log.trace("Error encountered during websocket connection");
		webhookHandler.onError(ex);
	}

	private void setHeaders() {
		this.addHeader("Client-Id", app.getClientId());
		this.addHeader("Client-Secret", app.getClientSecret());
		this.addHeader("Tunnel-Id", tunnelId);
		this.addHeader("Region", region);
	}

	private void registerWebhookCallback(NylasApplication app, String tunnelId, List<String> triggers)
			throws RequestFailedException, IOException {
		Webhook webhook = new Webhook();
		webhook.setCallbackUrl(String.format("https://%s/%s", callbackDomain, tunnelId));
		webhook.setState(Webhook.State.ACTIVE);
		webhook.setTriggers(triggers);
		app.webhooks().create(webhook);
	}

	public static class Builder {
		private final NylasApplication app;
		private final WebhookHandler webhookHandler;
		private String region = "us";
		private List<String> triggers = convertScopesToString(Webhook.Trigger.values());

		public Builder(NylasApplication app, WebhookHandler webhookHandler) {
			this.app = app;
			this.webhookHandler = webhookHandler;
		}

		public Builder region(String region) {
			this.region = region;
			return this;
		}

		public Builder triggers(Webhook.Trigger... triggers) {
			this.triggers = convertScopesToString(triggers);
			return this;
		}

		public WebhookTunnel build() throws URISyntaxException {
			return new WebhookTunnel(this);
		}

		private static List<String> convertScopesToString(Webhook.Trigger[] triggers) {
			return Stream.of(triggers)
					.map(Webhook.Trigger::getName)
					.collect(Collectors.toList());
		}
	}

	public interface WebhookHandler {
		void onOpen(short httpStatusCode);
		void onClose(int code, String reason, boolean remote);
		void onMessage(Notification notification);
		void onError(Exception ex);
	}
}
