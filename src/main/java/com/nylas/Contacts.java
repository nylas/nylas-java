package com.nylas;

import java.io.IOException;

/**
 * @see https://docs.nylas.com/reference#contacts
 */
public class Contacts extends RestfulCollection<Contact, ContactQuery> {

	Contacts(NylasClient client, String accessToken) {
		super(client, Contact.class, "contacts", accessToken);
	}
	
	public Contact create(Contact draft) throws IOException, RequestFailedException {
		return super.create(draft);
	}
	
	public Contact update(Contact draft) throws IOException, RequestFailedException {
		return super.update(draft);
	}
}
