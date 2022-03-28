package com.nylas;

import com.nylas.UAS.Provider;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;

import java.io.IOException;
import java.util.*;

import static com.nylas.Validations.assertState;
import static com.nylas.Validations.nullOrEmpty;

public class Integration extends RestfulModel {

	/** Name of the integration */
	private String name;
	/** OAuth provider */
	private String provider;
	/**
	 * How long authentication will remain valid, in seconds.
	 * Must be within {@value MINIMUM_EXPIRES_IN_SECONDS} and {@value MAXIMUM_EXPIRES_IN_SECONDS}
	 */
	private Long expires_in;
	/** OAuth provider credentials and settings */
	private Map<String, String> settings = new HashMap<>();
	/** Allowed redirect URIs (for hosted authentication) */
	private List<String> redirect_uris = new ArrayList<>();
	/** OAuth provider-specific scopes */
	private List<String> scope = new ArrayList<>();
	/** Minimum {@link #expires_in} value is 1 minute */
	private static final long MINIMUM_EXPIRES_IN_SECONDS = 60;
	/** Maximum {@link #expires_in} value is 10 years */
	private static final long MAXIMUM_EXPIRES_IN_SECONDS = 315360000;

	/** for deserialization only */ public Integration() {}

	public Integration(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Provider getProvider() {
		return Provider.valueOf(provider.toUpperCase());
	}

	public String getProviderString() {
		return provider;
	}

	public Long getExpiresIn() {
		return expires_in;
	}

	public Map<String, String> getSettings() {
		return settings;
	}

	public List<String> getRedirectUris() {
		return redirect_uris;
	}

	public List<String> getScope() {
		return scope;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setExpiresIn(Long expiresIn) {
		this.expires_in = expiresIn;
	}

	public void setSettings(Map<String, String> settings) {
		this.settings = settings;
	}

	public void setRedirectUris(List<String> redirectUris) {
		this.redirect_uris = redirectUris;
	}

	public void setScope(List<String> scope) {
		this.scope = scope;
	}

	/**
	 * Set the client ID of the OAuth provider
	 * @param clientId Client ID of the OAuth provider
	 */
	public void setClientId(String clientId) {
		this.settings.put("client_id", clientId);
	}

	/**
	 * Set the client secret of the OAuth provider
	 * @param clientSecret Client secret of the OAuth provider
	 */
	public void setClientSecret(String clientSecret) {
		this.settings.put("client_secret", clientSecret);
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
	 * Add redirect URI(s) for hosted authentication
	 * @param redirectUris The redirect URI(s)
	 */
	public void addRedirectUris(String... redirectUris) {
		Collections.addAll(this.redirect_uris, redirectUris);
	}

	/**
	 * Add OAuth provider-specific scope(s)
	 * @param scopes The scope(s)
	 */
	public void addScope(String... scopes) {
		Collections.addAll(this.scope, scopes);
	}

	/**
	 * Checks if the integration is valid
	 * <br>
	 * {@link #name} must be set and, if {@link #expires_in} is set, it must be within expected range
	 * @return The validity of the integration
	 */
	public boolean isValid() {
		return name != null &&
				(expires_in == null || (expires_in > MINIMUM_EXPIRES_IN_SECONDS && expires_in < MAXIMUM_EXPIRES_IN_SECONDS));
	}

	/**
	 * Checks if the provider field is set
	 * @return If the provider field is set
	 */
	public boolean hasProvider() {
		return !Validations.nullOrEmpty(provider);
	}

	/**
	 * Overrides parent as an integration's ID is the OAuth provider
	 * @return The {@link #provider}
	 */
	@Override
	public String getId() {
		return provider;
	}

	/**
	 * Overrides parent as an integration's ID is the OAuth provider
	 * @return If the provider field is set
	 * @see #hasProvider()
	 */
	@Override
	public boolean hasId() {
		return hasProvider();
	}

	@Override
	public String toString() {
		return "Integration [" +
				"name='" + name + '\'' +
				", provider='" + provider + '\'' +
				", expires_in=" + expires_in +
				", settings=" + settings +
				", redirect_uris=" + redirect_uris +
				", scope=" + scope +
				']';
	}

	@Override
	protected Map<String, Object> getWritableFields(boolean creation) {
		Map<String, Object> params = new HashMap<>();
		Maps.putIfNotNull(params, "name", name);
		Maps.putIfNotNull(params, "settings", settings);
		Maps.putIfNotNull(params, "redirect_uris", redirect_uris);
		Maps.putIfNotNull(params, "expires_in", expires_in);
		Maps.putIfNotNull(params, "scope", scope);
		return params;
	}

	void validate() {
		String expiresInErrorMessage = String.format("Expires In value must be between %d and %d seconds.",
				MINIMUM_EXPIRES_IN_SECONDS, MAXIMUM_EXPIRES_IN_SECONDS);

		assertState(!nullOrEmpty(name), "Name is required");
		assertState(expires_in == null || (expires_in > MINIMUM_EXPIRES_IN_SECONDS
						&& expires_in < MAXIMUM_EXPIRES_IN_SECONDS), expiresInErrorMessage);
	}

	/**
	 * These adapters work around the API returning the integration object within a nested "data" key
	 */
	@SuppressWarnings("unchecked")
	static class IntegrationCustomAdapter {
		@FromJson
		Integration fromJson(JsonReader reader, JsonAdapter<Integration> delegate) throws IOException {
			Map<String, Object> json = JsonHelper.jsonToMap(reader);

			if(json.get("data") != null) {
				json = (Map<String, Object>) json.get("data");
			}
			return delegate.fromJson(JsonHelper.mapToJson(json));
		}
	}

	@SuppressWarnings("unchecked")
	static class IntegrationListCustomAdapter {
		@FromJson
		List<Integration> fromJson(JsonReader reader, JsonAdapter<List<Integration>> delegate) throws IOException {
			Map<String, Object> json = JsonHelper.jsonToMap(reader);

			if(json.get("data") == null) {
				return delegate.fromJson("[]");
			}
			List<Object> integrations = (List<Object>) json.get("data");
			return delegate.fromJson(JsonHelper.listToJson(integrations));
		}
	}
}
