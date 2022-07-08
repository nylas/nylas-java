package com.nylas;

import java.util.Map;

public class JobStatus extends AccountOwnedModel {

	private String action;
	private Long created_at;
	private String object;
	private String status;
	private Map<String, Object> metadata;
	
	public String getAction() {
		return action;
	}
	
	public Long getCreatedAt() {
		return created_at;
	}
	
	public String getObject() {
		return object;
	}
	
	public String getStatus() {
		return status;
	}

	public Map<String, Object> getMetadata() {
		return metadata;
	}

	/**
	 * Returns the id of this job status object.
	 * <p>
	 * The Nylas HTTP API for job status is unusual in that it returns the id for the job status
	 * in the field called "job_status_id".  It also has a field "id" but that refers to the id
	 * of the object that job is actually for.  The Java SDK fixes this to be more intuitive for
	 * developers:
	 *  <li> JobStatus.getId() returns the id of the job status.
	 *  <li> JobStatus.getJobObjectId() returns the id of the object the job is for.
	 */
	@Override
	public String getId() {
		return super.getJobStatusId();
	}

	/**
	 * Returns the id of object this job is for.
	 * <p>
	 * The Nylas HTTP API for job status is unusual in that it returns the id for the job status
	 * in the field called "job_status_id".  It also has a field "id" but that refers to the id
	 * of the object that job is actually for.  The Java SDK fixes this to be more intuitive for
	 * developers:
	 *  <li> JobStatus.getId() returns the id of the job status.
	 *  <li> JobStatus.getJobObjectId() returns the id of the object the job is for.
	 */
	public String getJobObjectId() {
		return super.getId();
	}

	@Override
	public String toString() {
		return "JobStatus [" +
				"action='" + action + '\'' +
				", created_at=" + created_at +
				", object='" + object + '\'' +
				", status='" + status + '\'' +
				", metadata=" + metadata +
				", jobObjectId=" + getJobObjectId() +
				", accountId=" + getAccountId() +
				", id=" + getId() +
				']';
	}
	
	static class JobStatusJson {
		
	}
	
}
