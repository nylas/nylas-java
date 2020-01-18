package com.nylas.examples;

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
		ExampleConf conf = new ExampleConf();

		ImapProviderSettings settings = new ImapProviderSettings()
				.imapHost(conf.get("imap.host"))
				.imapPort(Integer.parseInt(conf.get("imap.port")))
				.imapUsername(conf.get("imap.username"))
				.imapPassword(conf.get("imap.password"))
				.smtpHost(conf.get("smtp.host"))
				.smtpPort(Integer.parseInt(conf.get("smtp.port")))
				.smtpUsername(conf.get("smtp.username"))
				.smtpPassword(conf.get("smtp.password"))
				.sslRequired(true)
				;

		NylasClient client = new NylasClient();
		NylasApplication application = client.application(conf.get("nylas.client.id"), conf.get("nylas.client.secret"));
		NativeAuthentication authentication = application.nativeAuthentication();
		AuthRequestBuilder authRequest = authentication.authRequest()
				.name(conf.get("imap.name"))
				.emailAddress(conf.get("imap.email"))
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
