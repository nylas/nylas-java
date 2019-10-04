package com.nylas.examples;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Properties;

import com.nylas.AccessToken;
import com.nylas.Account;
import com.nylas.AccountQuery;
import com.nylas.Accounts;
import com.nylas.Application;
import com.nylas.Draft;
import com.nylas.DraftQuery;
import com.nylas.Drafts;
import com.nylas.File;
import com.nylas.Files;
import com.nylas.Folder;
import com.nylas.FolderQuery;
import com.nylas.Folders;
import com.nylas.GoogleProviderSettings;
import com.nylas.HostedAuthentication;
import com.nylas.IPAddressWhitelist;
import com.nylas.Message;
import com.nylas.MessageQuery;
import com.nylas.Messages;
import com.nylas.NativeAuthentication;
import com.nylas.NativeAuthentication.AuthRequestBuilder;
import com.nylas.NylasClient;
import com.nylas.RequestFailedException;
import com.nylas.Scope;
import com.nylas.Thread;
import com.nylas.ThreadQuery;
import com.nylas.Threads;
import com.nylas.TokenInfo;

public class Examples {

	public static void main(String[] args) throws IOException, RequestFailedException {

		Properties props = loadProperties();
		
		//runHostedAuthExample(props);
		//runNativeAuthExample(props);
		//runThreadsAndFolderExample(props);
		//runMessagesAndLabelsExample(props);
		
		//runAccountsExample(props);
		//runIpAddressesExample(props);
		//runTokenInfoExample(props);
		//runDraftsExample(props);
		
		//runFilesExample(props);
	}
	
	@SuppressWarnings("unused")
	private static void runFilesExample(Properties props) throws IOException, RequestFailedException {
		String accessToken = props.getProperty("access.token");
		
		NylasClient client = new NylasClient();
		Files files = client.files(accessToken);
		List<File> allFiles = files.list();
		for (File file : allFiles) {
			System.out.println("File: " + file);
		}
		
		File first = allFiles.get(0);
		byte[] fileBytes = files.downloadBytes(first.getId());
		java.nio.file.Files.write(Paths.get("/tmp/" + first.getFilename()), fileBytes);
		
		File uploaded = files.upload(first.getFilename(), first.getContentType(), fileBytes);
		System.out.println("Uploaded: " + uploaded);
		
		files.delete(uploaded.getId());
		System.out.println("deleted");
		
	}

	private static Properties loadProperties() throws IOException {
		Properties props = new Properties();
		try (final InputStream in = Examples.class.getResourceAsStream("/example.properties")) {
			props.load(in);
		}
		return props;
	}

	@SuppressWarnings("unused")
	private static void runDraftsExample(Properties props) throws IOException, RequestFailedException {
		String accessToken = props.getProperty("access.token");
		
		NylasClient client = new NylasClient();
		Drafts drafts = client.drafts(accessToken);
		
		DraftQuery query = new DraftQuery()
				//.limit(1)
				//.offset(1)
				//.anyEmail("info@twitter.com")
				;
		
		List<Draft> allDrafts = drafts.list(query);
		Draft firstDraft = allDrafts.get(0);
		for (Draft draft : allDrafts) {
			System.out.println(draft);
		}
		
		firstDraft = drafts.get(firstDraft.getId());
		System.out.println("first = " + firstDraft);
	}

	@SuppressWarnings("unused")
	private static void runIpAddressesExample(Properties props) throws IOException, RequestFailedException {
		NylasClient client = new NylasClient();
		Application application = client.application(props.getProperty("nylas.client.id"),
				props.getProperty("nylas.client.secret"));
		IPAddressWhitelist ipAddresses = application.fetchIpAddressWhitelist();
		System.out.println(ipAddresses);
	}

	@SuppressWarnings("unused")
	private static void runAccountsExample(Properties props) throws IOException, RequestFailedException {
		NylasClient client = new NylasClient();
		Application application = client.application(props.getProperty("nylas.client.id"),
				props.getProperty("nylas.client.secret"));
		Accounts accounts = application.accounts();
		AccountQuery query = new AccountQuery()
				.limit(2)
				//.offset(10)
				;
		List<Account> accountList = accounts.list(query);
		for (Account account : accountList) {
			System.out.println(account);
		}
		
		Account first = accounts.get(accountList.get(0).getId());
		System.out.println("first: " + first);
		
		String accessToken = props.getProperty("access.token");
		TokenInfo tokenInfo = accounts.tokenInfo(first.getId(), accessToken);
		System.out.println("token info: " + tokenInfo);
		
		accounts.downgrade(first.getId());
		first = accounts.get(accountList.get(0).getId());
		System.out.println("after downgrade: " + first);
		
		accounts.upgrade(first.getId());
		first = accounts.get(accountList.get(0).getId());
		System.out.println("after upgrade: " + first);
		
		accounts.revokeAllTokensForAccount(first.getId(), "blahblah");
		
	}

