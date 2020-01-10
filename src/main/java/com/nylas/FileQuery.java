package com.nylas;

import okhttp3.HttpUrl.Builder;

public class FileQuery extends PaginatedQuery<FileQuery> {

	private String filename;
	private String messageId;
	private String contentType;
	
	@Override
	public void addParameters(Builder url) {
		super.addParameters(url);
		
		if (filename != null) {
			url.addQueryParameter("filename", filename);
		}
		if (messageId != null) {
			url.addQueryParameter("message_id", messageId);
		}
		if (contentType != null) {
			url.addQueryParameter("content_type", contentType);
		}
	}
	
	public FileQuery filename(String filename) {
		this.filename = filename;
		return this;
	}
	
	public FileQuery messageId(String messageId) {
		this.messageId = messageId;
		return this;
	}
	
	public FileQuery contentType(String contentType) {
		this.contentType = contentType;
		return this;
	}
	

}
