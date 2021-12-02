package com.nylas;

import java.util.ArrayList;
import java.util.List;

public abstract class RestfulModel extends Model {

	private String id;
	
	private String job_status_id;

	public String getId() {
		return id;
	}

	public boolean hasId() {
		return id != null;
	}
	
	/**
	 * For model objects that are returned from the Nylas server as part of
	 * a create or update operation, they may include a job status id to allow
	 * tracking of when the operation has synced back to the underlying provider.
	 */
	public String getJobStatusId() {
		return job_status_id;
	}
	
	public static List<String> getIds(Iterable<? extends RestfulModel> models) {
		List<String> ids = new ArrayList<>();
		for (RestfulModel model : models) {
			ids.add(model.getId());
		}
		return ids;
	}
	
}
