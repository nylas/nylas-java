package com.nylas.examples;

import com.nylas.AccessToken;
import com.nylas.AccountDetail;
import com.nylas.KnownImapProviderSettings;
import com.nylas.NativeAuthentication;
import com.nylas.NativeAuthentication.AuthRequestBuilder;
import com.nylas.NylasAccount;
import com.nylas.NylasApplication;
import com.nylas.NylasClient;
import com.nylas.ProviderSettings;
import com.nylas.Scope;

public class NativeAuthKnownImapExample {

	public static void main(String[] args) throws Exception {
		ExampleConf conf = new ExampleConf();

		String email = conf.get("known.imap.email");
		ProviderSettings settings = new KnownImapProviderSettings(conf.get("known.imap.provider.name"))
				.password(conf.get("known.imap.password"))
				;

		NylasClient client = new NylasClient();
		NylasApplication application = client.application(conf.get("nylas.client.id"), conf.get("nylas.client.secret"));
		NativeAuthentication authentication = application.nativeAuthentication();
		AuthRequestBuilder authRequest = authentication.authRequest()
				.name(conf.get("known.imap.name"))
				.emailAddress(email)
				.providerSettings(settings)
				.scopes(Scope.values());
				
		System.out.println("Making a native authentication request for a " + settings.getName() + " account.");
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
