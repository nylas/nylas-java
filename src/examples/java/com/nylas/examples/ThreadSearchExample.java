package com.nylas.examples;

import java.util.List;

import com.nylas.NylasAccount;
import com.nylas.NylasClient;
import com.nylas.Thread;
import com.nylas.Threads;

public class ThreadSearchExample {

	public static void main(String[] args) throws Exception {
		ExampleConf conf = new ExampleConf();
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(conf.get("access.token"));

		Threads threads = account.threads();
		
		// search
		List<Thread> results = threads.search("kohl's", 5, 0);
		for (Thread thread : results) {
			System.out.println(thread);
		}	
	}
}
