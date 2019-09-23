package com.nylas;

import static com.nylas.Validations.assertState;
import static com.nylas.Validations.nullOrEmpty;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import com.squareup.moshi.Moshi;

import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NativeAuthentication {

	private final Application application;

	NativeAuthentication(Application application) {
		this.application = application;
	}
	
	public AuthRequestBuilder authRequest() {
		return new AuthRequestBuilder(application);
	}
	
	@SuppressWarnings("unused")  // some fields used only via reflection
	public static class AuthRequestBuilder {
		
		private transient final Application application;
		
		private final String client_id;
		private String name;
		private String email_address;
		private String provider;
		private Map<String, Object> settings;
		private String scopes;
		private String reauth_account_id;
		
		AuthRequestBuilder(Application application) {
			this.application = application;
			this.client_id = application.getClientId();
		}

		public AuthRequestBuilder name(String name) {
			this.name = name;
			return this;
		}
		
		public AuthRequestBuilder emailAddress(String emailAddress) {
			this.email_address = emailAddress;
			return this;
		}
		
		/**
		 * Provider-specific name and configuration.
		 * Strongly typed.
		 */
		public AuthRequestBuilder providerSettings(ProviderSettings providerSettings) {
			return providerSettings(providerSettings.getName(), providerSettings.getSettings());
		}
		
		public AuthRequestBuilder providerSettings(String providerName, Map<String, Object> providerSettings) {
			this.provider = providerName;
			this.settings = providerSettings;
			return this;
		}
		
		/**
		 * The set of specific scopes being requested.
		 * Strongly typed.
		 */
		public AuthRequestBuilder scopes(Scope... scopes) {
			StringJoiner joiner = new StringJoiner(",");
			for (Scope scope : scopes) {
				joiner.add(scope.getName());
			}
			return scopes(joiner.toString());
		}
		
		/**
		 * Comma separated list of scopes requested.
		 * It is recommended to use the other scopes method with the enumerated values instead.
		 */
		public AuthRequestBuilder scopes(String scopes) {
			this.scopes = scopes;
			return this;
		}
		
		public AuthRequestBuilder reauthAccountId(String reauthAccountId) {
			this.reauth_account_id = reauthAccountId;
			return this;
		}
		
		private void validate() {
			assertState(!nullOrEmpty(name), "Name is required");
			assertState(!nullOrEmpty(email_address), "Email Address is required");
			assertState(settings != null, "Provider Settings required");
		}
		
		public String execute() throws IOException {
			validate();
			
			Moshi moshi = new Moshi.Builder().build();
			String json = moshi.adapter(AuthRequestBuilder.class).toJson(this);
			
			HttpUrl authUrl = application.getClient().getBaseUrl().resolve("connect/authorize");
			RequestBody body = RequestBody.create(JsonHelper.jsonType(), json);
			
			Request request = new Request.Builder().url(authUrl)
					.post(body)
					.build();
			
			try (Response response = application.getClient().getHttpClient().newCall(request).execute()) {
				if (!response.isSuccessful()) {
					throw new IOException("Unexpected code " + response);
				}
				
				AuthorizationCode code = moshi.adapter(AuthorizationCode.class).fromJson(response.body().source());
				return code.getCode();
			}
		}
	}
	
	public AccessToken fetchToken(String authorizationCode) throws IOException {
		
		HttpUrl tokenUrl = application.getClient().getBaseUrl().resolve("connect/token");
		
		Map<String, Object> params = new HashMap<>();
		params.put("client_id", application.getClientId());
		params.put("client_secret", application.getClientSecret());
		params.put("code", authorizationCode);
		
		RequestBody body = JsonHelper.jsonRequestBody(params);
		Request request = new Request.Builder().url(tokenUrl)
				.post(body)
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
