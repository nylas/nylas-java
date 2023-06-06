package com.nylas;

import java.io.IOException;

public class Components extends RestfulDAO<Component> {

	Components(NylasClient client, NylasApplication application) {
		super(client, Component.class, "component/" + application.getClientId(), application.getClientSecret());
	}

	public RemoteCollection<Component> list() throws IOException, RequestFailedException {
		return list(new ComponentQuery());
	}

	public RemoteCollection<Component> list(ComponentQuery query) throws IOException, RequestFailedException {
		return super.list(query);
	}

	@Override
	public Component get(String id) throws IOException, RequestFailedException {
		return super.get(id);
	}

	public Component create(Component component) throws IOException, RequestFailedException {
		return super.create(component);
	}

	public Component update(Component component) throws IOException, RequestFailedException {
		return super.update(component);
	}

	/**
	 * Delete the scheduler with the given id.
	 * Returns the id of job status for the deletion.
	 */
	public String delete(String id) throws IOException, RequestFailedException {
		return super.delete(id);
	}

	/**
	 * Convenience method to create or update the given scheduler.
	 * If it has an existing ID, update it, otherwise create it.
	 * Returns the scheduler as returned by the server.
	 */
	public Component save(Component component) throws IOException, RequestFailedException {
		return component.hasId() ? update(component) : create(component);
	}
}
