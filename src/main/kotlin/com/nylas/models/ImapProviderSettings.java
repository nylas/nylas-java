package com.nylas.models;

/**
 * Provider settings for generic IMAP providers
 * 
 * <p>
 * <a href="https://docs.nylas.com/docs/native-authentication#section-provider-specific-settings">
 * https://docs.nylas.com/docs/native-authentication#section-provider-specific-settings</a>
 */
public class ImapProviderSettings extends ProviderSettings {

	protected ImapProviderSettings() {
		super(NativeAuthentication.Provider.IMAP.getName());
	}

	public ImapProviderSettings imapHost(String imapHost) {
		add("imap_host", imapHost);
		return this;
	}
	
	public ImapProviderSettings imapPort(int imapPort) {
		add("imap_port", imapPort);
		return this;
	}
	
	public ImapProviderSettings imapUsername(String imapUsername) {
		add("imap_username", imapUsername);
		return this;
	}
	
	public ImapProviderSettings imapPassword(String imapPassword) {
		add("imap_password", imapPassword);
		return this;
	}
	
	public ImapProviderSettings smtpHost(String smtpHost) {
		add("smtp_host", smtpHost);
		return this;
	}
	
	public ImapProviderSettings smtpPort(int smtpPort) {
		add("smtp_port", smtpPort);
		return this;
	}
	
	public ImapProviderSettings smtpUsername(String smtpUsername) {
		add("smtp_username", smtpUsername);
		return this;
	}
	
	public ImapProviderSettings smtpPassword(String smtpPassword) {
		add("smtp_password", smtpPassword);
		return this;
	}
	
	public ImapProviderSettings sslRequired(boolean sslRequired) {
		add("ssl_required", sslRequired);
		return this;
	}

	@Override
	protected void validate() {
		assertSetting("imap_host", "IMAP Host is required");
		assertSetting("imap_username", "IMAP Username is required");
		assertSetting("imap_password", "IMAP Password is required");
		assertSetting("smtp_host", "SMTP Host is required");
		assertSetting("smtp_username", "SMTP Host is required");
		assertSetting("smtp_password", "SMTP Host is required");
	}

}