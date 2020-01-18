package com.nylas.examples;

import java.util.List;

import com.nylas.Folder;
import com.nylas.FolderQuery;
import com.nylas.Folders;
import com.nylas.NylasAccount;
import com.nylas.NylasClient;
import com.nylas.RequestFailedException;
import com.nylas.Thread;
import com.nylas.ThreadQuery;
import com.nylas.Threads;


public class FoldersExample {
	
	public static void main(String[] args) throws Exception {
		ExampleConf conf = new ExampleConf();
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(conf.get("access.token"));
		
		Folders folders = account.folders();
		
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
		
		Threads threads = account.threads();
		List<Thread> threadList = threads.list(new ThreadQuery().limit(1));
		if (threadList.isEmpty()) {
			System.out.println("No threads");
			return;
		}
		
		String threadId = threadList.get(0).getId();
		
		Thread thread = threads.setFolderId(threadId, newFolder.getId());
		System.out.println(thread);
		
		try {
			folders.delete(newFolder.getId());
		} catch (RequestFailedException rfe) {
			System.out.println(rfe.getErrorMessage());
		}
		
		
		thread = threads.setFolderId(threadId, inbox.getId());
		System.out.println(thread);
		
		folders.delete(newFolder.getId());
	}
}
