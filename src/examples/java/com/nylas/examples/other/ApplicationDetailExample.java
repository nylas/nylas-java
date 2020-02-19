package com.nylas.examples.other;

import com.nylas.ApplicationDetail;
import com.nylas.NylasApplication;
import com.nylas.NylasClient;
import com.nylas.examples.ExampleConf;

public class ApplicationDetailExample {

	public static void main(String[] args) throws Exception {
		ExampleConf conf = new ExampleConf();
		NylasClient client = new NylasClient();
		NylasApplication application = client.application(conf.get("nylas.client.id"), conf.get("nylas.client.secret"));
		ApplicationDetail detail = application.getApplicationDetail();
		System.out.println("existing application details: " + detail);
		
		detail = application.addRedirectUri("https://example.com/bogus_redirect");
		System.out.println("added new uri: " + detail);
		
		detail = application.addRedirectUri("https://example.com/bogus_redirect");
		System.out.println("skipped adding new uri again: " + detail);
		
		detail = application.removeRedirectUri("https://example.com/bogus_redirect");
		System.out.println("removed uri: " + detail);
		
	}

}
