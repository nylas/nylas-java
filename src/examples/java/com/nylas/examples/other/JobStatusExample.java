package com.nylas.examples.other;

import com.nylas.JobStatus;
import com.nylas.JobStatusQuery;
import com.nylas.JobStatuses;
import com.nylas.NylasAccount;
import com.nylas.NylasClient;
import com.nylas.RemoteCollection;
import com.nylas.examples.ExampleConf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JobStatusExample {

	private static final Logger log = LogManager.getLogger(JobStatusExample.class);

	public static void main(String[] args) throws Exception {
		ExampleConf conf = new ExampleConf();
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(conf.get("access.token"));
		JobStatuses jobStatuses = account.jobStatuses();
		
		long count = jobStatuses.count(new JobStatusQuery());
		log.info("Job status count: " + count);
		
		JobStatusQuery query = new JobStatusQuery().limit(50);
		RemoteCollection<JobStatus> allJobStatuses = jobStatuses.list(query);
		JobStatus lastStatus = null;
		for (JobStatus jobStatus: allJobStatuses) {
			log.info(jobStatus);
			lastStatus = jobStatus;
		}
		
		if (lastStatus != null) {
			JobStatus jobStatus = jobStatuses.get(lastStatus.getId());
			log.info("status: " + jobStatus);
		}
		
	}

}
