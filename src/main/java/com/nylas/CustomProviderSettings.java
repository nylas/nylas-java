package com.nylas;

import java.util.HashMap;
import java.util.Map;

/**
 * Generic provider settings to fill in as the app wishes.
 * Could be used if Nylas API adds new providers before this SDK is updated.
 * 
 * <p>
 * <a href="https://docs.nylas.com/docs/native-authentication#section-provider-specific-settings">
 * https://docs.nylas.com/docs/native-authentication#section-provider-specific-settings</a>
 */
public class CustomProviderSettings extends ProviderSettings {

	private Map<String, String> settings = new HashMap<>();
	
	public CustomProviderSettings(String providerName) {
		super(providerName);
	}

	public CustomProviderSettings add(String key, String value) {
		settings.put(key, value);
		return this;
	}
	
	@Override
	protected void validate() {
		// nothing to validate 
	}
	
	@Override
	protected void fillSettings(Map<String, Object> settings) {
		settings.putAll(this.settings);
	}
	
}
