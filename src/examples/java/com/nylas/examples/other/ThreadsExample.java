package com.nylas.examples.other;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import com.nylas.NylasAccount;
import com.nylas.NylasClient;
import com.nylas.RemoteCollection;
import com.nylas.Thread;
import com.nylas.ThreadQuery;
import com.nylas.Threads;
import com.nylas.examples.ExampleConf;

public class ThreadsExample {

	public static void main(String[] args) throws Exception {
		ExampleConf conf = new ExampleConf();
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(conf.get("access.token"));
		Threads threads = account.threads();
		
		Instant end = ZonedDateTime.now().toInstant();
		Instant start = end.minus(1, ChronoUnit.DAYS);
		ThreadQuery query = new ThreadQuery()
				.limit(55)
				.lastMessageAfter(start)
//				.lastMessageBefore(end)
				;
//		List<Thread> allThreads = threads.list(query).chunkSize(10).fetchAll();
//		System.out.println("result thread count: " + allThreads.size());
//		for (Thread thread : allThreads) {
//			System.out.println(thread);
//		}
		
		RemoteCollection<Thread> allThreads = threads.list(query).chunkSize(10);
		for (Thread thread : allThreads) {
			System.out.println(thread);
		}

	}
}
