package com.nylas;

import okhttp3.HttpUrl;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Neural {
	protected final String neuralPath = "neural";
	protected final NylasClient client;
	protected final String accessToken;

	public Neural(NylasClient client, String accessToken) {
		this.client = client;
		this.accessToken = accessToken;
	}

	public List<NeuralSentimentAnalysis> sentimentAnalysisMessage(String[] messageIds)
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

	public List<NeuralSignatureExtraction> extractSignature(String[] messageIds)
			throws RequestFailedException, IOException {
		return extractSignature(messageIds, null);
	}

	public List<NeuralSignatureExtraction> extractSignature(String[] messageIds, NeuralMessageOptions options)
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

	public List<NeuralCleanConversation> cleanConversation(String[] messageIds)
			throws RequestFailedException, IOException {
		return cleanConversation(messageIds, null);
	}

	public List<NeuralCleanConversation> cleanConversation(String[] messageIds, NeuralMessageOptions options)
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

	private <T> T neuralRequest(String path, Map<String, Object> body, Type modelClass)
			throws IOException, RequestFailedException {
		HttpUrl.Builder url = client.newUrlBuilder().addPathSegment(neuralPath).addPathSegment(path);
		return client.executePut(accessToken, url, body, modelClass);
	}
}