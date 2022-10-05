package com.nylas;

import static com.nylas.Validations.assertState;
import static com.nylas.Validations.nullOrEmpty;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;

public class NativeAuthentication {

	private final NylasApplication application;

	/** Supported providers for Native Authentication */
	public enum Provider {
		GOOGLE("google"),
		IMAP("imap"),
		OFFICE_365("office365"),
		EXCHANGE("exchange"),
		YAHOO("yahoo"),
		AOL("aol"),
		HOTMAIL("hotmail"),
		OUTLOOK("outlook"),
		ICLOUD("icloud"),

		;

		private final String name;

		Provider(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public static Provider getProviderByName(String name) {
			for(Provider provider : Provider.values()) {
				if(provider.name.equals(name)) {
					return provider;
				}
			}

			return null;
		}
	}

	NativeAuthentication(NylasApplication application) {
		this.application = application;
	}
	
	public AuthRequestBuilder authRequest() {
		return new AuthRequestBuilder(application);
	}
	
	@SuppressWarnings("unused")  // some fields used only via reflection
	public static class AuthRequestBuilder {
		
		private transient final NylasApplication application;
		
		private final String client_id;
		private String name;
		private String email_address;
		private String provider;
		private Map<String, Object> settings;
		private String scopes;
		private String reauth_account_id;
		
		AuthRequestBuilder(NylasApplication application) {
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
		 */
		public AuthRequestBuilder providerSettings(ProviderSettings providerSettings) {
			this.provider = providerSettings.getName();
			this.settings = providerSettings.getValidatedSettings();
			return this;
		}
		
		/**
		 * The set of specific scopes being requested.
		 * Strongly typed.
		 */
		public AuthRequestBuilder scopes(Scope... scopes) {
			return scopes(Arrays.asList(scopes));
		}
		
		/**
		 * The set of specific scopes being requested.
		 * Strongly typed.
		 */
		public AuthRequestBuilder scopes(Iterable<Scope> scopes) {
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
		
		public String execute() throws IOException, RequestFailedException {
			validate();
			
			HttpUrl authUrl = application.getClient().newUrlBuilder().addPathSegments("connect/authorize").build();
			String json = JsonHelper.adapter(AuthRequestBuilder.class).toJson(this);
			RequestBody body = RequestBody.create(JsonHelper.jsonType(), json);
			Request request = new Request.Builder().url(authUrl).post(body).build();
			
			AuthorizationCode code = application.getClient().executeRequest(request, AuthorizationCode.class);
			return code.getCode();
		}
	}
	
	/**
	 * Exchange the code from sending the native authentication for an access token
	 * @param authorizationCode The code received from the native authentication
	 * @return The access token details
	 */
	public AccessToken fetchToken(String authorizationCode) throws IOException, RequestFailedException {
		Map<String, Object> params = new HashMap<>();
		params.put("client_id", application.getClientId());
		params.put("client_secret", application.getClientSecret());
		params.put("code", authorizationCode);
		
		HttpUrl.Builder tokenUrl = application.getClient().newUrlBuilder().addPathSegments("connect/token");
		return application.getClient().executePost(null, tokenUrl, params, AccessToken.class);
	}
	
	/**
	 * Detect which provider an email uses
	 * @param emailAddress The email address to determine the provider of
	 * @return The details of the provider detection
	 */
	public DetectedProvider detectProvider(String emailAddress) throws RequestFailedException, IOException {
		Map<String, Object> params = new HashMap<>();
		params.put("client_id", application.getClientId());
		params.put("client_secret", application.getClientSecret());
		params.put("email_address", emailAddress);

		HttpUrl.Builder detectUrl = application.getClient().newUrlBuilder().addPathSegments("connect/detect-provider");
		return application.getClient().executePost(null, detectUrl, params, DetectedProvider.class);
	}

	/**
	 * Get settings of a provider detected from an email
	 * @param emailAddress The email address to determine the provider of
	 * @return The settings for the detected provider
	 */
	public ProviderSettings getDetectedProviderSettings(String emailAddress) throws RequestFailedException, IOException {
		DetectedProvider detectedProvider = detectProvider(emailAddress);
		return ProviderSettings.getProviderSettingsByProvider(detectedProvider.getAuthName());
	}

	public static class DetectedProvider {
		private String auth_name;
		private String email_address;
		private String provider_name;
		private Boolean detected;
		private Boolean is_imap;

		/**
		 * The provider's auth name to be used in Native Authentication
		 */
		public String getAuthName() {
			return auth_name;
		}

		/**
		 * The email address being checked
		 */
		public String getEmailAddress() {
			return email_address;
		}

		/**
		 * The name of the detected provider
		 */
		public String getProviderName() {
			return provider_name;
		}

		/**
		 * If this account's mail server settings and provider were auto-detected
		 */
		public Boolean isDetected() {
			return detected;
		}

		/**
		 * If the account should be authenticated with IMAP.
		 * If {@link #isDetected()} returns false, ignore this value.
		 */
		public Boolean isImap() {
			return is_imap;
		}
	}
}
