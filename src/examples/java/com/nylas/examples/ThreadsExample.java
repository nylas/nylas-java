package com.nylas.examples;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.nylas.NylasAccount;
import com.nylas.NylasClient;
import com.nylas.Thread;
import com.nylas.ThreadQuery;
import com.nylas.Threads;

public class ThreadsExample {

	public static void main(String[] args) throws Exception {
		ExampleConf conf = new ExampleConf();
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(conf.get("access.token"));
		Threads threads = account.threads();
		
		Instant end = ZonedDateTime.now().toInstant();
		Instant start = end.minus(1, ChronoUnit.DAYS);
		ThreadQuery query = new ThreadQuery()
				.limit(5)
				.lastMessageAfter(start)
				.lastMessageBefore(end)
				;
		List<Thread> allThreads = threads.list(query);
		System.out.println("result thread count: " + allThreads.size());
		for (Thread thread : allThreads) {
			System.out.println(thread);
		}
	}
}
