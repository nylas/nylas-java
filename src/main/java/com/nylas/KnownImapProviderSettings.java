package com.nylas;

/**
 * Provider settings for providers where Nylas already knows the IMAP configuration
 * 
 * <p>
 * <a href="https://docs.nylas.com/docs/native-authentication#section-provider-specific-settings">
 * https://docs.nylas.com/docs/native-authentication#section-provider-specific-settings</a>
 */
public class KnownImapProviderSettings extends ProviderSettings {

	protected KnownImapProviderSettings(String providerName) {
		super(providerName);
	}

	public KnownImapProviderSettings username(String username) {
		add("username", username);
		return this;
	}
	
	public KnownImapProviderSettings password(String password) {
		add("password", password);
		return this;
	}
	
	@Override
	protected void validate() {
		assertSetting("password", "Password is required");
	}
	
}
