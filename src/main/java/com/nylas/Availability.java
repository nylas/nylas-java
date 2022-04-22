package com.nylas;

import java.util.List;

public class Availability {
	private List<TimeSlot> time_slots;
	private List<String> order;

	public List<TimeSlot> getTimeSlots() {
		return time_slots;
	}
	public List<String> getOrder() {
		return order;
	}

	@Override
	public String toString() {
		return "Availability [time_slots=" + time_slots + ", order=" + order + "]";
	}
}
