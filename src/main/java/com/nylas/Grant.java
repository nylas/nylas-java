package com.nylas;

import com.nylas.UAS.Provider;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;

import java.io.IOException;
import java.util.*;

import static com.nylas.Validations.assertState;
import static com.nylas.Validations.nullOrEmpty;

public class Grant extends RestfulModel {

	/** OAuth Provider */
	private String provider;
	/** State value to return after authentication flow is completed  */
	private String state;
	/** Email extracted from the provider */
	private String email;
	/** End user client IP address. Mostly useful for hosted auth */
	private String ip;
	/** If grant is valid or needs re-auth */
	private String grant_status;
	/** End user client/browser information */
	private String user_agent;
	/** Date timestamp of creation of the grant */
	private Long created_at;
	/** Date timestamp of last updated of the grant */
	private Long updated_at;
	/** OAuth provider-specific scopes */
	private List<String> scope = new ArrayList<>();
	/** Settings required by provider */
	private Map<String, String> settings = new HashMap<>();
	/** Metadata to store as part of the grant */
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
	 * Add metadata to the grant
	 * @param metadataKey The key of the metadata
	 * @param metadataValue The value of the metadata
	 */
	public void addMetadata(String metadataKey, Object metadataValue) {
		Maps.putIfNotNull(this.metadata, metadataKey, metadataValue);
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
				"id='" + this.getId() + '\'' +
				", provider='" + provider + '\'' +
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
		assertState(!nullOrEmpty(provider), "Provider is required");
		assertState(!nullOrEmpty(settings), "Settings are required");
	}

	/**
	 * These adapters work around the API returning the grant object within a nested "data" key
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
