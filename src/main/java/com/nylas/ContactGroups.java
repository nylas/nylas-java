package com.nylas;

import java.io.IOException;
import java.util.List;

/**
 * <a href="https://docs.nylas.com/reference#contactsgroups">https://docs.nylas.com/reference#contactsgroups</a>
 */
public class ContactGroups extends RestfulCollection<ContactGroup, UnsupportedQuery> {

	ContactGroups(NylasClient client, String accessToken) {
		super(client, ContactGroup.class, "contacts/groups", accessToken);
	}
	
	@Override
	public List<ContactGroup> list() throws IOException, RequestFailedException {
		return super.list();
	}

}
