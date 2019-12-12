package com.nylas.examples;

import java.io.IOException;
import java.util.Arrays;

import com.nylas.Draft;
import com.nylas.File;
import com.nylas.NameEmail;
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

	/**
	 * https://docs.nylas.com/reference#get-threads
	 */
	public static void getThreadsExample() throws IOException, RequestFailedException {
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
	
	/*
	 * 2019-12-12 NOTE David Latham:
	 * currently with the Java SDK, updates to an object go through the collection interface.
	 * For example, `threads.setUnread(thread.getId(), true)` compared to the python `thread.mark_as_unread()`
	 * This is to maintain the invariant that all api network requests go through the collection objects
	 * and avoid any surprising behavior.
	 * 
	 * Likewise, updating label ids requires specifying the entire set, so I removed the individual add/remove cases.
	 * 
	 * However, after talking with Ben it seems that other threads perform auto batching when iterating
	 * collections, which would violate this invariant.  If we're willing to throw out the invariant anyway,
	 * then we could make these object updates similar to other SDKs as well.  E.g. `thread.setUnread(true)`
	 */
	/**
	 * https://docs.nylas.com/reference#threadsid-1
	 */
	public static void putThreadsExample() throws IOException, RequestFailedException {
		NylasClient client = new NylasClient();
		NylasAccount account = client.account("YOUR_ACCESS_TOKEN");
		Threads threads = account.threads();
		
		// Replace '{id}' with the appropriate value
		Thread thread = threads.get("{id}");

		// Mark thread read
		threads.setUnread(thread.getId(), false);

		// Mark thread unread
		threads.setUnread(thread.getId(), true);

		// Star a thread
		threads.setStarred(thread.getId(), true);

		// Unstar a thread
		threads.setStarred(thread.getId(), false);

		// Update labels on a thread (Gmail)
		threads.setLabelIds(thread.getId(), Arrays.asList("{label_id}", "{another_label_id}"));

		// Move a thread to a different folder (Non-Gmail)
		threads.setFolderId(thread.getId(), "{folder_id}");
	}
	
	/*
	 * 2019-12-12 NOTE David Latham:
	 * This example copies the python example's lead of calling "save" once at the end.
	 * Save performs either a "create" or a "update" under the covers depending on whether
	 * the passed draft was created only locally by the SDK or was previously retrieved from the server
	 * I think the example may be improved by demonstrating separate calls to create a new draft
	 * and later to update a previously created one.  But left it as-is to match existing ones.
	 */
	/**
	 * https://docs.nylas.com/reference#post-draft
	 */
	public static void postDraftExample() throws IOException, RequestFailedException {
		NylasClient client = new NylasClient();
		NylasAccount account = client.account("YOUR_ACCESS_TOKEN");
		
		// Create a new draft object
		Draft draft = new Draft();

		draft.setSubject("With Love, From Nylas");
		draft.setTo(Arrays.asList(new NameEmail("My Nylas Friend", "swag@nylas.com")));
		// You can also assign draft.cc, draft.bcc, and draft.from_ in the same manner
		draft.setBody("This email was sent using the Nylas email API. Visit https://nylas.com for details.");
		draft.setReplyTo(new NameEmail("Your Name", "you@example.com"));
		// Note: changing from_ to a different email address may cause deliverability issues
		draft.setFrom(new NameEmail("Your Name", "you@example.com"));
		
		// Replace {id} with the appropriate id for a file that you want to attach to a draft
		File file = account.files().get("{id}");

		// Attach a file to a draft
		draft.attach(file);

		// Remove a file attachment from a draft
		draft.detach(file);

		// You must save the draft for changes to take effect
		draft = account.drafts().save(draft);
		// Note: Nylas saves all drafts, but not all providers
		// display the drafts on their user interface

	}
}
