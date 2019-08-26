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
		fillSettings(settings);
		return settings;
	}
	
	protected abstract void fillSettings(Map<String, Object> settings);

	
}
