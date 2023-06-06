package com.nylas;

import java.util.List;

public class NeuralOcr extends File {
	private List<String> ocr;
	private int processed_pages;

	public List<String> getOcr() {
		return ocr;
	}

	public int getProcessedPages() {
		return processed_pages;
	}

	@Override
	public String toString() {
		return String.format("NeuralOcr [ocr=%s, processed_pages=%d id=%s, filename=%s, " +
						"size=%s, content_type=%s, message_ids=%s, content_id=%s]",
				ocr, processed_pages, getId(), getFilename(), getSize(), getContentType(),
				getMessageIds(), getContentId());
	}
}
