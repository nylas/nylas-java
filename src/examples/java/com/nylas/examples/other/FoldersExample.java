package com.nylas.examples.other;

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
import com.nylas.examples.ExampleConf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class FoldersExample {

	private static final Logger log = LogManager.getLogger(FoldersExample.class);
	
	public static void main(String[] args) throws Exception {
		ExampleConf conf = new ExampleConf();
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(conf.get("access.token"));
		
		Folders folders = account.folders();
		
		FolderQuery fQuery = new FolderQuery()
				.limit(5)
				.offset(0);
		
		List<Folder> allFolders = folders.list(fQuery).fetchAll();
		Folder inbox = null;
		for (Folder folder : allFolders) {
			log.info(folder);
			if ("inbox".equals(folder.getName())) {
				inbox = folder;
			}
		}
		
		Folder newFolder = folders.create("Example Folder 7");
		log.info(newFolder);
		
		Threads threads = account.threads();
		List<Thread> threadList = threads.list(new ThreadQuery().limit(1)).fetchAll();
		if (threadList.isEmpty()) {
			log.info("No threads");
			return;
		}
		
		String threadId = threadList.get(0).getId();
		
		Thread thread = threads.setFolderId(threadId, newFolder.getId());
		log.info(thread);
		
		try {
			folders.delete(newFolder.getId());
		} catch (RequestFailedException rfe) {
			log.info(rfe.getErrorMessage());
		}
		
		
		thread = threads.setFolderId(threadId, inbox.getId());
		log.info(thread);
		
		folders.delete(newFolder.getId());
	}
}
