package com.nylas;

import static com.nylas.Validations.assertState;
import static com.nylas.Validations.nullOrEmpty;

import java.util.Map;

/**
 * Provider settings for Microsoft Office 365.
 * <p>
 * <a href="https://docs.nylas.com/docs/native-authentication#section-provider-specific-settings">
 * https://docs.nylas.com/docs/native-authentication#section-provider-specific-settings</a>
 * <a href="https://docs.nylas.com/docs/o365-oauth-setup">
 * https://docs.nylas.com/docs/o365-oauth-setup</a>
 */
public class MicrosoftOffice365ProviderSettings extends ProviderSettings {

	private String microsoftClientId;
	private String microsoftClientSecret;
	private String microsoftRefreshToken;
	private String redirectUri;
	
	public MicrosoftOffice365ProviderSettings() {
		super("office365");
	}
	
	public String getMicrosoftClientId() {
		return microsoftClientId;
	}

	public String getMicrosoftClientSecret() {
		return microsoftClientSecret;
	}

	public String getMicrosoftRefreshToken() {
		return microsoftRefreshToken;
	}

	public String getRedirectUri() {
		return redirectUri;
	}

	public MicrosoftOffice365ProviderSettings microsoftClientId(String microsoftClientId) {
		this.microsoftClientId = microsoftClientId;
		return this;
	}
	
	public MicrosoftOffice365ProviderSettings microsoftClientSecret(String microsoftClientSecret) {
		this.microsoftClientSecret = microsoftClientSecret;
		return this;
	}
	
	public MicrosoftOffice365ProviderSettings microsoftRefreshToken(String microsoftRefreshToken) {
		this.microsoftRefreshToken = microsoftRefreshToken;
		return this;
	}

	public MicrosoftOffice365ProviderSettings redirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
		return this;
	}

	private void validate() {
		assertState(!nullOrEmpty(microsoftClientId), "Microsoft Client ID is required");
		assertState(!nullOrEmpty(microsoftClientSecret), "Microsoft Client Secret is required");
		assertState(!nullOrEmpty(microsoftRefreshToken), "Microsoft Refresh Token is required");
		assertState(!nullOrEmpty(redirectUri), "Redirect URI is required");
	}
	
	@Override
	protected void fillSettings(Map<String, Object> settings) {
		validate();
		
		settings.put("microsoft_client_id", microsoftClientId);
		settings.put("microsoft_client_secret", microsoftClientSecret);
		settings.put("microsoft_refresh_token", microsoftRefreshToken);
		settings.put("redirect_uri", redirectUri);
	}
	
}
