package com.nylas.examples;

import java.util.Properties;

import com.nylas.Application;
import com.nylas.HostedAuthentication;
import com.nylas.NylasClient;
import com.nylas.Scope;

public class HostedAuthUrlExample {

	public static void main(String[] args) throws Exception {
		Properties props = Examples.loadExampleProperties();

		NylasClient client = new NylasClient();
		Application application = client.application(props.getProperty("nylas.client.id"),
				props.getProperty("nylas.client.secret"));
		HostedAuthentication authentication = application.hostedAuthentication();
		String hostedAuthUrl = authentication.urlBuilder()
			.redirectUri("https://example.com/nylas-redirect")
			.responseType("code")
			.scopes(Scope.EMAIL, Scope.CALENDAR, Scope.CONTACTS)
			.loginHint(props.getProperty("hosted.login.hint"))
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
