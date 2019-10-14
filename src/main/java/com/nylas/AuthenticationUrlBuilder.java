package com.nylas;

import static com.nylas.Validations.assertState;
import static com.nylas.Validations.nullOrEmpty;

import java.util.StringJoiner;

import okhttp3.HttpUrl;

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
	
	public AuthenticationUrlBuilder redirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
		return this;
	}
	
	public AuthenticationUrlBuilder responseType(String responseType) {

		this.responseType = responseType;
		return this;
	}
	
	/**
	 * Comma separated list of scopes requested.
	 * It is recommended to use the other scopes method with the enumerated values instead.
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
	
	public AuthenticationUrlBuilder loginHint(String loginHint) {
		this.loginHint = loginHint;
		return this;
	}
	
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
	
	public String buildUrl() {
		validate();
		
		HttpUrl.Builder urlBuilder = app.getClient().getBaseUrl().newBuilder()
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
