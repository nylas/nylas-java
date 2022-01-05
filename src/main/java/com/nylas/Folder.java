package com.nylas;

import java.util.HashMap;
import java.util.Map;

public class Folder extends AccountOwnedModel implements JsonObject {

	private String name;
	private String display_name;

	@Override
	public String getObjectType() {
		return "folder";
	}
	
	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return display_name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDisplayName(String displayName) {
		this.display_name = displayName;
	}

	@Override
	Map<String, Object> getWritableFields(boolean creation) {
		Map<String, Object> params = new HashMap<>();
		Maps.putIfNotNull(params, "name", getName());
		Maps.putIfNotNull(params, "display_name", getDisplayName());
		return params;
	}

	@Override
	public String toString() {
		return "Folder [id=" + getId() + ", name=" + name + ", display_name=" + display_name + "]";
	}

}
