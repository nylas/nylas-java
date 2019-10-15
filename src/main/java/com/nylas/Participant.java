package com.nylas;

public class Participant {

	private String name;
	private String email;
	private String status;
	private String comment;

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

	@Override
	public String toString() {
		return "Participant [name=" + name + ", email=" + email + ", status=" + status + ", comment=" + comment + "]";
	}
}
