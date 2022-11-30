package com.nylas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Webhook extends RestfulModel {

	private String application_id;
	private String callback_url;
	private String state;
	private List<String> triggers;
	private String version;

	/**
	 * Enumeration containing the different webhook triggers
	 */
	public enum Trigger {
		AccountConnected("account.connected"),
		AccountRunning("account.running"),
		AccountStopped("account.stopped"),
		AccountInvalid("account.invalid"),
		AccountSyncError("account.sync_error"),
		MessageBounced("message.bounced"),
		MessageCreated("message.created"),
		MessageOpened("message.opened"),
		MessageUpdated("message.updated"),
		MessageLinkClicked("message.link_clicked"),
		ThreadReplied("thread.replied"),
		ContactCreated("contact.created"),
		ContactUpdated("contact.updated"),
		ContactDeleted("contact.deleted"),
		CalendarCreated("calendar.created"),
		CalendarUpdated("calendar.updated"),
		CalendarDeleted("calendar.deleted"),
		EventCreated("event.created"),
		EventUpdated("event.updated"),
		EventDeleted("event.deleted"),
		JobSuccessful("job.successful"),
		JobFailed("job.failed");

		private final String name;

		Trigger(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	public enum State {
		ACTIVE,
		INACTIVE;
	}
	
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

	public void setState(State state) {
		this.state = state.toString().toLowerCase();
	}

	public void setTriggers(List<String> triggers) {
		this.triggers = triggers;
	}

	public void setTriggers(Trigger... triggers) {
		this.triggers = Stream.of(triggers)
				.map(Trigger::name)
				.collect(Collectors.toList());
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
