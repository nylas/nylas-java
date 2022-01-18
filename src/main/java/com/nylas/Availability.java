package com.nylas;

import java.util.List;

public class Availability {
	private List<TimeSlot> time_slots;

	public List<TimeSlot> getTimeSlots() {
		return time_slots;
	}

	@Override
	public String toString() {
		return "Availability [time_slots=" + time_slots + "]";
	}
}
