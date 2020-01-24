package com.nylas;

/**
 * Provider settings for Microsoft Exchange.
 * <p>
 * <a href="https://docs.nylas.com/docs/native-authentication#section-provider-specific-settings">
 * https://docs.nylas.com/docs/native-authentication#section-provider-specific-settings</a>
 * <a href="https://docs.nylas.com/docs/microsoft">
 * https://docs.nylas.com/docs/microsoft</a>
 */
public class MicrosoftExchangeProviderSettings extends ProviderSettings {

	protected MicrosoftExchangeProviderSettings() {
		super("exchange");
	}

	public MicrosoftExchangeProviderSettings username(String username) {
		add("username", username);
		return this;
	}
	
	public MicrosoftExchangeProviderSettings password(String password) {
		add("password", password);
		return this;
	}
	
	public MicrosoftExchangeProviderSettings easServerHost(String easServerHost) {
		add("eas_server_host", easServerHost);
		return this;
	}

	@Override
	protected void validate() {
		assertSetting("username", "Username is required");
		assertSetting("password", "Password is required");
	}
}
