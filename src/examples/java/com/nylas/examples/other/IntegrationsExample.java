package com.nylas.examples.other;

import com.nylas.*;
import com.nylas.examples.ExampleConf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IntegrationsExample {

	private static final Logger log = LogManager.getLogger(IntegrationsExample.class);

	public static void main(String[] args) throws Exception {
		ExampleConf conf = new ExampleConf();
		NylasClient client = new NylasClient();
		NylasApplication application = client.application(conf.get("nylas.client.id"), conf.get("nylas.client.secret"));
		Integrations integrations = application.integrations();

		log.info("Listing all integrations:");
		RemoteCollection<Integration> allIntegrations = integrations.list();
		for(Integration integration : allIntegrations) {
			log.info("Integration: " + integration);
		}

		log.info("Creating a new Zoom integration");
		Integration integration = new Integration("Test Zoom Integration");
		integration.setClientId(conf.get("zoom.client.id"));
		integration.setClientSecret(conf.get("zoom.client.secret"));
		integration.addRedirectUris("https://www.nylas.com");
		integration.setExpiresIn(1209600L);
		Integration created = integrations.create(integration, Integration.Provider.ZOOM);
		log.info("Created: " + created);

		log.info("Updating the integration");
		created.setName("Updated Test Zoom Integration");
		created.setClientId(conf.get("zoom.client.id"));
		created.setClientSecret(conf.get("zoom.client.secret"));
		Integration update = integrations.update(created);
		log.info("Updated: " + update);

		log.info("Deleting integration");
		integrations.delete(Integration.Provider.ZOOM);
	}
}
