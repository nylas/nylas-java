package com.nylas.examples.other;

import com.nylas.*;
import com.nylas.examples.ExampleConf;

public class ComponentsExample {

	public static void main(String[] args) throws Exception {

		ExampleConf conf = new ExampleConf();
		NylasClient client = new NylasClient();
		NylasApplication application = client.application(conf.get("nylas.client.id"), conf.get("nylas.client.secret"));
		Components components = application.components();

		Component component = new Component();
		component.setName("Java Component Test");
		component.setType("agenda");
		component.setPublicAccountId("ACCOUNT_ID");
		component.setAccessToken(conf.get("access.token"));
		component = components.save(component);

		RemoteCollection<Component> componentList = components.list();
		for(Component comp : componentList) {
			System.out.println(comp);
		}

		Component fetchedComponent = components.get(component.getId());
		fetchedComponent.setName("Updated component name");
		fetchedComponent.addAllowedDomains("www.nylas.com");
		fetchedComponent.addSetting("Test", "yes");
		components.save(fetchedComponent);

		components.delete(fetchedComponent.getId());
	}
}
