package com.nylas;

public class Calendar extends AccountOwnedModel {

	private String name;
	private String description;
	private Boolean read_only;
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Boolean isReadOnly() {
		return read_only;
	}

	@Override
	public String toString() {
		return "Calendar [name=" + name + ", description=" + description + ", read_only=" + read_only + "]";
	}
	
}
