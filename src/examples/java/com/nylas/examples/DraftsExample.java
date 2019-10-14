package com.nylas.examples;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Properties;

import com.nylas.Draft;
import com.nylas.DraftQuery;
import com.nylas.Drafts;
import com.nylas.Message;
import com.nylas.MessageQuery;
import com.nylas.Messages;
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
				//.limit(1)
				//.offset(1)
				.anyEmail("ddlatham@gmail.com")
				;
		
		List<Draft> allDrafts = drafts.list(query);
		Draft firstDraft = allDrafts.get(0);
		for (Draft draft : allDrafts) {
			System.out.println(draft);
		}
		
		firstDraft = drafts.get(firstDraft.getId());
		System.out.println("first = " + firstDraft);
		
		
//		Files files = client.files(accessToken);
//		List<File> allFiles = files.list();
//		for (File file : allFiles) {
//			System.out.println("File: " + file);
//		}
//		
//		File first = allFiles.get(0);
////		byte[] fileBytes = files.downloadBytes(first.getId());
////		java.nio.file.Files.write(Paths.get("/tmp/" + first.getFilename()), fileBytes);
//		byte[] fileBytes = java.nio.file.Files.readAllBytes(Paths.get("/tmp/desktop.ini"));
//		
//		File uploaded = files.upload("desktop2.ini", first.getContentType(), fileBytes);
//		firstDraft.getFiles().add(uploaded);
	
		
		Messages messages = account.messages();
		
		Instant start = LocalDate.of(2019,10,7).atStartOfDay(ZoneId.systemDefault()).toInstant();
		Instant end = start.plus(1, ChronoUnit.DAYS);
		MessageQuery mquery = new MessageQuery()
				.limit(3)
				//.hasAttachment(true)
				.receivedAfter(start)
				.receivedBefore(end)
				//.anyEmail("info@twitter.com")
//				.in("Example Label 2")
				;

		
		List<Message> allMessages = messages.list(mquery);
	//	for (Message msg : allMessages)
		Message firstMessage = allMessages.get(0);
		
		
		firstDraft.setReplyToMessageId(firstMessage.getId());
		
		
//		Draft putResult = drafts.put(firstDraft);
//		System.out.println("put result = " + putResult);
//		
//		Draft draft = new Draft();
//		draft.setTo(Arrays.asList(new NameEmail("dude", "dude@b.com")));
//		draft.setThreadId(firstMessage.getThreadId());
//		draft.setReplyToMessageId(allMessages.get(1).getId());
//		Draft postResult = drafts.post(draft);
//		System.out.println("post result = " + postResult);
	}

}
