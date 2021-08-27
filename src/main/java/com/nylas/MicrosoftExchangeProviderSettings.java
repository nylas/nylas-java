package com.nylas;

/**
 * Provider settings for Microsoft Exchange.
 * <p>
 * <a href="https://docs.nylas.com/docs/native-authentication#section-provider-specific-settings">
 * https://docs.nylas.com/docs/native-authentication#section-provider-specific-settings</a>
 * <a href="https://developer.nylas.com/docs/the-basics/provider-guides/microsoft/microsoft-authentication/#native-authentication">
 * https://developer.nylas.com/docs/the-basics/provider-guides/microsoft/microsoft-authentication/#native-authentication</a>
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
	
	/**
	 * @deprecated Use exchangeServerHost instead
	 */
	@Deprecated
	public MicrosoftExchangeProviderSettings easServerHost(String easServerHost) {
		return exchangeServerHost(easServerHost);
	}
	
	public MicrosoftExchangeProviderSettings exchangeServerHost(String exchangeServerHost) {
		add("exchange_server_host", exchangeServerHost);
		return this;
	}

	@Override
	protected void validate() {
		assertSetting("username", "Username is required");
		assertSetting("password", "Password is required");
	}
}
