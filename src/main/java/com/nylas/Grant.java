package com.nylas;

import com.nylas.Integration.Provider;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;

import java.io.IOException;
import java.util.*;

public class Grant extends RestfulModel {

	private String provider;
	private String state;
	private String email;
	private String ip;
	private String grant_status;
	private String user_agent;
	private Long created_at;
	private Long updated_at;
	private List<String> scope = new ArrayList<>();
	private Map<String, String> settings = new HashMap<>();
	private Map<String, Object> metadata = new HashMap<>();

	/** for deserialization only */ public Grant() {}

	public Grant(Provider provider, Map<String, String> settings) {
		this.provider = provider.toString();
		this.settings = settings;
	}

	public Provider getProvider() {
		return Provider.valueOf(provider.toUpperCase());
	}

	public String getProviderString() {
		return provider;
	}

	public String getState() {
		return state;
	}

	public String getEmail() {
		return email;
	}

	public String getIp() {
		return ip;
	}

	public String getGrantStatus() {
		return grant_status;
	}

	public String getUserAgent() {
		return user_agent;
	}

	public Long getCreatedAt() {
		return created_at;
	}

	public Long getUpdatedAt() {
		return updated_at;
	}

	public List<String> getScope() {
		return scope;
	}

	public Map<String, String> getSettings() {
		return settings;
	}

	public Map<String, Object> getMetadata() {
		return metadata;
	}

	public void setProvider(Provider provider) {
		this.provider = provider.toString();
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setScope(List<String> scope) {
		this.scope = scope;
	}

	public void setSettings(Map<String, String> settings) {
		this.settings = settings;
	}

	public void setMetadata(Map<String, Object> metadata) {
		this.metadata = metadata;
	}

	/**
	 * Add OAuth provider-specific scope(s)
	 * @param scopes The scope(s)
	 */
	public void addScope(String... scopes) {
		Collections.addAll(this.scope, scopes);
	}

	/**
	 * Add an OAuth provider setting
	 * @param settingName The name of the setting
	 * @param settingValue The value of the setting
	 */
	public void addSetting(String settingName, String settingValue) {
		Maps.putIfNotNull(this.settings, settingName, settingValue);
	}

	/**
	 * Add an OAuth provider setting
	 * @param metadataKey The name of the setting
	 * @param metadataValue The value of the setting
	 */
	public void addMetadata(String metadataKey, Object metadataValue) {
		Maps.putIfNotNull(this.metadata, metadataKey, metadataValue);
	}

	/**
	 * Checks if the integration is valid
	 * <br>
	 * {@link #name} must be set and, if {@link #expires_in} is set, it must be within expected range
	 * @return The validity of the integration
	 */
	public boolean isValid() {
		return provider != null && !Validations.nullOrEmpty(this.settings);
	}

	@Override
	protected Map<String, Object> getWritableFields(boolean creation) {
		Map<String, Object> params = new HashMap<>();
		if(creation) {
			Maps.putIfNotNull(params, "provider", provider);
			Maps.putIfNotNull(params, "state", state);
		}
		Maps.putIfNotNull(params, "settings", settings);
		Maps.putIfNotNull(params, "scope", scope);
		Maps.putIfNotNull(params, "metadata", metadata);
		return params;
	}

	@Override
	public String toString() {
		return "Grant [" +
				"provider='" + provider + '\'' +
				", state='" + state + '\'' +
				", email='" + email + '\'' +
				", ip='" + ip + '\'' +
				", grant_status='" + grant_status + '\'' +
				", user_agent='" + user_agent + '\'' +
				", created_at=" + created_at +
				", updated_at=" + updated_at +
				", scope=" + scope +
				", settings=" + settings +
				", metadata=" + metadata +
				']';
	}

	void validate() {
		if(isValid()) {
			return;
		}

		StringJoiner errorMessage = new StringJoiner(" ");

		errorMessage.add("Grant object is not valid:");
		if(provider == null) {
			errorMessage.add("Grant missing required field 'provider'.");
		}
		if(Validations.nullOrEmpty(this.settings)) {
			errorMessage.add("Grant missing required field 'settings'.");
		}

		throw new IllegalArgumentException(errorMessage.toString());
	}

	/**
	 * These adapters work around the API returning the integration object within a nested "data" key
	 */
	@SuppressWarnings("unchecked")
	static class GrantCustomAdapter {
		@FromJson
		Grant fromJson(JsonReader reader, JsonAdapter<Grant> delegate) throws IOException {
			Map<String, Object> json = JsonHelper.jsonToMap(reader);

			if(json.get("data") != null) {
				json = (Map<String, Object>) json.get("data");
			}
			return delegate.fromJson(JsonHelper.mapToJson(json));
		}
	}

	@SuppressWarnings("unchecked")
	static class GrantListCustomAdapter {
		@FromJson
		List<Grant> fromJson(JsonReader reader, JsonAdapter<List<Grant>> delegate) throws IOException {
			Map<String, Object> json = JsonHelper.jsonToMap(reader);

			if(json.get("data") == null) {
				return delegate.fromJson("[]");
			}
			List<Object> integrations = (List<Object>) json.get("data");
			return delegate.fromJson(JsonHelper.listToJson(integrations));
		}
	}
}
