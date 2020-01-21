package com.nylas;

import static com.nylas.Validations.assertState;
import static com.nylas.Validations.nullOrEmpty;

import java.util.Map;

/**
 * Provider settings for generic IMAP providers
 * 
 * <p>
 * <a href="https://docs.nylas.com/docs/native-authentication#section-provider-specific-settings">
 * https://docs.nylas.com/docs/native-authentication#section-provider-specific-settings</a>
 */
public class ImapProviderSettings extends ProviderSettings {

	private String imap_host;
	private Integer imap_port;
	private String imap_username;
	private String imap_password;
	private String smtp_host;
	private Integer smtp_port;
	private String smtp_username;
	private String smtp_password;
	private Boolean ssl_required;
	
	public ImapProviderSettings() {
		super("imap");
	}

	public ImapProviderSettings imapHost(String imapHost) {
		this.imap_host = imapHost;
		return this;
	}
	
	public ImapProviderSettings imapPort(int imapPort) {
		this.imap_port = imapPort;
		return this;
	}
	
	public ImapProviderSettings imapUsername(String imapUsername) {
		this.imap_username = imapUsername;
		return this;
	}
	
	public ImapProviderSettings imapPassword(String imapPassword) {
		this.imap_password = imapPassword;
		return this;
	}
	
	public ImapProviderSettings smtpHost(String smtpHost) {
		this.smtp_host = smtpHost;
		return this;
	}
	
	public ImapProviderSettings smtpPort(int smtpPort) {
		this.smtp_port = smtpPort;
		return this;
	}
	
	public ImapProviderSettings smtpUsername(String smtpUsername) {
		this.smtp_username = smtpUsername;
		return this;
	}
	
	public ImapProviderSettings smtpPassword(String smtpPassword) {
		this.smtp_password = smtpPassword;
		return this;
	}
	
	public ImapProviderSettings sslRequired(boolean sslRequired) {
		this.ssl_required = Boolean.valueOf(sslRequired);
		return this;
	}

	@Override
	protected void validate() {
		// TODO - which fields are required?
		assertState(!nullOrEmpty(imap_host), "IMAP Host is required");
	}
	
	@Override
	protected void fillSettings(Map<String, Object> settings) {
		// IMAP
		settings.put("imap_host", imap_host);
		if (imap_port != null) {
			settings.put("imap_port", imap_port);
		}
		if (!nullOrEmpty(imap_username)) {
			settings.put("imap_username", imap_username);
		}
		if (!nullOrEmpty(imap_password)) {
			settings.put("imap_password", imap_password);
		}
		
		// SMTP
		if (!nullOrEmpty(smtp_host)) {
			settings.put("smtp_host", smtp_host);
		}
		if (smtp_port != null) {
			settings.put("smtp_port", smtp_port);
		}
		if (!nullOrEmpty(smtp_username)) {
			settings.put("smtp_username", smtp_username);
		}
		if (!nullOrEmpty(smtp_password)) {
			settings.put("smtp_password", smtp_password);
		}

		// SSL
		if (ssl_required != null) {
			settings.put("ssl_required", ssl_required);
		}
	}


}