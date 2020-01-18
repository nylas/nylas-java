package com.nylas.examples;

import com.nylas.HostedAuthentication;
import com.nylas.NylasApplication;
import com.nylas.NylasClient;
import com.nylas.Scope;

public class HostedAuthUrlExample {

	public static void main(String[] args) throws Exception {
		ExampleConf conf = new ExampleConf();
		NylasClient client = new NylasClient();
		NylasApplication application = client.application(conf.get("nylas.client.id"), conf.get("nylas.client.secret"));
		HostedAuthentication authentication = application.hostedAuthentication();
		String hostedAuthUrl = authentication.urlBuilder()
			.redirectUri("https://example.com/nylas-redirect")
			.responseType("code")
			.scopes(Scope.EMAIL, Scope.CALENDAR, Scope.CONTACTS, Scope.ROOM_RESOURCES_READ_ONLY)
			.loginHint(conf.get("hosted.login.hint"))
			.state("example_csrf_token")
			.buildUrl();
		
		System.out.println("Forward the user to this URL for hosted authentication:");
		System.out.println(hostedAuthUrl);
		System.out.println();
		
		System.out.println("If the user authenticates and grants permission for Nylas to access their account,");
		System.out.println("then grab the authorication code included when the user is redirected to your server.");
		System.out.println("Enter it in example.properties to use it for the fetch token example");

	}

}
