package com.nylas;

import java.util.HashMap;
import java.util.Map;

public class Participant {

	private String name;
	private String email;
	private String status;
	private String comment;
	
	/** For deserialiation only */ public Participant() {}
	
	/**
	 * Email is required for participants
	 */
	public Participant(String email) {
		this.email = email;
	}
	
	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getStatus() {
		return status;
	}

	public String getComment() {
		return comment;
	}

	public Participant name(String name) {
		this.name = name;
		return this;
	}

	@Deprecated
	public Participant status(String status) {
		this.status = status;
		return this;
	}

	public Participant comment(String comment) {
		this.comment = comment;
		return this;
	}

	Map<String, Object> getWritableFields() {
		Map<String, Object> params = new HashMap<>();
		Maps.putIfNotNull(params, "name", name);
		Maps.putIfNotNull(params, "email", email);
		Maps.putIfNotNull(params, "comment", comment);
		return params;
	}

	@Override
	public String toString() {
		return "Participant [name=" + name + ", email=" + email + ", status=" + status + ", comment=" + comment + "]";
	}
}
