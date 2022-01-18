package com.nylas.examples.other;

import com.nylas.*;
import com.nylas.examples.ExampleConf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NeuralExample {

	private static final Logger log = LogManager.getLogger(NeuralExample.class);

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
		List<NeuralSentimentAnalysis> messageAnalysis = neural
				.sentimentAnalysisMessage(new ArrayList<>(Collections.singletonList(email.getId())));
		log.info(messageAnalysis.get(0));

		NeuralSentimentAnalysis textAnalysis = neural
				.sentimentAnalysisText("Hi, thank you so much for reaching out! We can catch up tomorrow.");
		log.info(textAnalysis);

		// Signature Extraction
		List<NeuralSignatureExtraction> extractSignature = neural
				.extractSignature(new ArrayList<>(Collections.singletonList(email.getId())));
		log.info(extractSignature.get(0));
		Contact contact = extractSignature.get(0).getContacts().toContactObject();
		log.info(contact);

		NeuralMessageOptions options = new NeuralMessageOptions()
				.ignoreImages(true)
				.ignoreTables(false)
				.ignoreLinks(true)
				.removeConclusionPhrases(false)
				.imagesAsMarkdown(true)
				.parseContacts(false);
		extractSignature = neural.extractSignature(new ArrayList<>(Collections.singletonList(email.getId())), options);

		// OCR
		NeuralOcr ocr = neural.ocrRequest( email.getFiles().get(0).getId() );
		log.info(ocr);
		ocr = neural.ocrRequest( email.getFiles().get(0).getId(), 2, 3 );
		log.info(ocr);

		// Clean Conversations
		List<NeuralCleanConversation> cleanConversations = neural
				.cleanConversation(new ArrayList<>(Collections.singletonList(email.getId())));
		log.info(cleanConversations);
		neural.extractImages(cleanConversations.get(0));
	}
}
