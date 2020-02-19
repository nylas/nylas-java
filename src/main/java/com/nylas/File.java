package com.nylas;

import java.util.Collections;
import java.util.List;

public class File extends AccountOwnedModel implements JsonObject {

	private String filename;
	private Integer size;
	private String content_type;
	private List<String> message_ids = Collections.emptyList();
	private String content_id;
	
	@Override
	public String getObjectType() {
		return "file";
	}
	
	public String getFilename() {
		return filename;
	}
	
	public Integer getSize() {
		return size;
	}
	
	public String getContentType() {
		return content_type;
	}
	
	public List<String> getMessageIds() {
		return message_ids;
	}
	
	public String getContentId() {
		return content_id;
	}

	@Override
	public String toString() {
		return "File [id=" + getId() + ", filename=" + filename + ", size=" + size + ", content_type=" + content_type
				+ ", message_ids=" + message_ids + ", content_id=" + content_id + "]";
	}
	
	
}
