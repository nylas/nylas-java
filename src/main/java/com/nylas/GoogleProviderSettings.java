package com.nylas;

import static com.nylas.Validations.assertState;
import static com.nylas.Validations.nullOrEmpty;

import java.util.Map;

/**
 * Provider settings for Google.
 * 
 * @see https://docs.nylas.com/docs/native-authentication#section-provider-specific-settings
 * @see https://docs.nylas.com/docs/native-authentication-setup-for-google-oauth
 */
public class GoogleProviderSettings extends ProviderSettings {

	private String googleClientId;
	private String googleClientSecret;
	private String googleRefreshToken;
	
	public GoogleProviderSettings() {
		super("gmail");
	}
	
	public GoogleProviderSettings googleClientId(String googleClientId) {
		this.googleClientId = googleClientId;
		return this;
	}
	
	public GoogleProviderSettings googleClientSecret(String googleClientSecret) {
		this.googleClientSecret = googleClientSecret;
		return this;
	}
	
	public GoogleProviderSettings googleRefreshToken(String googleRefreshToken) {
		this.googleRefreshToken = googleRefreshToken;
		return this;
	}

	private void validate() {
		assertState(!nullOrEmpty(googleClientId), "Google Client ID is required");
		assertState(!nullOrEmpty(googleClientSecret), "Google Client Secret is required");
		assertState(!nullOrEmpty(googleRefreshToken), "Google Refresh Token is required");
	}
	
	@Override
	protected void fillSettings(Map<String, String> settings) {
		validate();
		
		settings.put("google_client_id", googleClientId);
		settings.put("google_client_secret", googleClientSecret);
		settings.put("google_refresh_token", googleRefreshToken);
	}
	
}
