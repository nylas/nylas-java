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
	private Map<String, String> metadata;
	
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

	public Map<String, String> getMetadata() {
		return metadata;
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

	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}

	/**
	 * Convenience method to add a metadata pair to an calendar.
	 *
	 * @return true if the metadata was newly added, and false if overwriteIfExists is false the metadata key already exists
	 */
	public boolean addMetadata(String key, String value, boolean overwriteIfExists) {
		if(!overwriteIfExists && metadata.containsKey(key)) {
			return false;
		}
		metadata.put(key, value);
		return true;
	}

	public boolean addMetadata(String key, String value) {
		return addMetadata(key, value, true);
	}

	/**
	 * Convenience method to remove a metadata pair from an calendar.
	 *
	 * @return true if the metadata pair was removed, and false if the calendar did not have the metadata key
	 */
	public boolean removeMetadata(String key) {
		return metadata.remove(key) != null;
	}
	
	@Override
	protected Map<String, Object> getWritableFields(boolean creation) {
		Map<String, Object> params = new HashMap<>();
		Maps.putIfNotNull(params, "name", getName());
		Maps.putIfNotNull(params, "description", getDescription());
		Maps.putIfNotNull(params, "location", location);
		Maps.putIfNotNull(params, "timezone", timezone);
		Maps.putIfNotNull(params, "metadata", metadata);
		return params;
	}

	@Override
	public String toString() {
		return "Calendar [name=" + name + ", description=" + description + ", location=" + location + ", timezone="
				+ timezone + ", readOnly=" + read_only + ", isPrimary=" + is_primary + ", metadata=" + metadata + "]";
	}
}
