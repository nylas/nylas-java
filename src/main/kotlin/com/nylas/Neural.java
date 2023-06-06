package com.nylas;

import okhttp3.HttpUrl;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.nylas.NeuralCategorizer.Category;

public class Neural {
	private final NylasAccount account;
	private final NylasClient client;
	private final String accessToken;
	private static final Pattern IMAGE_PATTERN = Pattern.compile("[(']cid:(.)*[)']");

	public Neural(NylasAccount account, NylasClient client, String accessToken) {
		this.account = account;
		this.client = client;
		this.accessToken = accessToken;
	}

	/**
	 * Performs sentiment analysis on a list of messages
	 * @see <a href="https://developer.nylas.com/docs/intelligence/sentiment-analysis/">
	 * Nylas Docs - Sentiment Analysis</a>
	 */
	public List<NeuralSentimentAnalysis> sentimentAnalysisMessage(List<String> messageIds)
			throws RequestFailedException, IOException {
		Map<String, Object> body = new HashMap<>();
		body.put("message_id", messageIds);
		Type listType = JsonHelper.listTypeOf(NeuralSentimentAnalysis.class);
		return neuralRequest("sentiment", body, listType);
	}

	/**
	 * Performs sentiment analysis on any string
	 * @see <a href="https://developer.nylas.com/docs/intelligence/sentiment-analysis/#analyze-text">
	 * Nylas Docs - Sentiment Analysis (Analyze Text)</a>
	 */
	public NeuralSentimentAnalysis sentimentAnalysisText(String text) throws RequestFailedException, IOException {
		Map<String, Object> body = new HashMap<>();
		body.put("text", text);
		return neuralRequest("sentiment", body, NeuralSentimentAnalysis.class);
	}

	/**
	 * Extracts signature and any contact information within from emails passed in
	 * <br> {@code options} is set to null.
	 * @see Neural#extractSignature(List, NeuralMessageOptions)
	 */
	public List<NeuralSignatureExtraction> extractSignature(List<String> messageIds)
			throws RequestFailedException, IOException {
		return extractSignature(messageIds, null);
	}

	/**
	 * Extracts signature and any contact information within from emails passed in
	 * @param messageIds List of emails to extract signatures from
	 * @param options Optional parameters to control what gets cleaned up in the signature
	 * @see <a href="https://developer.nylas.com/docs/intelligence/signature-extraction/">
	 * Nylas Docs - Signature Extraction</a>
	 */
	public List<NeuralSignatureExtraction> extractSignature(List<String> messageIds, NeuralMessageOptions options)
			throws RequestFailedException, IOException {
		Map<String, Object> body = new HashMap<>();
		body.put("message_id", messageIds);
		if(options != null) {
			body.putAll(options.toMap());
		}
		Type listType = JsonHelper.listTypeOf(NeuralSignatureExtraction.class);
		return neuralRequest("signature", body, listType);
	}

	/**
	 * Performs optical character recognition on a file
	 * @param fileId The file ID to perform OCR on
	 * @param pages Optionally page numbers can be specified to run OCR on
	 * @see <a href="https://developer.nylas.com/docs/intelligence/optical-charecter-recognition/">
	 * Nylas Docs - Optical Character Recognition</a>
	 */
	public NeuralOcr ocrRequest(String fileId, int... pages) throws RequestFailedException, IOException {
		Map<String, Object> body = new HashMap<>();
		body.put("file_id", fileId);
		if(pages != null) {
			body.put("pages", pages);
		}
		return neuralRequest("ocr", body, NeuralOcr.class);
	}

	/**
	 * Removes extra information from the email body
	 * <br> {@code options} is set to null.
	 * @see Neural#cleanConversation(List, NeuralMessageOptions)
	 */
	public List<NeuralCleanConversation> cleanConversation(List<String> messageIds)
			throws RequestFailedException, IOException {
		return cleanConversation(messageIds, null);
	}

	/**
	 * Removes extra information from the email body
	 * @param messageIds List of emails to clean
	 * @param options Optional parameters to control what gets cleaned up in the body
	 * @see <a href="https://developer.nylas.com/docs/intelligence/clean-conversations/">
	 * Nylas Docs - Clean Conversations</a>
	 */
	public List<NeuralCleanConversation> cleanConversation(List<String> messageIds, NeuralMessageOptions options)
			throws RequestFailedException, IOException {
		Map<String, Object> body = new HashMap<>();
		body.put("message_id", messageIds);
		if(options != null) {
			body.putAll(options.toMap());

			// Remove parse_contacts key as this option is only for signature extraction
			if(body.get("parse_contacts") != null) {
				body.remove("parse_contacts");
			}
		}
		Type listType = JsonHelper.listTypeOf(NeuralCleanConversation.class);
		return neuralRequest("conversation", body, listType);
	}

	/**
	 * Categorize a list of messages
	 * @see <a href="https://developer.nylas.com/docs/intelligence/categorizer/#categorize-message-request/">
	 * Nylas Docs - Categorizer</a>
	 */
	public List<NeuralCategorizer> categorize(List<String> messageIds)
			throws RequestFailedException, IOException {
		Map<String, Object> body = new HashMap<>();
		body.put("message_id", messageIds);
		Type listType = JsonHelper.listTypeOf(NeuralCategorizer.class);
		return neuralRequest("categorize", body, listType);
	}

	/**
	 * Parses image file IDs found in the clean conversation object and returns
	 * an array of File objects returned from the File API
	 */
	public List<File> extractImages(NeuralCleanConversation neuralCleanConversation) throws RequestFailedException, IOException {
		List<File> fileList = new ArrayList<>();
		if(neuralCleanConversation.getConversation() != null) {
			// After applying the regex, if there are IDs found they would be
			// in the form of => 'cid:xxxx' (including apostrophes), so we discard
			// everything before and after the file ID (denoted as xxxx above)
			Matcher fileIdMatcher = IMAGE_PATTERN.matcher(neuralCleanConversation.getConversation());
			while (fileIdMatcher.find()) {
				String match = fileIdMatcher.group();
				String fileId = match.substring(5, match.length() - 1);
				fileList.add(account.files().get(fileId));
			}
		}
		return fileList;
	}

	/**
	 * Re-categorize a message
	 * @param messageId The message to re-categorize
	 * @param category The new category (see: {@link Category})
	 * @see <a href="https://developer.nylas.com/docs/intelligence/categorizer/#categorize-feedback/">
	 * Nylas Docs - Categorize (Feedback)</a>
	 */
	public Map<String, Object> reCategorize(String messageId, Category category)
			throws RequestFailedException, IOException {
		Map<String, Object> body = new HashMap<>();
		body.put("message_id", messageId);
		body.put("category", category.toString());

		HttpUrl.Builder url = client.newUrlBuilder()
				.addPathSegment("neural")
				.addPathSegment("categorize")
				.addPathSegment("feedback");
		return client.executePost(accessToken, url, body, Map.class);
	}

	private <T> T neuralRequest(String path, Map<String, Object> body, Type modelClass)
			throws IOException, RequestFailedException {
		HttpUrl.Builder url = client.newUrlBuilder().addPathSegment("neural").addPathSegment(path);
		return client.executePut(accessToken, url, body, modelClass);
	}
}