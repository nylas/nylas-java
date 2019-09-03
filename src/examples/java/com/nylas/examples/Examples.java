package com.nylas.examples;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Properties;

import com.nylas.AccessToken;
import com.nylas.Account;
import com.nylas.Application;
import com.nylas.ImapProviderSettings;
import com.nylas.Message;
import com.nylas.Messages;
import com.nylas.NativeAuthentication;
import com.nylas.NativeAuthentication.AuthRequestBuilder;
import com.nylas.NylasClient;
import com.nylas.RequestFailedException;
import com.nylas.Scope;
import com.nylas.Thread;
import com.nylas.ThreadQuery;
import com.nylas.Threads;

public class Examples {

	public static void main(String[] args) throws IOException, RequestFailedException {

		Properties props = loadProperties();
		
		//runNativeAuthExample(props);
		
		runThreadsAndFolderExample(props);
		//runMessagesExample(props);
	}
	
	private static Properties loadProperties() throws IOException {
		Properties props = new Properties();
		try (final InputStream in = Examples.class.getResourceAsStream("/example.properties")) {
			props.load(in);
		}
		return props;
	}

	@SuppressWarnings("unused")
	private static void runNativeAuthExample(Properties props) throws IOException, RequestFailedException {
		ImapProviderSettings settings = new ImapProviderSettings()
			.imapHost(props.getProperty("imap.host"))
			.imapPort(Integer.parseInt(props.getProperty("imap.port")))
			.imapUsername(props.getProperty("imap.username"))
			.imapPassword(props.getProperty("imap.password"))
			.smtpHost(props.getProperty("smtp.host"))
			.smtpPort(Integer.parseInt(props.getProperty("smtp.port")))
			.smtpUsername(props.getProperty("imap.username"))
			.smtpPassword(props.getProperty("imap.password"))
			.sslRequired(true)
			;
		
		NylasClient client = new NylasClient();
		Application application = client.application(props.getProperty("client.id"),
				props.getProperty("client.secret"));
		NativeAuthentication authentication = application.nativeAuthentication();
		AuthRequestBuilder authRequest = authentication.authRequest()
				.name(props.getProperty("imap.name"))
				.emailAddress(props.getProperty("imap.email"))
				.providerSettings(settings)
				.scopes(Scope.EMAIL, Scope.CALENDAR, Scope.CONTACTS);
				
		String authorizationCode = authRequest.execute();
		System.out.println("code: " + authorizationCode);
		
		AccessToken token = authentication.fetchToken(authorizationCode);
		System.out.println(token);
		
		Account account = client.fetchAccountByAccessToken(token.getAccessToken());
		System.out.println(account);
	}
	
	@SuppressWarnings("unused")
	private static void runThreadsAndFolderExample(Properties props) throws IOException, RequestFailedException {
		String accessToken = props.getProperty("access.token");
		
		NylasClient client = new NylasClient();
		Threads threads = client.threads(accessToken);
		
		Instant start = LocalDate.of(2019,9,1).atStartOfDay(ZoneId.systemDefault()).toInstant();
		Instant end = LocalDate.of(2019,9,2).atStartOfDay(ZoneId.systemDefault()).toInstant();
		ThreadQuery query = new ThreadQuery()
				.limit(100)
				.lastMessageAfter(start)
				.lastMessageBefore(end)
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
//		Folders folders = client.folders(accessToken);
//		List<Folder> allFolders = folders.list();
//		Folder inbox = null;
//		for (Folder folder : allFolders) {
//			System.out.println(folder);
//			if ("inbox".equals(folder.getName())) {
//				inbox = folder;
//			}
//		}
//		
//		Folder newFolder = folders.create("Example Folder");
//		System.out.println(newFolder);
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

	@SuppressWarnings("unused")
	private static void runMessagesExample(Properties props) throws IOException, RequestFailedException {
		String accessToken = props.getProperty("access.token");
		
		NylasClient client = new NylasClient();
		Messages messages = client.messages(accessToken);
		
		List<Message> allMessages = messages.list();
		Message firstMessage = allMessages.get(0);
//		for (Message message : allMessages) {
//			System.out.println(message);
//		}
		
//		List<ExpandedMessage> allMessages = messages.expanded(null);
//		for (ExpandedMessage message : allMessages) {
//			System.out.println(message);
//		}
		

		String raw = messages.getRaw(firstMessage.getId());
		System.out.println(raw);

		
//		Message message = messages.get(firstMessage.getId());
//		System.out.println(message);
//		
//		message = messages.setUnread(firstMessage.getId(), !firstMessage.getUnread());
//		System.out.println(message);
//		
//		message = messages.setStarred(firstMessage.getId(), !firstMessage.getStarred());
//		System.out.println(message);

//		Folders folders = client.folders(accessToken);
//		List<Folder> allFolders = folders.list();
//		Folder inbox = null;
//		for (Folder folder : allFolders) {
//			System.out.println(folder);
//			if ("inbox".equals(folder.getName())) {
//				inbox = folder;
//			}
//		}
//		
//		Folder newFolder = folders.create("Example Folder");
//		System.out.println(newFolder);
//		message = messages.setFolderId(firstMessage.getId(), newFolder.getId());
//		try {
//			folders.delete(newFolder.getId());
//		} catch (RequestFailedException rfe) {
//			System.out.println(rfe.getErrorMessage());
//		}
//
//		message = messages.setFolderId(firstMessage.getId(), inbox.getId());
//		System.out.println(message);
//		
//		folders.delete(newFolder.getId());
	}

}
