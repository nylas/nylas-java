package com.nylas;

import static com.nylas.Validations.assertState;
import static com.nylas.Validations.nullOrEmpty;

import java.util.StringJoiner;

import okhttp3.HttpUrl;

/**
 * Builder to construct a url for the destination of directing a user
 * to user Nylas Hosted Authentication.
 */
public class AuthenticationUrlBuilder {

	private final NylasApplication app;
	
	private String redirectUri = "";
	private String responseType = "code";
	private String scopes = "";
	private String loginHint = "";
	private String state = "";

	AuthenticationUrlBuilder(NylasApplication app) {
		this.app = app;
	}
	
	/**
	 * The URI to which the user will be redirected once authentication completes.
	 * This must match a URI registered in the developer dashboard.
	 * The URI should not be encoded - it will be encoded later.
	 */
	public AuthenticationUrlBuilder redirectUri(String redirectUri) {
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
	public AuthenticationUrlBuilder responseType(String responseType) {
		this.responseType = responseType;
		return this;
	}
	
	/**
	 * Comma separated list of scopes being requested.
	 */
	public AuthenticationUrlBuilder scopes(String scopes) {
		this.scopes = scopes;
		return this;
	}
	
	/**
	 * The set of specific scopes being requested.
	 */
	public AuthenticationUrlBuilder scopes(Scope... scopes) {
		StringJoiner joiner = new StringJoiner(",");
		for (Scope scope : scopes) {
			joiner.add(scope.getName());
		}
		return scopes(joiner.toString());
	}
	
	/**
	 * The user's email address if known, to allow the account provider to prefill it.
	 */
	public AuthenticationUrlBuilder loginHint(String loginHint) {
		this.loginHint = loginHint;
		return this;
	}
	
	/**
	 * An optional arbitrary string that is returned as a URL parameter in your redirect URI.
	 * You can pass a value here to keep track of a specific user’s authentication flow.
	 * This may also be used to protect against CSRF attacks.
	 * The maximum length of this string is 255 characters.
	 */
	public AuthenticationUrlBuilder state(String state) {
		this.state = state;
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
				
		return urlBuilder.build().toString();
	}
}
