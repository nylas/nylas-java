package com.nylas;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

public class MockNylas {

	final NylasClient client;
	final WireMockServer server;
	
	
	MockNylas() {
		server = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
		server.start();
		client = new NylasClient.Builder().baseUrl(server.baseUrl()).build();
	}
	
	void cleanup() {
		server.stop();
	}

	
}