	@SuppressWarnings("unused")
	private static void runHostedAuthExample(Properties props) throws IOException, RequestFailedException {
		NylasClient client = new NylasClient();
		Application application = client.application(props.getProperty("nylas.client.id"),
				props.getProperty("nylas.client.secret"));
		HostedAuthentication authentication = application.hostedAuthentication();
		String hostedAuthUrl = authentication.urlBuilder()
			.redirectUri("https://example.com/nylas-redirect")
			.responseType("code")
			.scopes(Scope.EMAIL, Scope.CALENDAR, Scope.CONTACTS)
			.loginHint(props.getProperty("hosted.login.hint"))
			.state("example_csrf_token").buildUrl();
		System.out.println(hostedAuthUrl);
	
		String authorizationCode = props.getProperty("hosted.auth.code");
		String token = authentication.fetchToken(authorizationCode).getAccessToken();
		System.out.println(token);
	}
	
	@SuppressWarnings("unused")
	private static void runNativeAuthExample(Properties props) throws IOException, RequestFailedException {
//		ImapProviderSettings settings = new ImapProviderSettings()
//			.imapHost(props.getProperty("imap.host"))
//			.imapPort(Integer.parseInt(props.getProperty("imap.port")))
//			.imapUsername(props.getProperty("imap.username"))
//			.imapPassword(props.getProperty("imap.password"))
//			.smtpHost(props.getProperty("smtp.host"))
//			.smtpPort(Integer.parseInt(props.getProperty("smtp.port")))
//			.smtpUsername(props.getProperty("smtp.username"))
//			.smtpPassword(props.getProperty("smtp.password"))
//			.sslRequired(true)
//			;
		
		
		
		GoogleProviderSettings settings = new GoogleProviderSettings()
				.googleClientId("")
				.googleClientSecret("")
				.googleRefreshToken("")
				;
		
		NylasClient client = new NylasClient();
		Application application = client.application(props.getProperty("nylas.client.id"),
				props.getProperty("nylas.client.secret"));
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

	@SuppressWarnings("unused")
	private static void runMessagesAndLabelsExample(Properties props) throws IOException, RequestFailedException {
		String accessToken = props.getProperty("access.token");
		
		NylasClient client = new NylasClient();
		Messages messages = client.messages(accessToken);
		
		Instant start = LocalDate.of(2019,9,7).atStartOfDay(ZoneId.systemDefault()).toInstant();
		Instant end = LocalDate.of(2019,9,8).atStartOfDay(ZoneId.systemDefault()).toInstant();
		MessageQuery query = new MessageQuery()
				.limit(10)
				.hasAttachment(true)
				//.receivedAfter(start)
				//.receivedBefore(end)
				//.anyEmail("info@twitter.com")
//				.in("Example Label 2")
				;

		
		List<Message> allMessages = messages.list(query);
		Message firstMessage = allMessages.get(0);
		for (Message message : allMessages) {
			System.out.println(message);
		}
		
		String messageId = "88hhp3qq9xin9lovkxmneqdct";
		String labelId = "89g7aoatr4u4br5ldxri5cxct";
		
		
//		List<ExpandedMessage> allExpandedMessages = messages.expanded(query);
//		for (ExpandedMessage message : allExpandedMessages) {
//			System.out.println(message);
//		}
		

//		String raw = messages.getRaw(firstMessage.getId());
//		System.out.println(raw);

		
		Message message = messages.get(firstMessage.getId());
		System.out.println(message);
		
		message = messages.setUnread(firstMessage.getId(), !firstMessage.getUnread());
		System.out.println(message);
		
		message = messages.setStarred(firstMessage.getId(), !firstMessage.getStarred());
		System.out.println(message);

//		Labels labels = client.labels(accessToken);
//		List<Label> allLabels = labels.list();
//		Label inbox = null;
//		for (Label label : allLabels) {
//			System.out.println(label);
//			if ("inbox".equals(label.getName())) {
//				inbox = label;
//			}
//			
////			if (label.getDisplayName().startsWith("Example")) {
////				labels.delete(label.getId());
////				System.out.println("Deleted: " + label);
////			}
//		}
//		System.out.println("Inbox label: " + inbox);
//		
//	//	Label newLabel1 = labels.create("Example Label 1");
//	//	System.out.println(newLabel1);
//		Label newLabel2 = labels.create("Example Label 3");
//		System.out.println(newLabel2);
////		
////		String messageId = firstMessage.getId();
//		message = messages.setLabelIds(messageId, Arrays.asList("89g7aoatr4u4br5ldxri5cxct"));
//		System.out.println(message);
		
		
		
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
