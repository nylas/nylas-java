package com.nylas;

import com.squareup.moshi.Json;
import com.squareup.moshi.adapters.EnumJsonAdapter;

import java.util.Map;

public class JobStatus extends AccountOwnedModel {

	private String action;
	private Long created_at;
	private String object;
	private String status;
	private Map<String, Object> metadata;

	/** Known actions for job status */
	public enum Action {
		@Json(name="create_calendar")
		CREATE_CALENDAR,
		@Json(name="update_calendar")
		UPDATE_CALENDAR,
		@Json(name="delete_calendar")
		DELETE_CALENDAR,
		@Json(name="create_contact")
		CREATE_CONTACT,
		@Json(name="update_contact")
		UPDATE_CONTACT,
		@Json(name="delete_contact")
		DELETE_CONTACT,
		@Json(name="create_folder")
		CREATE_FOLDER,
		@Json(name="update_folder")
		UPDATE_FOLDER,
		@Json(name="delete_folder")
		DELETE_FOLDER,
		@Json(name="create_label")
		CREATE_LABEL,
		@Json(name="update_label")
		UPDATE_LABEL,
		@Json(name="create_event")
		CREATE_EVENT,
		@Json(name="update_event")
		UPDATE_EVENT,
		@Json(name="delete_event")
		DELETE_EVENT,
		@Json(name="update_message")
		UPDATE_MESSAGE,
		@Json(name="save_draft")
		SAVE_DRAFT,
		@Json(name="new_outbox")
		NEW_OUTBOX,

		UNKNOWN;

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}
	}
	
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toJSON() {
		return JsonHelper.objectToJson(JobStatus.class, this);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, JobStatus> toMap() {
		return (Map<String, JobStatus>) JsonHelper.adapter(JobStatus.class).toJsonValue(this);
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

	/**
	 * Adapter for deserializing job actions as enums.
	 * Actions from the API that are not matched returns {@link Action#UNKNOWN}
	 */
	public static final EnumJsonAdapter<Action> JOB_STATUS_ACTIONS_ADAPTER =
			EnumJsonAdapter.create(Action.class).withUnknownFallback(Action.UNKNOWN);
}
