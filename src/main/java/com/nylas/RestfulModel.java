package com.nylas;

public abstract class RestfulModel {

	private String id;

	public String getId() {
		return id;
	}

	public boolean hasId() {
		return id != null;
	}
	
}
