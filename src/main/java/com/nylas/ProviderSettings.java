package com.nylas;

import java.util.HashMap;
import java.util.Map;

public abstract class ProviderSettings {

	private final transient String providerName;
	
	public ProviderSettings(String providerName) {
		this.providerName = providerName;
	}
	
	public String getName() {
		return providerName;
	}
	
	public Map<String, Object> getSettings() {
		Map<String, Object> settings = new HashMap<>();
		validate();
		fillSettings(settings);
		return settings;
	}
	
	/**
	 * Subclasses override to check that all required fields are populated.
	 * If not, a runtime exception is thrown.
	 */
	protected abstract void validate();
	
	/**
	 * Subclasses override to populate the given settings map with the provider specific settings
	 */
	protected abstract void fillSettings(Map<String, Object> settings);

	
}
