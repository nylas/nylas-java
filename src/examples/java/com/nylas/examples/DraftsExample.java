package com.nylas.examples;

import java.util.List;
import java.util.Properties;

import com.nylas.Draft;
import com.nylas.DraftQuery;
import com.nylas.Drafts;
import com.nylas.NylasAccount;
import com.nylas.NylasClient;

public class DraftsExample {

	public static void main(String[] args) throws Exception {
		Properties props = Examples.loadExampleProperties();
		String accessToken = props.getProperty("access.token");
		
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(accessToken);
		Drafts drafts = account.drafts();
		
		DraftQuery query = new DraftQuery()
				//.subject("now a subject and recipient")
				//.to("bob@example.com")
				//.cc("dude@dude.com")
				//.bcc("georgejetson@example.com")
				//.in("Example Label 2")
				//.limit(1)
				//.offset(1)
				//.unread(true)
				//.starred(false)
				//.anyEmail("dude@dude.com")
				//.filename("test.pdf")
				//.hasAttachment(true)
				//.threadId("53m7tluqh0i1500ntrljtif6e")
				//.anyEmail("info@twitter.com")
				;
		
		List<Draft> allDrafts = drafts.list(query);
		for (Draft draft : allDrafts) {
			System.out.println(draft);
		}
		
//		System.out.println(account.fetchAccountByAccessToken());
		
//		Draft firstDraft = allDrafts.get(0);
//		firstDraft = drafts.get(firstDraft.getId());
//		System.out.println("first = " + firstDraft);
//		
//		
//		Files files = account.files();
//		List<File> allFiles = files.list();
//		File iconFile = null;
//		for (File file : allFiles) {
//			System.out.println("File: " + file);
//			if (file.getFilename().equals("icon.png")) {
//				iconFile = file;
//				break;
//			}
//		}
//		System.out.println("iconFile = " + iconFile);
//		
//		allDrafts.get(0).attach(iconFile);
//		drafts.update(allDrafts.get(0));
////		
//		File first = allFiles.get(0);
////		byte[] fileBytes = files.downloadBytes(first.getId());
////		java.nio.file.Files.write(Paths.get("/tmp/" + first.getFilename()), fileBytes);
//		byte[] fileBytes = java.nio.file.Files.readAllBytes(Paths.get("/tmp/desktop.ini"));
////		
//		File uploaded = files.upload("desktop2.ini", first.getContentType(), fileBytes);
//		firstDraft.getFiles().add(uploaded);
	
		
//		Messages messages = account.messages();
//		
//		Instant start = LocalDate.of(2019,10,7).atStartOfDay(ZoneId.systemDefault()).toInstant();
//		Instant end = start.plus(1, ChronoUnit.DAYS);
//		MessageQuery mquery = new MessageQuery()
//				.limit(3)
//				//.hasAttachment(true)
//				.receivedAfter(start)
//				.receivedBefore(end)
//				//.anyEmail("info@twitter.com")
////				.in("Example Label 2")
//				;
//
//		
//		List<Message> allMessages = messages.list(mquery);
//	//	for (Message msg : allMessages)
//		Message firstMessage = allMessages.get(0);
//		
//		
//		firstDraft.setReplyToMessageId(firstMessage.getId());
		
		
//		Draft putResult = drafts.put(firstDraft);
//		System.out.println("put result = " + putResult);
//		
//		Draft draft = new Draft();
//		draft.setTo(Arrays.asList(new NameEmail("dude", "dude@b.com")));
//		draft.setBody("this is the draft body text");
//		draft.setSubject("I wonder if this draft will show up in gmail");
//		Draft created = drafts.create(draft);
//		System.out.println("post result = " + created);
	}

}
