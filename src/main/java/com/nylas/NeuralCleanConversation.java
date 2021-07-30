package com.nylas;

public class NeuralCleanConversation extends Message {
	private String conversation;
	private String model_version;

	public String getConversation() {
		return conversation;
	}

	public String getModelVersion() {
		return model_version;
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
