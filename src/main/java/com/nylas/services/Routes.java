package com.nylas.services;

import com.nylas.*;
import com.sun.istack.internal.Nullable;

import java.io.IOException;

public class Routes {
	private final NylasApplication application;

	public Routes(NylasApplication application) {
		this.application = application;
	}

	public String buildAuthUrl(
			Scope[] scopes,
			String emailAddress,
			String successUrl,
			@Nullable String clientUri,
			@Nullable String state
	) {
		clientUri = clientUri != null ? clientUri : "";
		HostedAuthentication.UrlBuilder authUrl = application
				.hostedAuthentication()
				.urlBuilder()
				.scopes(scopes)
				.loginHint(emailAddress)
				.redirectUri(clientUri + successUrl);

		if(state != null) {
			authUrl = authUrl.state(state);
		}

		return authUrl.buildUrl();
	}

	public AccessToken exchangeCodeForToken(String code) throws RequestFailedException, IOException {
		return application.nativeAuthentication().fetchToken(code);
	}

	public boolean verifyWebhookSignature(String nylasSignature, String rawBody) {
		return Notification.isSignatureValid(rawBody, application.getClientSecret(), nylasSignature);
	}

	public enum DefaultRoutes {
		BUILD_AUTH_URL("/nylas/generate-auth-url"),
		EXCHANGE_CODE_FOR_TOKEN("/nylas/exchange-mailbox-token"),
		WEBHOOKS("/nylas/webhook"),

		;

		private final String path;

		DefaultRoutes(String path) {
			this.path = path;
		}

		public String getPath() {
			return path;
		}
	}
}
