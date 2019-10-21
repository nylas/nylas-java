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

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "Participant [name=" + name + ", email=" + email + ", status=" + status + ", comment=" + comment + "]";
	}
}
