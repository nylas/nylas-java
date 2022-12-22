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

	public void setEmail(String email) {
		this.email = email;
	}

	public void setTimeSlots(List<TimeSlot> timeSlots) {
		this.time_slots = timeSlots;
	}

	@Override
	public String toString() {
		return "FreeBusy [email=" + email + ", time_slots=" + time_slots + "]";
	}
}
