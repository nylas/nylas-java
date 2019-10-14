package com.nylas.examples;

import java.util.Properties;

import com.nylas.AccessToken;
import com.nylas.Account;
import com.nylas.NylasApplication;
import com.nylas.GoogleProviderSettings;
import com.nylas.NativeAuthentication;
import com.nylas.NylasAccount;
import com.nylas.NativeAuthentication.AuthRequestBuilder;
import com.nylas.NylasClient;
import com.nylas.Scope;

public class NativeAuthImapExample {

	public static void main(String[] args) throws Exception {
		Properties props = Examples.loadExampleProperties();

		GoogleProviderSettings settings = new GoogleProviderSettings()
				.googleClientId(props.getProperty("google.client.id"))
				.googleClientSecret(props.getProperty("google.client.secret"))
				.googleRefreshToken(props.getProperty("google.refresh.token"))
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
				
		System.out.println("Making a native authentication request for a Google account.");
		String authorizationCode = authRequest.execute();
		System.out.println("Succeeded.  Returned authorization code: " + authorizationCode);
		
		System.out.println("Using authorization code to request long lived access token.");
		AccessToken token = authentication.fetchToken(authorizationCode);
		System.out.println("Succeeded.  Returned token: " + token);
		
		System.out.println("Requesting account details with token.");
		NylasAccount account = client.account(token.getAccessToken());
		Account accountInfo = account.fetchAccountByAccessToken();
		System.out.println("Succeeded.  Account details: " + account);
	}

}
