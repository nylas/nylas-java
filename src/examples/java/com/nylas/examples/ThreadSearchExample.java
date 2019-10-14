package com.nylas.examples;

import java.util.List;
import java.util.Properties;

import com.nylas.NylasAccount;
import com.nylas.NylasClient;
import com.nylas.SearchQuery;
import com.nylas.Threads;
import com.nylas.Thread;

public class ThreadSearchExample {

	public static void main(String[] args) throws Exception {
		Properties props = Examples.loadExampleProperties();
		String accessToken = props.getProperty("access.token");
		
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(accessToken);

		Threads threads = account.threads();
		
		// search
		List<Thread> results = threads.search(new SearchQuery("kohl's", 5, 0));
		for (Thread thread : results) {
			System.out.println(thread);
		}	
	}
}
