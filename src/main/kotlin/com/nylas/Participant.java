package com.nylas;

import java.util.HashMap;
import java.util.Map;

public class Participant {

	/** The participant's full name */
	private String name;
	/** The participant's email address */
	private String email;
	/** The participant's phone number */
	private String phone_number;
	/** The participant's attendance status. See {@link Participant.Status} for allowed values. */
	private String status;
	/** A comment for the participant */
	private String comment;

	/** Supported values for participant status */
	public enum Status {
		YES,
		NO,
		MAYBE,
		NOREPLY,

		;

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}
	}
	
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

	/** Set the participant's full name */
	public Participant name(String name) {
		this.name = name;
		return this;
	}

	/** Set the participant's phone number */
	public Participant phoneNumber(String phoneNumber) {
		this.phone_number = phoneNumber;
		return this;
	}

	/** Set the participant's attendance status */
	public Participant status(Status status) {
		this.status = status.toString();
		return this;
	}

	/**
	 * Set the participant's attendance status
	 * @deprecated Use {@link #status(Status)} instead
	 */
	@Deprecated
	public Participant status(String status) {
		this.status = status;
		return this;
	}

	/** Set a comment for the participant */
	public Participant comment(String comment) {
		this.comment = comment;
		return this;
	}

	/**
	 * Serializes the object to a map with only the writable fields
	 * @param creation If the object is being created
	 * @return The object as a map
	 */
	Map<String, Object> getWritableFields(boolean creation) {
		Map<String, Object> params = new HashMap<>();
		if (creation) {
			Maps.putIfNotNull(params, "status", status);
		}
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
