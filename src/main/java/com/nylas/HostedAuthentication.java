package com.nylas;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
	public AuthenticationUrlBuilder urlBuilder() {
		return new AuthenticationUrlBuilder(application);
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
	
}
