package com.nylas;

import java.io.IOException;
import java.util.List;

/**
 * <a href="https://docs.nylas.com/reference#contactsgroups">https://docs.nylas.com/reference#contactsgroups</a>
 */
public class ContactGroups extends RestfulDAO<ContactGroup> {

	ContactGroups(NylasClient client, String accessToken) {
		super(client, ContactGroup.class, "contacts/groups", accessToken);
	}
	
	public List<ContactGroup> listAll() throws IOException, RequestFailedException {
		return fetchQuery(null, null, getModelListType());
	}

}
