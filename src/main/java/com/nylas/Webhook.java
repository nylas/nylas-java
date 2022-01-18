package com.nylas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Webhook extends RestfulModel {

	private String application_id;
	private String callback_url;
	private String state;
	private List<String> triggers;
	private String version;
	
	public String getApplicationId() {
		return application_id;
	}
	
	public String getCallbackUrl() {
		return callback_url;
	}
	
	public String getState() {
		return state;
	}
	
	public List<String> getTriggers() {
		return triggers;
	}
	
	public String getVersion() {
		return version;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callback_url = callbackUrl;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setTriggers(List<String> triggers) {
		this.triggers = triggers;
	}

	@Override
	public String toString() {
		return "Webhook [application_id=" + application_id + ", callback_url=" + callback_url + ", state=" + state
				+ ", triggers=" + triggers + ", version=" + version + "]";
	}
	
	@Override
	protected Map<String, Object> getWritableFields(boolean creation) {
		Map<String, Object> params = new HashMap<>();
		if (creation) {
			Maps.putIfNotNull(params, "callback_url", getCallbackUrl());
			Maps.putIfNotNull(params, "triggers", getTriggers());
		}
		Maps.putIfNotNull(params, "state", getState());
		return params;
	}
}
