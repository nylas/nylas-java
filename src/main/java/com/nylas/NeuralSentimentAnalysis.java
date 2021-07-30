package com.nylas;

public class NeuralSentimentAnalysis {
	private String account_id;
	private String sentiment;
	private double sentiment_score;
	private int processed_length;
	private String text;

	public String getAccountId() {
		return account_id;
	}

	public String getSentiment() {
		return sentiment;
	}

	public double getSentimentScore() {
		return sentiment_score;
	}

	public int getProcessedLength() {
		return processed_length;
	}

	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		return String.format("NeuralSentimentAnalysis [account_id=%s, sentiment=%s, sentiment_score=%.2f, processed_length=%d, text=%s]",
				account_id, sentiment, sentiment_score, processed_length, text);
	}
}
