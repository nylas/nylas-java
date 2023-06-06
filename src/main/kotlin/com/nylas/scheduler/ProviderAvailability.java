package com.nylas.scheduler;

import java.util.List;

public class ProviderAvailability {

	private String name;
	private String email;
	private List<Busy> busy;

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public List<Busy> getBusy() {
		return busy;
	}

	@Override
	public String toString() {
		return "ProviderAvailability [" +
				"name='" + name + '\'' +
				", email='" + email + '\'' +
				", busy=" + busy +
				']';
	}

	class Busy {

		private Long start;
		private Long end;

		public Long getStart() {
			return start;
		}

		public Long getEnd() {
			return end;
		}

		@Override
		public String toString() {
			return "Busy [" +
					"start=" + start +
					", end=" + end +
					']';
		}
	}
}
