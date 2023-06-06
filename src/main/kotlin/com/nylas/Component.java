package com.nylas;

import java.util.*;

public class Component extends RestfulModel {

	private String name;
	private String type;
	private String public_application_id;
	private String public_token_id;
	private String public_account_id;
	private String access_token;
	private Integer action;
	private Boolean active;
	private Date created_at;
	private Date updated_at;
	private Map<String, Object> settings;
	private List<String> allowed_domains;

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public Integer getAction() {
		return action;
	}

	public Boolean isActive() {
		return active;
	}

	public String getPublicAccountId() {
		return public_account_id;
	}

	public String getPublicTokenId() {
		return public_token_id;
	}

	public String getPublicApplicationId() {
		return public_application_id;
	}

	public String getAccessToken() {
		return access_token;
	}

	public Date getCreatedAt() {
		return created_at;
	}

	public Date getUpdatedAt() {
		return updated_at;
	}

	public Map<String, Object> getSettings() {
		return settings;
	}

	public List<String> getAllowedDomains() {
		return allowed_domains;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setAction(Integer action) {
		this.action = action;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public void setPublicAccountId(String publicAccountId) {
		this.public_account_id = publicAccountId;
	}

	public void setPublicTokenId(String publicTokenId) {
		this.public_token_id = publicTokenId;
	}

	public void setPublicApplicationId(String publicApplicationId) {
		this.public_application_id = publicApplicationId;
	}

	public void setAccessToken(String accessToken) {
		this.access_token = accessToken;
	}

	public void setSettings(Map<String, Object> settings) {
		this.settings = settings;
	}

	public void setAllowedDomains(List<String> allowedDomains) {
		this.allowed_domains = allowedDomains;
	}

	public boolean addAllowedDomains(String... allowedDomains) {
		if(this.allowed_domains == null) {
			this.allowed_domains = new ArrayList<>();
		}
		return Collections.addAll(this.allowed_domains, allowedDomains);
	}

	public boolean addSetting(String settingName, Object settingValue) {
		if(this.settings == null) {
			this.settings = new HashMap<>();
		}
		this.settings.put(settingName, settingValue);
		return true;
	}

	@Override
	protected Map<String, Object> getWritableFields(boolean creation) {
		Map<String, Object> params = new HashMap<>();

		if(creation) {
			Maps.putIfNotNull(params, "type", type);
			Maps.putIfNotNull(params, "public_application_id", public_application_id);
			Maps.putIfNotNull(params, "access_token", access_token);
		}

		Maps.putIfNotNull(params, "name", name);
		Maps.putIfNotNull(params, "public_token_id", public_token_id);
		Maps.putIfNotNull(params, "public_account_id", public_account_id);
		Maps.putIfNotNull(params, "action", action);
		Maps.putIfNotNull(params, "active", active);
		Maps.putIfNotNull(params, "settings", settings);
		Maps.putIfNotNull(params, "allowed_domains", allowed_domains);
		return params;
	}

	@Override
	public String toString() {
		return "Component [" +
				"name='" + name + '\'' +
				", type='" + type + '\'' +
				", public_application_id='" + public_application_id + '\'' +
				", public_token_id='" + public_token_id + '\'' +
				", public_account_id='" + public_account_id + '\'' +
				", access_token='" + access_token + '\'' +
				", action=" + action +
				", active=" + active +
				", created_at=" + created_at +
				", updated_at=" + updated_at +
				", settings=" + settings +
				", allowed_domains=" + allowed_domains +
				']';
	}
}
