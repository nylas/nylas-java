package com.nylas.examples;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Properties;

import com.nylas.Draft;
import com.nylas.DraftQuery;
import com.nylas.Drafts;
import com.nylas.Label;
import com.nylas.Labels;
import com.nylas.Message;
import com.nylas.MessageQuery;
import com.nylas.Messages;
import com.nylas.NylasClient;

public class DraftsExample {

	public static void main(String[] args) throws Exception {
		Properties props = Examples.loadExampleProperties();
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
	
		
		Messages messages = client.messages(accessToken);
		
		Instant start = LocalDate.of(2019,9,7).atStartOfDay(ZoneId.systemDefault()).toInstant();
		Instant end = LocalDate.of(2019,9,8).atStartOfDay(ZoneId.systemDefault()).toInstant();
		MessageQuery mquery = new MessageQuery()
				.limit(10)
				.hasAttachment(true)
				//.receivedAfter(start)
				//.receivedBefore(end)
				//.anyEmail("info@twitter.com")
//				.in("Example Label 2")
				;

		
		List<Message> allMessages = messages.list(mquery);
		Message firstMessage = allMessages.get(0);
		
		firstDraft.setReplyToMessageId(firstMessage.getId());
		
		
		Draft putResult = drafts.put(firstDraft);
		System.out.println("put result = " + putResult);
	}

}
