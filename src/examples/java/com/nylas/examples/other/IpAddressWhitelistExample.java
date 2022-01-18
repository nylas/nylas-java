package com.nylas.examples.other;

import com.nylas.IPAddressWhitelist;
import com.nylas.NylasApplication;
import com.nylas.NylasClient;
import com.nylas.examples.ExampleConf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IpAddressWhitelistExample {

	private static final Logger log = LogManager.getLogger(IpAddressWhitelistExample.class);

	public static void main(String[] args) throws Exception {
		ExampleConf conf = new ExampleConf();
		NylasClient client = new NylasClient();
		NylasApplication application = client.application(conf.get("nylas.client.id"), conf.get("nylas.client.secret"));
		log.info("Fetching IP Address whitelist");
		IPAddressWhitelist ipAddresses = application.fetchIpAddressWhitelist();
		log.info("IP Address whitelist: " + ipAddresses);
	}

}
