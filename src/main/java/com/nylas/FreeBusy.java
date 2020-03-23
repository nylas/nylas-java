package com.nylas;

import java.time.Instant;
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

	public static class TimeSlot {
		private String status;
		private Long start_time;
		private Long end_time;
		
		public String getStatus() {
			return status;
		}
		
		public Instant getStartTime() {
			return Instants.toNullableInstant(start_time);
		}
		
		public Instant getEndTime() {
			return Instants.toNullableInstant(end_time);
		}

		@Override
		public String toString() {
			return "TimeSlot [status=" + status + ", start_time=" + getStartTime() + ", end_time=" + getEndTime() + "]";
		}
	}
}
