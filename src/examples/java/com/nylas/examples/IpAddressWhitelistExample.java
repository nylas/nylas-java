package com.nylas.examples;

import java.util.Properties;

import com.nylas.Application;
import com.nylas.IPAddressWhitelist;
import com.nylas.NylasClient;

public class IpAddressWhitelistExample {

	public static void main(String[] args) throws Exception {
		Properties props = Examples.loadExampleProperties();
		NylasClient client = new NylasClient();
		Application application = client.application(props.getProperty("nylas.client.id"),
				props.getProperty("nylas.client.secret"));
		System.out.println("Fetching IP Address whitelist");
		IPAddressWhitelist ipAddresses = application.fetchIpAddressWhitelist();
		System.out.println("IP Address whitelist: " + ipAddresses);
	}

}
