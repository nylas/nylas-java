package com.nylas.examples;

import java.io.IOException;

import com.nylas.NylasAccount;
import com.nylas.NylasClient;
import com.nylas.RequestFailedException;
import com.nylas.Thread;
import com.nylas.ThreadQuery;
import com.nylas.Threads;

/**
 * Code examples for the reference doc
 */
public class DocExamples {

	// technically "thread.list()" will only get the first 100 threads since that's the default limit
	// the comment for "Available filter" I think belongs with(before) the second example
	// why thread.object / Thread.getObjectType()?  if you know it's a thread already
	// the order of the attributes seems unorganized - doesn't match docs, not alphabetized,
	// related attributes not together.  copy that order from python?
	// python "last_message_received_at" appears redundant to "last_message_received_timestamp"
	// likewise for "last_message_at" and "last_message_timestamp"
	// and missing "last_message_sent_at"
	
	public static void threadsExample() throws IOException, RequestFailedException {
		NylasClient client = new NylasClient();
		NylasAccount account = client.account("YOUR_ACCESS_TOKEN");
		Threads threads = account.threads();
		
		// Return all threads found in the user's inbox 
		threads.list();
		
		// Return threads that are filtered by specified arguments
		// Available filters: subject, to, from, cc, bcc, in, unread,
		// starred, filename, lastMessageBefore, lastMessageAfter, startedBefore, startedAfter
		threads.list(new ThreadQuery().to("swag@nylas.com"));
		
		// Use offset, and limit to control pagination
		threads.list(new ThreadQuery().limit(10).offset(10));
		
		// Return all threads that meet a specified search criteria
		threads.search("swag@nylas.com");
		
		// Return the most recent thread
		Thread thread = threads.list(new ThreadQuery().limit(1)).get(0);
		
		// The following attributes are available for the thread object
		thread.getSubject();
		thread.getMessageIds();
		thread.isUnread();
		thread.getLabels(); // Gmail accounts only
		thread.getFolders(); // All providers other than Gmail
		thread.getFirstMessageTimestamp();
		thread.getLastMessageTimestamp();
		thread.getLastMessageReceivedTimestamp();
		thread.getLastMessageSentTimestamp();
		thread.getAccountId();
		thread.getObjectType();
		thread.getId();
		thread.getSnippet();
		thread.getParticipants();
		thread.getDraftIds();
		thread.getVersion();
		thread.isStarred();
	}
}
