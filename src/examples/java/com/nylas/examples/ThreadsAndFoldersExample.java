package com.nylas.examples;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Properties;

import com.nylas.Folder;
import com.nylas.FolderQuery;
import com.nylas.Folders;
import com.nylas.NylasClient;
import com.nylas.Thread;
import com.nylas.ThreadQuery;
import com.nylas.Threads;

public class ThreadsAndFoldersExample {

	public static void main(String[] args) throws Exception {
		Properties props = Examples.loadExampleProperties();
		String accessToken = props.getProperty("access.token");
		
		NylasClient client = new NylasClient();
		Threads threads = client.threads(accessToken);
		
		Instant start = LocalDate.of(2019,9,1).atStartOfDay(ZoneId.systemDefault()).toInstant();
		Instant end = LocalDate.of(2019,9,2).atStartOfDay(ZoneId.systemDefault()).toInstant();
		ThreadQuery query = new ThreadQuery()
				.limit(5)
				//.lastMessageAfter(start)
				//.lastMessageBefore(end)
				;
		List<Thread> allThreads = threads.list(query);
		System.out.println("result thread count: " + allThreads.size());
		for (Thread thread : allThreads) {
			System.out.println(thread);
		}
		
//		String threadId = allThreads.get(0).getId();
//		Thread thread = threads.get(threadId);
//		System.out.println(thread);
//
//		
		Folders folders = client.folders(accessToken);
		
		FolderQuery fQuery = new FolderQuery()
				.limit(5)
				.offset(0);
		
		List<Folder> allFolders = folders.list(fQuery);
		Folder inbox = null;
		for (Folder folder : allFolders) {
			System.out.println(folder);
			if ("inbox".equals(folder.getName())) {
				inbox = folder;
			}
		}
		
		Folder newFolder = folders.create("Example Folder 7");
		System.out.println(newFolder);
//		
//		thread = threads.setFolderId(threadId, newFolder.getId());
//		System.out.println(thread);
//		
//		try {
//			folders.delete(newFolder.getId());
//		} catch (RequestFailedException rfe) {
//			System.out.println(rfe.getErrorMessage());
//		}
//		
		
//		thread = threads.setFolderId(threadId, inbox.getId());
//		System.out.println(thread);
//		
//		folders.delete(newFolder.getId());
	}

}
