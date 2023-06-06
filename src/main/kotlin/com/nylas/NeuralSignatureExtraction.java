package com.nylas;

public class NeuralSignatureExtraction extends Message {
	private String signature;
	private String model_version;
	private NeuralSignatureContacts contacts;

	public String getSignature() {
		return signature;
	}

	public String getModelVersion() {
		return model_version;
	}

	public NeuralSignatureContacts getContacts() {
		return contacts;
	}

	@Override
	public String toString() {
		return String.format("NeuralSignatureExtraction [signature=%s, model_version=%s, contacts={ %s }, id=%s," +
						"account_id=%s, thread_id=%s, from=%s, to=%s, cc=%s, bcc=%s, reply_to=%s, date=%s, unread=%s," +
						"starred=%s, snippet=%s, files=%s, events=%s, folder=%s, labels=%s, headers=%s]",
				signature, model_version, contacts, getId(), getAccountId(), getThreadId(), from, to, cc, bcc, reply_to,
				date, unread, starred, snippet, files, events, folder, labels, getHeaders());
	}
}