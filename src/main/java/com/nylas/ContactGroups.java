package com.nylas;

/**
 * @see https://docs.nylas.com/reference#contactgroups
 */
public class ContactGroups extends RestfulCollection<ContactGroup, ContactGroupQuery> {

	ContactGroups(NylasClient client, String accessToken) {
		super(client, ContactGroup.class, "contacts/groups", accessToken);
	}

}
