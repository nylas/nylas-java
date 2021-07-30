package com.nylas;

import okhttp3.HttpUrl;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	public List<NeuralSentimentAnalysis> sentimentAnalysisMessage(List<String> messageIds)
			throws RequestFailedException, IOException {
		Map<String, Object> body = new HashMap<>();
		body.put("message_id", messageIds);
		Type listType = JsonHelper.listTypeOf(NeuralSentimentAnalysis.class);
		return neuralRequest("sentiment", body, listType);
	}

	public NeuralSentimentAnalysis sentimentAnalysisText(String text) throws RequestFailedException, IOException {
		Map<String, Object> body = new HashMap<>();
		body.put("text", text);
		return neuralRequest("sentiment", body, NeuralSentimentAnalysis.class);
	}

	public List<NeuralSignatureExtraction> extractSignature(List<String> messageIds)
			throws RequestFailedException, IOException {
		return extractSignature(messageIds, null);
	}

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

	public NeuralOcr ocrRequest(String fileId, int... pages) throws RequestFailedException, IOException {
		Map<String, Object> body = new HashMap<>();
		body.put("file_id", fileId);
		if(pages != null) {
			body.put("pages", pages);
		}
		return neuralRequest("ocr", body, NeuralOcr.class);
	}

	public List<NeuralCleanConversation> cleanConversation(List<String> messageIds)
			throws RequestFailedException, IOException {
		return cleanConversation(messageIds, null);
	}

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

	private <T> T neuralRequest(String path, Map<String, Object> body, Type modelClass)
			throws IOException, RequestFailedException {
		HttpUrl.Builder url = client.newUrlBuilder().addPathSegment("neural").addPathSegment(path);
		return client.executePut(accessToken, url, body, modelClass);
	}
}