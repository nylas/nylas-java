package com.nylas;

import java.util.ArrayList;
import java.util.List;

public class Availability {
	private final List<TimeSlot> time_slots = new ArrayList<>();
	private final List<String> order = new ArrayList<>();

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
