package com.nylas;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.HttpUrl;

public class HostedAuthentication {

	private final NylasApplication application;

	HostedAuthentication(NylasApplication application) {
		this.application = application;
	}
	
	public AuthenticationUrlBuilder urlBuilder() {
		return new AuthenticationUrlBuilder(application);
	}
	
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
