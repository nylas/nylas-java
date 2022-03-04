package com.nylas;

import java.util.HashMap;
import java.util.Map;

public class Participant {

	private String name;
	private String email;
	private String phone_number;
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

	public String getPhoneNumber() {
		return phone_number;
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

	public Participant phoneNumber(String phoneNumber) {
		this.phone_number = phoneNumber;
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
		Maps.putIfNotNull(params, "phone_number", phone_number);
		Maps.putIfNotNull(params, "comment", comment);
		return params;
	}

	@Override
	public String toString() {
		return "Participant [" +
				"name=" + name +
				"email=" + email +
				"phone_number=" + phone_number +
				"status=" + status +
				"comment=" + comment +
				"]";
	}
}
