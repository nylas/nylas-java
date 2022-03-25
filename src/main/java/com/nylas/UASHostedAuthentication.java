package com.nylas;

import com.nylas.UAS.Provider;
import com.nylas.NylasClient.AuthMethod;
import okhttp3.HttpUrl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nylas.Validations.assertState;
import static com.nylas.Validations.nullOrEmpty;

public class UASHostedAuthentication {

	private final NylasClient client;
	private final String authUser;
	private final HttpUrl.Builder endpointUrl;
	private static final AuthMethod authMethod = AuthMethod.BASIC_WITH_CREDENTIALS;

	public UASHostedAuthentication(NylasClient client, String authUser, HttpUrl.Builder baseUrl) {
		this.client = client;
		this.authUser = authUser;
		this.endpointUrl = baseUrl.addPathSegments("connect/auth");
	}

	public RequestBuilder requestBuilder() {
		return new RequestBuilder();
	}

	public UASLoginInfo request(RequestBuilder hostedAuthRequest) throws RequestFailedException, IOException {
		return client.executePost(authUser, endpointUrl, hostedAuthRequest.build(), UASLoginInfo.class, authMethod);
	}

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

		public RequestBuilder provider(Provider provider) {
			this.provider = provider.toString();
			return this;
		}

		public RequestBuilder redirectUri(String redirectUri) {
			this.redirect_uri = redirectUri;
			return this;
		}

		public RequestBuilder grantId(String grantId) {
			this.grant_id = grantId;
			return this;
		}

		public RequestBuilder loginHint(String loginHint) {
			this.login_hint = loginHint;
			return this;
		}

		public RequestBuilder state(String state) {
			this.state = state;
			return this;
		}

		public RequestBuilder expiresIn(long expiresIn) {
			this.expires_in = expiresIn;
			return this;
		}

		public RequestBuilder scope(List<String> scope) {
			this.scope = scope;
			return this;
		}

		public RequestBuilder settings(Map<String, String> settings) {
			this.settings = settings;
			return this;
		}

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
