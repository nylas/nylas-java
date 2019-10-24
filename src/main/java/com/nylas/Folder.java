package com.nylas;

public class Folder extends AccountOwnedModel {

	private String name;
	private String display_name;

	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return display_name;
	}

	@Override
	public String toString() {
		return "Folder [id=" + getId() + ", name=" + name + ", display_name=" + display_name + "]";
	}

}
