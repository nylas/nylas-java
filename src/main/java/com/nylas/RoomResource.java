package com.nylas;

public class RoomResource extends AccountOwnedModel implements JsonObject {

	private String name;
	private String email;
	private String capacity;
	private String building;
	private String floor_name;
	private String floor_number;

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public String getFloorName() {
		return floor_name;
	}

	public void setFloorName(String floor_name) {
		this.floor_name = floor_name;
	}

	public String getFloorNumber() {
		return floor_number;
	}

	public void setFloorNumber(String floor_number) {
		this.floor_number = floor_number;
	}

	@Override
	public String toString() {
		return String.format("RoomResource [name=%s, email=%s, capacity=%s, building=%s, floor_name=%s, floor_number=%s]",
				name, email, capacity, building, floor_name, floor_number);
	}

	@Override
	public String getObjectType() {
		return "room_resource";
	}
}
