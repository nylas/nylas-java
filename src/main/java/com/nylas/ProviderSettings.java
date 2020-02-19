package com.nylas;

import static com.nylas.Validations.assertState;

import java.util.HashMap;
import java.util.Map;

/**
 * Provider name and provider-specific settings.
 * Strongly typed subclasses provide convenience methods and validation for known provider types.
 * 
 * Or this class can be used directly if Nylas API adds new providers before this SDK is updated.
 * 
 * <p>
 * <a href="https://docs.nylas.com/docs/native-authentication#section-provider-specific-settings">
 * https://docs.nylas.com/docs/native-authentication#section-provider-specific-settings</a>
 */
public class ProviderSettings {

	private final String providerName;
	private final Map<String, Object> settings = new HashMap<>();
	
	public static GoogleProviderSettings google() {
		return new GoogleProviderSettings();
	}
	
	public static ImapProviderSettings imap() {
		return new ImapProviderSettings();
	}
	
	public static MicrosoftOffice365ProviderSettings office365() {
		return new MicrosoftOffice365ProviderSettings();
	}
	
	public static MicrosoftExchangeProviderSettings exchange() {
		return new MicrosoftExchangeProviderSettings();
	}
	
	public static KnownImapProviderSettings knownImap(String provider) {
		return new KnownImapProviderSettings(provider);
	}
	
	/**
	 * Settings with provider set to "yahoo"
	 * <p>
	 * NOTE - Many Yahoo accounts currently require the user to generate an App Password for this to work.
	 */
	public static KnownImapProviderSettings yahoo() {
		return new KnownImapProviderSettings("yahoo");
	}
	
	/**
	 * Settings with provider set to "aol"
	 * <p>
	 * NOTE - Many AOL accounts currently require the user to generate an App Password for this to work.
	 */
	public static KnownImapProviderSettings aol() {
		return new KnownImapProviderSettings("aol");
	}
	
	public static KnownImapProviderSettings hotmail() {
		return new KnownImapProviderSettings("hotmail");
	}
	
	public static KnownImapProviderSettings outlook() {
		return new KnownImapProviderSettings("outlook");
	}
	
	public static KnownImapProviderSettings icloud() {
		return new KnownImapProviderSettings("icloud");
	}
	
	public ProviderSettings(String providerName) {
		this.providerName = providerName;
	}
	
	public String getName() {
		return providerName;
	}
	
	public ProviderSettings add(String key, Object value) {
		settings.put(key, value);
		return this;
	}
	
	public ProviderSettings addAll(Map<String, ? extends Object> entries) {
		settings.putAll(entries);
		return this;
	}
	
	public Map<String, Object> getValidatedSettings() {
		validate();
		return settings;
	}
	
	/**
	 * Strongly typed subclasses may override to check that all required fields are populated.
	 * If not, a runtime exception is thrown.
	 */
	protected void validate() {}

	protected void assertSetting(String key, String message) {
		assertState(settings.containsKey(key), message);
	}
	
	@Override
	public String toString() {
		return "ProviderSettings [providerName=" + providerName + ", settings=" + settings + "]";
	}
	
}
