package com.nylas;

import com.nylas.Authentication.Provider;
import com.nylas.NylasClient.AuthMethod;
import okhttp3.HttpUrl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nylas.Validations.assertState;
import static com.nylas.Validations.nullOrEmpty;

public class IntegrationHostedAuthentication {

	private final NylasClient client;
	private final String authUser;
	private final HttpUrl.Builder endpointUrl;
	private static final AuthMethod authMethod = AuthMethod.BASIC_WITH_CREDENTIALS;

	public IntegrationHostedAuthentication(NylasClient client, String authUser, HttpUrl.Builder baseUrl) {
		this.client = client;
		this.authUser = authUser;
		this.endpointUrl = baseUrl.addPathSegments("connect/auth");
	}

	/**
	 * Get a new instance of {@link RequestBuilder}
	 * @return A new instance of {@link RequestBuilder}
	 */
	public RequestBuilder requestBuilder() {
		return new RequestBuilder();
	}

	/**
	 * Make a hosted authentication request
	 * @param hostedAuthRequest The request
	 * @return The login information
	 */
	public LoginInfo request(RequestBuilder hostedAuthRequest) throws RequestFailedException, IOException {
		return client.executePost(authUser, endpointUrl, hostedAuthRequest.build(), LoginInfo.class, authMethod);
	}

	/**
	 * Builder for a hosted authentication request
	 * <br> Note that a {@link #provider} and {@link #redirect_uri} need to be set or else an error will be thrown
	 */
	public static class RequestBuilder {

		private String provider;
		private String redirect_uri;
		private String grant_id;
		private String login_hint;
		private String state;
		private Long expires_in;
		private List<String> scope;
		private Map<String, String> settings;
		private Map<String, Object> metadata;

		/** OAuth provider */
		public RequestBuilder provider(Provider provider) {
			this.provider = provider.toString();
			return this;
		}

		/** The URI for the final redirect */
		public RequestBuilder redirectUri(String redirectUri) {
			this.redirect_uri = redirectUri;
			return this;
		}

		/** Existing Grant ID to trigger a re-authentication */
		public RequestBuilder grantId(String grantId) {
			this.grant_id = grantId;
			return this;
		}

		/** Hint to simplify the login flow */
		public RequestBuilder loginHint(String loginHint) {
			this.login_hint = loginHint;
			return this;
		}

		/** State value to return after authentication flow is completed */
		public RequestBuilder state(String state) {
			this.state = state;
			return this;
		}

		/** How long this request (and the attached login) ID will remain valid before the link expires */
		public RequestBuilder expiresIn(long expiresIn) {
			this.expires_in = expiresIn;
			return this;
		}

		/** OAuth provider-specific scopes */
		public RequestBuilder scope(List<String> scope) {
			this.scope = scope;
			return this;
		}

		/** Settings required by provider */
		public RequestBuilder settings(Map<String, String> settings) {
			this.settings = settings;
			return this;
		}

		/** Metadata to store as part of the grant */
		public RequestBuilder metadata(Map<String, Object> metadata) {
			this.metadata = metadata;
			return this;
		}

		private void validate() {
			assertState(!nullOrEmpty(this.provider), "Provider is required");
			assertState(!nullOrEmpty(this.redirect_uri), "Redirect URI is required");
		}

		private Map<String, Object> build() {
			validate();

			Map<String, Object> request = new HashMap<>();
			Maps.putIfNotNull(request, "provider", provider);
			Maps.putIfNotNull(request, "redirect_uri", redirect_uri);
			Maps.putIfNotNull(request, "settings", settings);
			Maps.putIfNotNull(request, "scope", scope);
			Maps.putIfNotNull(request, "grant_id", grant_id);
			Maps.putIfNotNull(request, "metadata", metadata);
			Maps.putIfNotNull(request, "login_hint", login_hint);
			Maps.putIfNotNull(request, "state", state);
			Maps.putIfNotNull(request, "expires_in", expires_in);
			return request;
		}
	}
}
