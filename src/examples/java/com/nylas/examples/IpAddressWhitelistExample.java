package com.nylas.examples;

import com.nylas.IPAddressWhitelist;
import com.nylas.NylasApplication;
import com.nylas.NylasClient;

public class IpAddressWhitelistExample {

	public static void main(String[] args) throws Exception {
		ExampleConf conf = new ExampleConf();
		NylasClient client = new NylasClient();
		NylasApplication application = client.application(conf.get("nylas.client.id"), conf.get("nylas.client.secret"));
		System.out.println("Fetching IP Address whitelist");
		IPAddressWhitelist ipAddresses = application.fetchIpAddressWhitelist();
		System.out.println("IP Address whitelist: " + ipAddresses);
	}

}
