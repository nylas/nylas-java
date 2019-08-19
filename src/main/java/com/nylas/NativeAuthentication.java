package com.nylas;

import static com.nylas.Validations.assertState;
import static com.nylas.Validations.nullOrEmpty;

import java.util.StringJoiner;

import com.squareup.moshi.Moshi;

import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class NativeAuthentication {

	private final Application application;

	NativeAuthentication(Application application) {
		this.application = application;
	}
	
	public AuthRequestBuilder authRequest() {
		return new AuthRequestBuilder(application);
	}
	
	public static class AuthRequestBuilder {
		
		private transient final Application application;
		
		private String name;
		private String email_address;
		private String provider;
		private ProviderSettings settings;
		private String scopes;
		private String reauth_account_id;
	
		
		AuthRequestBuilder(Application application) {
			super();
			this.application = application;
		}

		public AuthRequestBuilder name(String name) {
			this.name = name;
			return this;
		}
		
		public AuthRequestBuilder emailAddress(String emailAddress) {
			this.email_address = emailAddress;
			return this;
		}
		
		public AuthRequestBuilder providerSettings(ProviderSettings providerSettings) {
			this.provider = providerSettings.getName();
			this.settings = providerSettings;
			return this;
		}
		
		/**
		 * Comma separated list of scopes requested.
		 * It is recommended to use the other scopes method with the enumerated values instead.
		 */
		public AuthRequestBuilder scopes(String scopes) {
			this.scopes = scopes;
			return this;
		}
		
		/**
		 * The set of specific scopes being requested.
		 */
		public AuthRequestBuilder scopes(Scope... scopes) {
			StringJoiner joiner = new StringJoiner(",");
			for (Scope scope : scopes) {
				joiner.add(scope.getName());
			}
			return scopes(joiner.toString());
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
		
		private String getRequestJSON() {
			return null;
		}
		
		public String execute() {
			validate();
			
			HttpUrl revokeUrl = application.getClient().getBaseUrl().resolve("connect/authorize");
			MediaType jsonType = MediaType.parse("application/json; charset=utf-8");
			RequestBody body = RequestBody.create(jsonType, "");

			
			Request request = new Request.Builder().url(revokeUrl)
					.post(body)
					//.addHeader("Authorization", Credentials.basic(accessToken, ""))
					.build();
			return null;
		}

	}
	
	
	public static void main(String[] args) {
		ImapProviderSettings settings = new ImapProviderSettings()
				.imapHost("imap.gmail.com")
				.imapPort(587)
				;
		NylasClient client = new NylasClient();
		Application application = client.application("eookptrxndc091k0w30jtczyy", "7c7gi98svph4o2tcgbrondah1");
		NativeAuthentication authentication = application.nativeAuthentication();
		AuthRequestBuilder authRequest = authentication.authRequest();
		authRequest.emailAddress("davenylastest@gmail.com");
		authRequest.name("David Latham");
		authRequest.scopes(Scope.CALENDAR_ALL, Scope.EMAIL_DRAFTS);
		authRequest.providerSettings(settings);
		
		Moshi moshi = new Moshi.Builder().build();
		String json = moshi.adapter(AuthRequestBuilder.class).toJson(authRequest);
		System.out.println(json);
		

		
	}
	
}
