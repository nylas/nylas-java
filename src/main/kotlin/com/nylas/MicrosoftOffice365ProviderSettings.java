package com.nylas;

/**
 * Provider settings for Microsoft Office 365.
 * <p>
 * <a href="https://docs.nylas.com/docs/native-authentication#section-provider-specific-settings">
 * https://docs.nylas.com/docs/native-authentication#section-provider-specific-settings</a>
 * <a href="https://docs.nylas.com/docs/o365-oauth-setup">
 * https://docs.nylas.com/docs/o365-oauth-setup</a>
 */
public class MicrosoftOffice365ProviderSettings extends ProviderSettings {

	protected MicrosoftOffice365ProviderSettings() {
		super(NativeAuthentication.Provider.OFFICE_365.getName());
	}

	public MicrosoftOffice365ProviderSettings microsoftClientId(String microsoftClientId) {
		add("microsoft_client_id", microsoftClientId);
		return this;
	}
	
	public MicrosoftOffice365ProviderSettings microsoftClientSecret(String microsoftClientSecret) {
		add("microsoft_client_secret", microsoftClientSecret);
		return this;
	}
	
	public MicrosoftOffice365ProviderSettings microsoftRefreshToken(String microsoftRefreshToken) {
		add("microsoft_refresh_token", microsoftRefreshToken);
		return this;
	}

	public MicrosoftOffice365ProviderSettings redirectUri(String redirectUri) {
		add("redirect_uri", redirectUri);
		return this;
	}

	@Override
	protected void validate() {
		assertSetting("microsoft_client_id", "Microsoft Client ID is required");
		assertSetting("microsoft_client_secret", "Microsoft Client Secret is required");
		assertSetting("microsoft_refresh_token", "Microsoft Refresh Token is required");
		assertSetting("redirect_uri", "Redirect URI is required");
	}
	
}
