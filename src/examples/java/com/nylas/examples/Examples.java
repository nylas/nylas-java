package com.nylas.examples;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import com.nylas.AccessToken;
import com.nylas.Account;
import com.nylas.Application;
import com.nylas.Folder;
import com.nylas.Folders;
import com.nylas.ImapProviderSettings;
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

		
	}

	@SuppressWarnings("unused")
	private static void runThreadsAndFolderExample(Properties props) throws IOException, RequestFailedException {
		String accessToken = props.getProperty("access.token");
		
		NylasClient client = new NylasClient();
		Threads threads = client.threads(accessToken);
		List<Thread> allThreads = threads.expanded(new ThreadQuery());
		for (Thread thread : allThreads) {
			System.out.println(thread);
		}
		
		String threadId = allThreads.get(0).getId();
		Thread thread = threads.get(threadId);
		System.out.println(thread);

		
		Folders folders = client.folders(accessToken);
		List<Folder> allFolders = folders.list();
		Folder inbox = null;
		for (Folder folder : allFolders) {
			System.out.println(folder);
			if ("inbox".equals(folder.getName())) {
				inbox = folder;
			}
		}
		
		Folder newFolder = folders.create("Example Folder");
		System.out.println(newFolder);
		
		thread = threads.setFolderId(threadId, newFolder.getId());
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
				.scopes(Scope.EMAIL_ALL, Scope.CALENDAR_ALL, Scope.CONTACTS_ALL);
				
		String authorizationCode = authRequest.execute();
		System.out.println("code: " + authorizationCode);
		
		AccessToken token = authentication.fetchToken(authorizationCode);
		System.out.println(token);
		
		Account account = client.fetchAccountByAccessToken(token.getAccessToken());
		System.out.println(account);
	}
	
	private static Properties loadProperties() throws IOException {
		Properties props = new Properties();
		try (final InputStream in = Examples.class.getResourceAsStream("/example.properties")) {
			props.load(in);
		}
		return props;
	}
}
