package com.nylas;

import java.util.Locale;

@Deprecated
public enum Provider {

	GMAIL,
	YAHOO,
	EXCHANGE,
	OUTLOOK,
	IMAP,
	OFFICE365,
	ICLOUD,
	HOTMAIL,
	AOL,
	
	;
	
	public String lowercase() {
		return name().toLowerCase(Locale.ROOT);
	}
	
}
