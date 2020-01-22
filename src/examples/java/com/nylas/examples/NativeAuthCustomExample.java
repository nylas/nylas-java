package com.nylas.examples;

import java.util.Arrays;

import com.nylas.AccessToken;
import com.nylas.AccountDetail;
import com.nylas.CustomProviderSettings;
import com.nylas.NativeAuthentication;
import com.nylas.NativeAuthentication.AuthRequestBuilder;
import com.nylas.NylasAccount;
import com.nylas.NylasApplication;
import com.nylas.NylasClient;
import com.nylas.ProviderSettings;
import com.nylas.Scope;

public class NativeAuthCustomExample {

	public static void main(String[] args) throws Exception {
		ExampleConf conf = new ExampleConf();

		String email = conf.get("custom.provider.email");
		ProviderSettings settings = new CustomProviderSettings(conf.get("custom.provider.name"))
				.addAll(conf.getPrefixedEntries("custom.provider.setting."));

		System.out.println("Using custom settings: " + settings);
		
		NylasClient client = new NylasClient();
		NylasApplication application = client.application(conf.get("nylas.client.id"), conf.get("nylas.client.secret"));
		NativeAuthentication authentication = application.nativeAuthentication();
		AuthRequestBuilder authRequest = authentication.authRequest()
				.name(conf.get("custom.provider.user.name"))
				.emailAddress(email)
				.providerSettings(settings)
				.scopes(Arrays.asList(Scope.values()));
				
		System.out.println("Making a native authentication request.");
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
