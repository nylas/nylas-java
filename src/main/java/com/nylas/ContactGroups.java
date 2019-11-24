package com.nylas;

import java.io.IOException;
import java.util.List;

/**
 * @see https://docs.nylas.com/reference#contactgroups
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
