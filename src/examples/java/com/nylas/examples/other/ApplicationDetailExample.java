package com.nylas.examples.other;

import com.nylas.ApplicationDetail;
import com.nylas.NylasApplication;
import com.nylas.NylasClient;
import com.nylas.examples.ExampleConf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ApplicationDetailExample {

	private static final Logger log = LogManager.getLogger(ApplicationDetailExample.class);

	public static void main(String[] args) throws Exception {
		ExampleConf conf = new ExampleConf();
		NylasClient client = new NylasClient();
		NylasApplication application = client.application(conf.get("nylas.client.id"), conf.get("nylas.client.secret"));
		ApplicationDetail detail = application.getApplicationDetail();
		log.info("existing application details: " + detail);
		
		detail = application.addRedirectUri("https://example.com/bogus_redirect");
		log.info("added new uri: " + detail);
		
		detail = application.addRedirectUri("https://example.com/bogus_redirect");
		log.info("skipped adding new uri again: " + detail);
		
		detail = application.removeRedirectUri("https://example.com/bogus_redirect");
		log.info("removed uri: " + detail);
		
	}

}
