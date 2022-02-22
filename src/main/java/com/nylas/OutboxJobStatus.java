package com.nylas;

public class OutboxJobStatus extends AccountOwnedModel {

	private String status;
	private OutboxMessage original_data;

	public String getStatus() {
		return status;
	}

	public OutboxMessage getOriginalData() {
		return original_data;
	}

	@Override
	public String toString() {
		return "OutboxJobStatus [" +
				"job_status_id='" + getJobStatusId() + '\'' +
				"account_id='" + getAccountId() + '\'' +
				", status='" + status + '\'' +
				", original_data=" + original_data +
				']';
	}
}
