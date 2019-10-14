package com.nylas.examples;

import java.util.Properties;

import com.nylas.NylasApplication;
import com.nylas.HostedAuthentication;
import com.nylas.NylasClient;

public class HostedAuthFetchTokenExample {

	public static void main(String[] args) throws Exception {
		Properties props = Examples.loadExampleProperties();

		NylasClient client = new NylasClient();
		NylasApplication application = client.application(props.getProperty("nylas.client.id"),
				props.getProperty("nylas.client.secret"));
		HostedAuthentication authentication = application.hostedAuthentication();

		String authorizationCode = props.getProperty("hosted.auth.code");
		System.out.println("Exchanging authorication code for long lived access token.");
		String token = authentication.fetchToken(authorizationCode).getAccessToken();
		System.out.println("Authentication successful.  Here is your access token:");
		System.out.println(token);
		System.out.println("If you wish to use this for further examples, add it to your example.properties file");
	}

}
