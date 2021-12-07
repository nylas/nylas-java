package com.nylas;

import java.util.HashMap;
import java.util.Map;

public class Calendar extends AccountOwnedModel {

	private String name;
	private String description;
	private String location;
	private String timezone;
	private Boolean read_only;
	private Boolean is_primary;
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getLocation() {
		return location;
	}

	public String getTimezone() {
		return timezone;
	}

	public Boolean isReadOnly() {
		return read_only;
	}
	
	public Boolean isPrimary() {
		return is_primary;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	
	@Override
	Map<String, Object> getWritableFields(boolean creation) {
		Map<String, Object> params = new HashMap<>();
		Maps.putIfNotNull(params, "name", getName());
		Maps.putIfNotNull(params, "description", getDescription());
		Maps.putIfNotNull(params, "location", location);
		Maps.putIfNotNull(params, "timezone", timezone);
		return params;
	}

	@Override
	public String toString() {
		return "Calendar [name=" + name + ", description=" + description + ", location=" + location + ", timezone="
				+ timezone + ", readOnly=" + read_only + ", isPrimary=" + is_primary + "]";
	}
}
