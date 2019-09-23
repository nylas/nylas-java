package com.nylas;

public class Label {

	private String id;
	private String name;
	private String display_name;
	
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return display_name;
	}

	@Override
	public String toString() {
		return "Label [id=" + id + ", name=" + name + ", display_name=" + display_name + "]";
	}
}