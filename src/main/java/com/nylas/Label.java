package com.nylas;

public class Label extends AccountOwnedModel implements JsonObject {

	private String name;
	private String display_name;

	/** for deserialization only */ public Label() {}

	public Label(String displayName) {
		this.display_name = displayName;
	}

	@Override
	public String getObjectType() {
		return "label";
	}
	
	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return display_name;
	}

	@Override
	public String toString() {
		return "Label [id=" + getId() + ", name=" + name + ", display_name=" + display_name + "]";
	}
}