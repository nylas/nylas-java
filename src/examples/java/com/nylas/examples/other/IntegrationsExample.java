package com.nylas.examples.other;

import com.nylas.*;
import com.nylas.examples.ExampleConf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class IntegrationsExample {

	private static final Logger log = LogManager.getLogger(IntegrationsExample.class);

	public static void main(String[] args) throws Exception {
		ExampleConf conf = new ExampleConf();
		NylasClient client = new NylasClient();
		NylasApplication application = client.application(conf.get("nylas.client.id"), conf.get("nylas.client.secret"));
		Integrations integrations = application.integrations();
		Grants grants = application.grants();

		log.info("Creating a new Zoom integration");
		Integration integration = new Integration("Test Zoom Integration");
		integration.setClientId(conf.get("zoom.client.id"));
		integration.setClientSecret(conf.get("zoom.client.secret"));
		integration.addRedirectUris("https://www.nylas.com");
		integration.setExpiresIn(1209600L);
		Integration created = integrations.create(integration, Integration.Provider.ZOOM);
		log.info("Created: " + created);

		log.info("Listing all integrations:");
		RemoteCollection<Integration> allIntegrations = integrations.list();
		for(Integration currentIntegration : allIntegrations) {
			log.info("Integration: " + currentIntegration);
		}

		log.info("Updating the integration");
		created.setName("Updated Test Zoom Integration");
		created.setClientId(conf.get("zoom.client.id"));
		created.setClientSecret(conf.get("zoom.client.secret"));
		Integration update = integrations.update(created);
		log.info("Updated: " + update);

		grantsExample(conf, grants);

		log.info("Deleting integration");
		integrations.delete(Integration.Provider.ZOOM);
	}

	private static void grantsExample(ExampleConf conf, Grants grants) throws RequestFailedException, IOException {
		log.info("Creating a new Zoom grant");
		Map<String, String> settings = Collections.singletonMap("refresh_token", conf.get("zoom.client.refresh_token"));
		Grant grant = new Grant(Integration.Provider.ZOOM, settings);
		Grant created = grants.create(grant);
		log.info("Created: " + created);

		log.info("Listing all grants:");
		RemoteCollection<Grant> allGrants = grants.list();
		for(Grant currentGrant : allGrants) {
			log.info("Grant: " + currentGrant);
		}

		log.info("Deleting grant");
		grants.delete(created.getId());
	}
}
