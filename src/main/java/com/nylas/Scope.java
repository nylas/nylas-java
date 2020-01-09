package com.nylas;

/**
 * Scope limiting what operations are authorized on what resources
 * <p>
 * <a href="https://docs.nylas.com/docs/authentication-scopes">https://docs.nylas.com/docs/authentication-scopes</a>
 */
public enum Scope {

	/**
	 * Full email permissions, including read, modify, and send emails, folders, and labels
	 */
	EMAIL("email"),
	
	/**
	 * Read and modify all messages, threads, file attachments, and read email metadata like headers.
	 * Does not include send.
	 */
	EMAIL_MODIFY("email.modify"),
	
	/**
	 * Read all messages, threads, file attachments, drafts, and email metadata like headers—no write operations.
	 */
	EMAIL_READ_ONLY("email.read_only"),
	
	/**
	 * Send messages only. No read or modify privileges on users' emails.
	 */
	EMAIL_SEND("email.send"),
	
	/**
	 * Read and modify folders or labels, depending on the account type.
	 */
	EMAIL_FOLDERS_AND_LABELS("email.folders_and_labels"),
	
	/**
	 * Read email metadata including headers and labels/folders, but not the message body or file attachments.
	 */
	EMAIL_METADATA("email.metadata"),
	
	/**
	 * Read and modify drafts. Does not include send.
	 */
	EMAIL_DRAFTS("email.drafts"),
	
	/**
	 * Read and modify calendars and events.
	 */
	CALENDAR("calendar"),
	
	/**
	 * Read calendars and events.
	 */
	CALENDAR_READ_ONLY("calendar.read_only"),
	
	/**
	 * Read and modify contacts.
	 */
	CONTACTS("contacts"),
	
	/**
	 * Read contacts.
	 */
	CONTACTS_READ_ONLY("contacts.read_only"),
	
	;
	
	private final String name;
	
	private Scope(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
}
