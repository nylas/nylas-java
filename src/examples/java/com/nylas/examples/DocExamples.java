package com.nylas.examples;

import java.io.IOException;
import java.util.Arrays;

import com.nylas.Contact;
import com.nylas.Draft;
import com.nylas.Event;
import com.nylas.File;
import com.nylas.Message;
import com.nylas.NameEmail;
import com.nylas.NylasAccount;
import com.nylas.NylasApplication;
import com.nylas.NylasClient;
import com.nylas.Participant;
import com.nylas.RequestFailedException;
import com.nylas.Thread;
import com.nylas.ThreadQuery;
import com.nylas.Threads;
import com.nylas.Webhook;

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
	
	/*
	 * 2019-12-12 NOTE David Latham:
	 * Followed javascript example writing the result message to the console (system) output.
	 */
	/**
	 * https://docs.nylas.com/reference#sending-raw-mime
	 */
	public static void sendRawMimeExample() throws IOException, RequestFailedException {
		NylasClient client = new NylasClient();
		NylasAccount account = client.account("YOUR_ACCESS_TOKEN");
		String rawMime = ""; // rawMIME should be a MIME-format string with headers and multipart message
		Message message = account.drafts().sendRawMime(rawMime);
		System.out.println(message);
	}
	
	/*
	 * 2019-12-12 NOTE David Latham:
	 * The python example says that the date/time can be set in 3 ways, but I think that's out of date
	 * as there are now 4 supported types.
	 * May want to improve this example when I revisit date object handling
	 * 
	 * The example mentions that the event object must have `calendarId` and `when` before creation,
	 * and the Java API requires it explicitly, so even though the python example doesn't do it,
	 * this Java example includes a calendarId and date upfront rather than later on.
	 * 
	 * The python example updates many fields locally but doesn't appear to save/update them back to the server
	 * I think it's important to show that, so I added a line for it in the Java example here
	 */
	/**
	 * https://docs.nylas.com/reference#post-event
	 */
	public static void postEventExample() throws IOException, RequestFailedException {
		NylasClient client = new NylasClient();
		NylasAccount account = client.account("YOUR_ACCESS_TOKEN");
				
		// The event "when" (date/time) can be set as one of 4 types.
		// For details: https://docs.nylas.com/reference#event-subobjects
		Event.When when = null;
		when = new Event.Date("2020-01-01");
		when = new Event.Datespan("2019-08-29", "2019-09-01");
		when = new Event.Time(1408875644L);
		when = new Event.Timespan(1409594400L, 1409598000L);
				
		// Create a new event object
		// Provide the appropriate id for a calendar to add the event to a specific calendar
		Event event = new Event("{calendarId}", when);

		// save() must be called to save the event to the third party provider
		// notifyParticipants='true' will send a notification email to
		// all email addresses specified in the participants
		account.events().create(event, true);

		event.setTitle("Party!");
		event.setLocation("My House!");
		event.setDescription("Let's celebrate our calendar integration!!");
		event.setBusy(true);

		// Participants are added as a list of Participant objects, which require email
		// and may contain name, status, or comment as well
		event.setParticipants(Arrays.asList(new Participant("swag@nylas.com").name("My Nylas Friend")));

		// Update the event with the new values and notify the participants
		account.events().update(event, true);
	}
	
	/*
	 * 2019-12-12 NOTE David Latham:
	 * NOT TESTED - I have not been able to test this functionality as I cannot get 
	 * any events to show up in my test "Emailed events" calendar.
	*/
	/**
	 * https://docs.nylas.com/reference#rsvping-to-invitations
	 */
	public static void sendRsvpExample() throws IOException, RequestFailedException {
		NylasClient client = new NylasClient();
		NylasAccount account = client.account("YOUR_ACCESS_TOKEN");
		// RSVP to an invite. Note that you can only RSVP to invites found in the "Emailed events" calendar.
		// rsvp() accepts a status and an optional message
		// If notifyParticipants is true, then the message will be sent via email to all participants
		account.events().rsvp("{eventId}", "maybe", "{accountId}", "I may attend this event", true);
		System.out.println("RSVP sent!");
	}
	
	/*
	 * 2019-12-12 NOTE David Latham:
	 * The python example creates and saves an empty contact, then makes a bunch of 
	 * changes to it and never updates/saves them to the server.  I think this is very misleading, so
	 * I changed this example to create it with all the fields instead.
	*/
	/**
	 * https://docs.nylas.com/reference#contacts-2
	 */
	public static void postContactsExample() throws IOException, RequestFailedException {
		NylasClient client = new NylasClient();
		NylasAccount account = client.account("YOUR_ACCESS_TOKEN");
		
		// Create a new contact
		Contact contact = new Contact();

		// The following attributes can be modified for the contact object
		contact.setGivenName("My");
		contact.setMiddleName("Nylas");
		contact.setSurname("Friend");
		contact.setSuffix("API");
		contact.setNickname("Nylas");
		contact.setOfficeLocation("San Francisco");
		contact.setCompanyName("Nylas");
		contact.setNotes("Check out the Nylas Email, Calendar, and Contacts APIs");
		contact.setManagerName("Communications");
		contact.setJobTitle("Communications Platform");
		contact.setBirthday("2014-06-01");

		// emails must be one of type personal, or work
		contact.setEmails(Arrays.asList(new Contact.Email("personal", "swag@nylas.com")));

		// physical_addresses must be one of type work, home, or other
		Contact.PhysicalAddress address = new Contact.PhysicalAddress();
		address.setStreetAddress("695 Minna St");
		address.setType("work");
		address.setCity("San Francisco");
		address.setState("CA");
		address.setCountry("US");
		address.setPostalCode("94103");
		address.setFormat("structured");
		contact.setPhysicalAddresses(Arrays.asList(address));

		// phone_numbers must be one of type
		// business, organization_main, mobile, assistant,
		// business_fax, home_fax, radio, car, home, or pager
		contact.setPhoneNumbers(Arrays.asList(new Contact.PhoneNumber("business", "555 555-5555")));

		// web_pages must be one of type homepage, profile, work, or blog
		contact.setWebPages(Arrays.asList(new Contact.WebPage("homepage", "https://nylas.com")));

		// im_addresses must be one of type gtalk, aim,
		// yahoo, lync, skype, qq, msn, icc, or jabber
		contact.setIMAddresses(Arrays.asList(new Contact.IMAddress("gtalk", "Nylas")));
		
		// Save the contact to Nylas and the 3rd party provider
		// This must be executed whenever you want to save changes.
		contact = account.contacts().create(contact);
	}
	
	/**
	 * https://docs.nylas.com/reference#webhooks-post
	 */
	public static void postWebhooksExample() throws IOException, RequestFailedException {
		NylasClient client = new NylasClient();
		NylasApplication application = client.application("YOUR_CLIENT_ID", "YOUR_CLIENT_SECRET");
		Webhook webhook = new Webhook();
		webhook.setCallbackUrl("https://wwww.myapp.com/webhook");
		webhook.setState("active");
		webhook.setTriggers(Arrays.asList("event.created", "event.updated"));
		webhook = application.webhooks().create(webhook);
		System.out.println(webhook);
	}
}
