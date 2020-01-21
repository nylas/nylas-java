package com.nylas;

import static com.nylas.Validations.assertState;
import static com.nylas.Validations.nullOrEmpty;

import java.util.Map;

/**
 * Provider settings for Microsoft Exchange.
 * <p>
 * <a href="https://docs.nylas.com/docs/native-authentication#section-provider-specific-settings">
 * https://docs.nylas.com/docs/native-authentication#section-provider-specific-settings</a>
 * <a href="https://docs.nylas.com/docs/microsoft">
 * https://docs.nylas.com/docs/microsoft</a>
 */
public class MicrosoftExchangeProviderSettings extends ProviderSettings {

	private String username;
	private String password;
	private String easServerHost;
	
	public MicrosoftExchangeProviderSettings() {
		super("exchange");
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getEasServerHost() {
		return easServerHost;
	}

	public MicrosoftExchangeProviderSettings username(String username) {
		this.username = username;
		return this;
	}
	
	public MicrosoftExchangeProviderSettings password(String password) {
		this.password = password;
		return this;
	}
	
	public MicrosoftExchangeProviderSettings easServerHost(String easServerHost) {
		this.easServerHost = easServerHost;
		return this;
	}

	@Override
	protected void validate() {
		assertState(!nullOrEmpty(username), "Username is required");
		assertState(!nullOrEmpty(password), "Password is required");
	}
	
	@Override
	protected void fillSettings(Map<String, Object> settings) {
		settings.put("username", username);
		settings.put("password", password);
		if (!nullOrEmpty(easServerHost)) {
			settings.put("eas_server_host", easServerHost);
		}
	}
	
}
