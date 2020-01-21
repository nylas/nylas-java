package com.nylas.examples;

import java.util.Arrays;

import com.nylas.AccessToken;
import com.nylas.AccountDetail;
import com.nylas.MicrosoftExchangeProviderSettings;
import com.nylas.NativeAuthentication;
import com.nylas.NativeAuthentication.AuthRequestBuilder;
import com.nylas.NylasAccount;
import com.nylas.NylasApplication;
import com.nylas.NylasClient;
import com.nylas.Scope;

public class NativeAuthExchangeExample {

	public static void main(String[] args) throws Exception {
		ExampleConf conf = new ExampleConf();

		String email = conf.get("exchange.email");
		MicrosoftExchangeProviderSettings settings = new MicrosoftExchangeProviderSettings()
				.username(email)
				.password(conf.get("exchange.password"))
				.easServerHost(conf.getOrNull("exchange.server"))
				;

		NylasClient client = new NylasClient();
		NylasApplication application = client.application(conf.get("nylas.client.id"), conf.get("nylas.client.secret"));
		NativeAuthentication authentication = application.nativeAuthentication();
		AuthRequestBuilder authRequest = authentication.authRequest()
				.name(conf.get("exchange.name"))
				.emailAddress(email)
				.providerSettings(settings)
				.scopes(Arrays.asList(Scope.values()));
				
		System.out.println("Making a native authentication request for a Microsoft Exchange account.");
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
