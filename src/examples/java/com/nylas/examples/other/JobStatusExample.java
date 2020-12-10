package com.nylas.examples.other;

import com.nylas.JobStatus;
import com.nylas.JobStatusQuery;
import com.nylas.JobStatuses;
import com.nylas.NylasAccount;
import com.nylas.NylasClient;
import com.nylas.RemoteCollection;
import com.nylas.examples.ExampleConf;

public class JobStatusExample {

	public static void main(String[] args) throws Exception {
		ExampleConf conf = new ExampleConf();
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(conf.get("access.token"));
		JobStatuses jobStatuses = account.jobStatuses();
		
		long count = jobStatuses.count(new JobStatusQuery());
		System.out.println("Job status count: " + count);
		
		JobStatusQuery query = new JobStatusQuery().limit(50);
		RemoteCollection<JobStatus> allJobStatuses = jobStatuses.list(query);
		JobStatus lastStatus = null;
		for (JobStatus jobStatus: allJobStatuses) {
			System.out.println(jobStatus);
			lastStatus = jobStatus;
		}
		
		if (lastStatus != null) {
			JobStatus jobStatus = jobStatuses.get(lastStatus.getId());
			System.out.println("status: " + jobStatus);
		}
		
	}

}
