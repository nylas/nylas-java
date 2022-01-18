package com.nylas.examples.other;

import com.nylas.NylasAccount;
import com.nylas.NylasClient;
import com.nylas.RemoteCollection;
import com.nylas.Thread;
import com.nylas.Threads;
import com.nylas.examples.ExampleConf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ThreadSearchExample {

	private static final Logger log = LogManager.getLogger(ThreadSearchExample.class);

	public static void main(String[] args) throws Exception {
		ExampleConf conf = new ExampleConf();
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(conf.get("access.token"));

		Threads threads = account.threads();
		
		// search
		RemoteCollection<Thread> results = threads.search("kohl's");
		for (Thread thread : results) {
			log.info(thread);
		}	
	}
}
