package com.nylas;

// TODO - how is label different from Folder?
public class Label {
	private String id;
	private String name;
	private String display_name;
	
	@Override
	public String toString() {
		return "Label [id=" + id + ", name=" + name + ", display_name=" + display_name + "]";
	}
}