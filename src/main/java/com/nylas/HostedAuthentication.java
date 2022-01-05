package com.nylas;

import static com.nylas.Validations.assertState;
import static com.nylas.Validations.nullOrEmpty;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import okhttp3.HttpUrl;

/**
 * Support for Nylas Hosted Auth.
 * 
 * Redirect your user to the Hosted Auth service.
 * The user logs into their account and consents to the permissions your app requests.
 * Then, Nylas redirects them back to your app and provide the appropriate authorization credentials.
 */
public class HostedAuthentication {

	private final NylasApplication application;

	HostedAuthentication(NylasApplication application) {
		this.application = application;
	}
	
	/**
	 * Get a builder to construct the hosted auth url to redirect the user to,
	 * after specifying the required parameters.
	 */
	public UrlBuilder urlBuilder() {
		return new UrlBuilder(application);
	}
	
	/**
	 * Exchange the authorization code returned by a successful user authentication for a long lived
	 * access token.  Needed when using responseType of "code" in the hosted auth redirect.
	 */
	public AccessToken fetchToken(String authorizationCode) throws IOException, RequestFailedException {
		HttpUrl.Builder tokenUrl = application.getClient().newUrlBuilder().addPathSegments("oauth/token");
		
		Map<String, Object> params = new HashMap<>();
		params.put("client_id", application.getClientId());
		params.put("client_secret", application.getClientSecret());
		params.put("grant_type", "authorization_code");
		params.put("code", authorizationCode);

		return application.getClient().executePost(null, tokenUrl, params, AccessToken.class);
	}
	
	/**
	 * Builder to construct a url for the destination of directing a user
	 * to user Nylas Hosted Authentication.
	 */
	public static class UrlBuilder {

		private final NylasApplication app;
		
		private String redirectUri = "";
		private String responseType = "code";
		private String scopes = "";
		private String loginHint = "";
		private String state = "";
		private boolean forcePassword = false;

		UrlBuilder(NylasApplication app) {
			this.app = app;
		}
		
		/**
		 * The URI to which the user will be redirected once authentication completes.
		 * This must match a URI registered in the developer dashboard.
		 * The URI should not be encoded - it will be encoded later.
		 */
		public UrlBuilder redirectUri(String redirectUri) {
			this.redirectUri = redirectUri;
			return this;
		}
		
		/**
		 * Determine whether Nylas will include an access token directly or an authorization code
		 * that can then be exchanged for the token.
		 * 
		 * Use "code" for server side applications which can securely exchange it for a token without the token ever being exposed
		 * to the user or user agent. In this case you will need to take the "code" parameter when redirected back to your app
		 * and use HostedAuthentication.fetchToken to exchange it for the access token.
		 * 
		 * Use "token" for client side applications to reduce round trips since they will be handling the token anyway.
		 * In this case you will can take the "token" parameter when redirected back to your app as the access token directly.
		 */
		public UrlBuilder responseType(String responseType) {
			this.responseType = responseType;
			return this;
		}
		
		/**
		 * Comma separated list of scopes being requested.
		 */
		public UrlBuilder scopes(String scopes) {
			this.scopes = scopes;
			return this;
		}
		
		/**
		 * The set of specific scopes being requested.
		 */
		public UrlBuilder scopes(Scope... scopes) {
			StringJoiner joiner = new StringJoiner(",");
			for (Scope scope : scopes) {
				joiner.add(scope.getName());
			}
			return scopes(joiner.toString());
		}
		
		/**
		 * The user's email address if known, to allow the account provider to prefill it.
		 */
		public UrlBuilder loginHint(String loginHint) {
			this.loginHint = loginHint;
			return this;
		}
		
		/**
		 * An optional arbitrary string that is returned as a URL parameter in your redirect URI.
		 * You can pass a value here to keep track of a specific user’s authentication flow.
		 * This may also be used to protect against CSRF attacks.
		 * The maximum length of this string is 255 characters.
		 */
		public UrlBuilder state(String state) {
			this.state = state;
			return this;
		}

		/**
		 * An optional setting for when using a password or wanting to choose a different
		 * provider than auto selected
		 */
		public UrlBuilder forcePassword(boolean forcePassword) {
			this.forcePassword = forcePassword;
			return this;
		}
		
		private void validate() {
			assertState(!nullOrEmpty(redirectUri), "Redirection URI is required");
			assertState(responseType != null && (responseType.equals("code") || responseType.equals("token")),
					"Illegal responseType: %s", responseType);
			assertState(!nullOrEmpty(scopes), "Scopes are required");
		}
		
		/**
		 * Construct the url to direct the user to, including all specified parameters, properly encoded.
		 */
		public String buildUrl() {
			validate();
			
			HttpUrl.Builder urlBuilder = app.getClient().newUrlBuilder()
					.addPathSegments("oauth/authorize")
					.addQueryParameter("client_id", app.getClientId())
					.addQueryParameter("redirect_uri", redirectUri)
					.addQueryParameter("response_type", responseType)
					.addQueryParameter("scopes", scopes)
					;
			if (!nullOrEmpty(loginHint)) {
				urlBuilder.addQueryParameter("login_hint", loginHint);
			}
			if (!nullOrEmpty(state)) {
				urlBuilder.addQueryParameter("state", state);
			}
			if (forcePassword) {
				urlBuilder.addQueryParameter("force_password", "true");
			}
					
			return urlBuilder.build().toString();
		}
	}
}
