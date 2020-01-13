package com.nylas.examples;

import java.util.Properties;

import com.nylas.AccessToken;
import com.nylas.AccountDetail;
import com.nylas.ImapProviderSettings;
import com.nylas.NativeAuthentication;
import com.nylas.NativeAuthentication.AuthRequestBuilder;
import com.nylas.NylasAccount;
import com.nylas.NylasApplication;
import com.nylas.NylasClient;
import com.nylas.Scope;

public class NativeAuthImapExample {

	public static void main(String[] args) throws Exception {
		Properties props = Examples.loadExampleProperties();

		ImapProviderSettings settings = new ImapProviderSettings()
				.imapHost(props.getProperty("imap.host"))
				.imapPort(Integer.parseInt(props.getProperty("imap.port")))
				.imapUsername(props.getProperty("imap.username"))
				.imapPassword(props.getProperty("imap.password"))
				.smtpHost(props.getProperty("smtp.host"))
				.smtpPort(Integer.parseInt(props.getProperty("smtp.port")))
				.smtpUsername(props.getProperty("smtp.username"))
				.smtpPassword(props.getProperty("smtp.password"))
				.sslRequired(true)
				;

		NylasClient client = new NylasClient();
		NylasApplication application = client.application(props.getProperty("nylas.client.id"),
				props.getProperty("nylas.client.secret"));
		NativeAuthentication authentication = application.nativeAuthentication();
		AuthRequestBuilder authRequest = authentication.authRequest()
				.name(props.getProperty("imap.name"))
				.emailAddress(props.getProperty("imap.email"))
				.providerSettings(settings)
				.scopes(Scope.EMAIL, Scope.CALENDAR, Scope.CONTACTS);
				
		System.out.println("Making a native authentication request for a custom IMAP account.");
		String authorizationCode = authRequest.execute();
		System.out.println("Succeeded.  Returned authorization code: " + authorizationCode);
		
		System.out.println("Using authorization code to request long lived access token.");
		AccessToken token = authentication.fetchToken(authorizationCode);
		System.out.println("Succeeded.  Returned token: " + token);
		
		System.out.println("Requesting account details with token.");
		NylasAccount account = client.account(token.getAccessToken());
		AccountDetail accountDetail = account.fetchAccountByAccessToken();
		System.out.println("Succeeded.  Account details: " + accountDetail);
	}

}
