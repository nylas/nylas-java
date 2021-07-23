package com.nylas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NeuralCleanConversation extends Message {
	private String conversation;
	private String model_version;

	public String getConversation() {
		return conversation;
	}

	public void setConversation(String conversation) {
		this.conversation = conversation;
	}

	public String getModelVersion() {
		return model_version;
	}

	public void setModelVersion(String modelVersion) {
		this.model_version = modelVersion;
	}

	public List<File> extractImages(NylasAccount account) throws RequestFailedException, IOException {
		List<File> fileList = new ArrayList<>();
		if(conversation != null) {
			// After applying the regex, if there are IDs found they would be
			// in the form of => 'cid:xxxx' (including apostrophes), so we discard
			// everything before and after the file ID (denoted as xxxx above)
			Matcher fileIdMatcher = Pattern.compile("[(']cid:(.)*[)']").matcher(conversation);
			while (fileIdMatcher.find()) {
				String match = fileIdMatcher.group();
				String fileId = match.substring(5, match.length() - 1);
				fileList.add(account.files().get(fileId));
			}
		}
		return fileList;
	}

	@Override
	public String toString() {
		return String.format("NeuralCleanConversation [conversation=%s, model_version=%s, id=%s, account_id=%s, " +
						"thread_id=%s, from=%s, to=%s, cc=%s, bcc=%s, reply_to=%s, date=%s, unread=%s, starred=%s, " +
						"snippet=%s, files=%s, events=%s, folder=%s, labels=%s, headers=%s]",
				conversation, model_version, getId(), getAccountId(), getThreadId(), from, to, cc, bcc, reply_to, date,
				unread, starred, snippet, files, events, folder, labels, getHeaders());
	}
}
