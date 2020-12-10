package com.nylas;

import java.io.IOException;

public class JobStatuses extends RestfulDAO<JobStatus> {

	JobStatuses(NylasClient client, String accessToken) {
		super(client, JobStatus.class, "job-statuses", accessToken);
	}

	public RemoteCollection<JobStatus> list() throws IOException, RequestFailedException {
		return list(new JobStatusQuery());
	}
	
	public RemoteCollection<JobStatus> list(JobStatusQuery query) throws IOException, RequestFailedException {
		return super.list(query);
	}

	@Override
	public JobStatus get(String jobStatusId) throws IOException, RequestFailedException {
		return super.get(jobStatusId);
	}
	
	public long count(JobStatusQuery query) throws IOException, RequestFailedException {
		return super.count(query);
	}
}
