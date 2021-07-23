package com.nylas;

public class NeuralSentimentAnalysis {
	private String account_id;
	private String sentiment;
	private float sentiment_score;
	private int processed_length;
	private String text;

	public String getAccountId() {
		return account_id;
	}

	public void setAccountId(String accountId) {
		this.account_id = accountId;
	}

	public String getSentiment() {
		return sentiment;
	}

	public void setSentiment(String sentiment) {
		this.sentiment = sentiment;
	}

	public float getSentimentScore() {
		return sentiment_score;
	}

	public void setSentimentScore(float sentimentScore) {
		this.sentiment_score = sentimentScore;
	}

	public int getProcessedLength() {
		return processed_length;
	}

	public void setProcessedLength(int processedLength) {
		this.processed_length = processedLength;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return String.format("NeuralSentimentAnalysis [account_id=%s, sentiment=%s, sentiment_score=%.2f, processed_length=%d, text=%s]",
				account_id, sentiment, sentiment_score, processed_length, text);
	}
}
