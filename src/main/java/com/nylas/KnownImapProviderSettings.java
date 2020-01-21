package com.nylas;

import static com.nylas.Validations.assertState;
import static com.nylas.Validations.nullOrEmpty;

import java.util.Map;

/**
 * Provider settings for providers where Nylas already knows the IMAP configuration
 * 
 * <p>
 * <a href="https://docs.nylas.com/docs/native-authentication#section-provider-specific-settings">
 * https://docs.nylas.com/docs/native-authentication#section-provider-specific-settings</a>
 */
public class KnownImapProviderSettings extends ProviderSettings {

	private String password;
	
	public KnownImapProviderSettings(String providerName) {
		super(providerName);
	}

	public static KnownImapProviderSettings aol() {
		return new KnownImapProviderSettings("aol");
	}
	
	public static KnownImapProviderSettings hotmail() {
		return new KnownImapProviderSettings("hotmail");
	}
	
	public static KnownImapProviderSettings icloud() {
		return new KnownImapProviderSettings("icloud");
	}
	
	public static KnownImapProviderSettings outlook() {
		return new KnownImapProviderSettings("outlook");
	}
	
	public static KnownImapProviderSettings yahoo() {
		return new KnownImapProviderSettings("yahoo");
	}
	
	public String getPassword() {
		return password;
	}

	public KnownImapProviderSettings password(String password) {
		this.password = password;
		return this;
	}
	
	@Override
	protected void validate() {
		assertState(!nullOrEmpty(password), "Password is required");
	}
	
	@Override
	protected void fillSettings(Map<String, Object> settings) {
		settings.put("password", password);
	}
	
}
