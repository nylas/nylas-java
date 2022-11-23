package com.nylas.services;

import com.nylas.*;
import com.sun.istack.internal.Nullable;

import java.io.IOException;

/**
 * Collection of helpful routes to be implemented into a backend application.
 */
public class Routes {
	private final NylasApplication application;

	public Routes(NylasApplication application) {
		this.application = application;
	}

	/**
	 * Build the URL for authenticating users to your application via Hosted Authentication
	 * @param scopes Authentication scopes to request from the authenticating user
	 * @param emailAddress The user's email address
	 * @param successUrl The URI to which the user will be redirected once authentication completes
	 * @param clientUri The route of the client
	 * @param state An optional arbitrary string that is returned as a URL param in your redirect URI
	 * @return The URL for hosted authentication
	 */
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

	/**
	 * Exchange an authorization code for an access token
	 * @param code One-time authorization code from Nylas
	 * @return The object containing the access token and other information
	 */
	public AccessToken exchangeCodeForToken(String code) throws RequestFailedException, IOException {
		return application.nativeAuthentication().fetchToken(code);
	}

	/**
	 * Verify incoming webhook signature came from Nylas
	 * @param nylasSignature The signature to verify
	 * @param rawBody The raw body from the payload
	 * @return True if the webhook signature was verified from Nylas
	 */
	public boolean verifyWebhookSignature(String nylasSignature, String rawBody) {
		return Notification.isSignatureValid(rawBody, application.getClientSecret(), nylasSignature);
	}

	/**
	 * The default paths the Nylas backend middlewares and frontend SDKs are preconfigured to.
	 */
	public static class Constants {
		public static final String BUILD_AUTH_URL = "/nylas/generate-auth-url";
		public static final String EXCHANGE_CODE_FOR_TOKEN = "/nylas/exchange-mailbox-token";
		public static final String WEBHOOKS = "/nylas/webhook";
	}
}
