package com.nylas.examples.other;

import com.nylas.*;
import com.nylas.examples.ExampleConf;

import java.io.IOException;
import java.util.List;

public class NeuralExample {
	public static void main(String[] args) throws RequestFailedException, IOException {
		ExampleConf conf = new ExampleConf();
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(conf.get("access.token"));
		Neural neural = account.neural();

		MessageQuery query = new MessageQuery().limit(1).hasAttachment(true);
		List<Message> emails = account.messages().list(query).fetchAll();
		if(emails.size() != 1) {
			throw new RuntimeException("No emails found with attachments.");
		}
		Message email = emails.get(0);

		// Sentiment Analysis
		List<NeuralSentimentAnalysis> messageAnalysis = neural.sentimentAnalysisMessage(new String[]{ email.getId() });
		System.out.println(messageAnalysis.get(0));

		NeuralSentimentAnalysis textAnalysis = neural
				.sentimentAnalysisText("Hi, thank you so much for reaching out! We can catch up tomorrow.");
		System.out.println(textAnalysis);

		// Signature Extraction
		List<NeuralSignatureExtraction> extractSignature = neural.extractSignature(new String[]{ email.getId() });
		System.out.println(extractSignature.get(0));
		Contact contact = extractSignature.get(0).getContacts().toContactObject();
		System.out.println(contact);

		NeuralMessageOptions options = new NeuralMessageOptions(true, false, true, false, true);
		extractSignature = neural.extractSignature(new String[]{ email.getId() }, true, options);

		// OCR
		NeuralOcr ocr = neural.ocrRequest( email.getFiles().get(0).getId() );
		System.out.println(ocr);

		// Clean Conversations
		List<NeuralCleanConversation> cleanConversations = neural.cleanConversation(new String[]{ email.getId() });
		System.out.println(cleanConversations);
		cleanConversations.get(0).extractImages(account);
	}
}
