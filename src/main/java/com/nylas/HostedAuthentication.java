package com.nylas;

import java.io.IOException;

import com.squareup.moshi.Moshi;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

public class HostedAuthentication {

	private final NylasApplication application;

	HostedAuthentication(NylasApplication application) {
		this.application = application;
	}
	
	public AuthenticationUrlBuilder urlBuilder() {
		return new AuthenticationUrlBuilder(application);
	}
	
	public AccessToken fetchToken(String authorizationCode) throws IOException {
		
		HttpUrl tokenUrl = application.getClient().getBaseUrl().resolve("oauth/token");
		
		FormBody params = new FormBody.Builder()
				.add("client_id", application.getClientId())
				.add("client_secret", application.getClientSecret())
				.add("grant_type", "authorization_code")
				.add("code", authorizationCode)
				.build();
		
		Request request = new Request.Builder().url(tokenUrl)
				.post(params)
				.build();
		
		try (Response response = application.getClient().getHttpClient().newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new IOException("Unexpected code " + response);
			}
			
			Moshi moshi = new Moshi.Builder().build();
			AccessToken token = moshi.adapter(AccessToken.class).fromJson(response.body().source());
			return token;
		}
	}
	


	
}
