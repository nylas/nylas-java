package com.nylas;

import java.util.List;

public class FreeBusy {

	private String email;
	private List<TimeSlot> time_slots;
	
	public String getEmail() {
		return email;
	}

	public List<TimeSlot> getTimeSlots() {
		return time_slots;
	}

	@Override
	public String toString() {
		return "FreeBusy [email=" + email + ", time_slots=" + time_slots + "]";
	}
}
