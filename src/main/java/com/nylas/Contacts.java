package com.nylas;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.ResponseBody;

/**
 * @see https://docs.nylas.com/reference#contacts
 */
public class Contacts extends RestfulCollection<Contact, ContactQuery> {

	Contacts(NylasClient client, String accessToken) {
		super(client, Contact.class, "contacts", accessToken);
	}
	
	@Override
	public List<Contact> list() throws IOException, RequestFailedException {
		return super.list();
	}

	@Override
	public List<Contact> list(ContactQuery query) throws IOException, RequestFailedException {
		return super.list(query);
	}

	@Override
	public Contact get(String id) throws IOException, RequestFailedException {
		return super.get(id);
	}

	@Override
	public Contact create(Contact draft) throws IOException, RequestFailedException {
		return super.create(draft);
	}
	
	@Override
	public Contact update(Contact draft) throws IOException, RequestFailedException {
		return super.update(draft);
	}

	@Override
	public void delete(String id) throws IOException, RequestFailedException {
		super.delete(id);
	}
	
	/**
	 * Download the profile picture for the given contact.
	 * If the request is successful, returns the raw response body, exposing useful headers
	 * such as Content-Type and Content-Length.
	 * 
	 * The returned ResponseBody must be closed
	 * @see https://square.github.io/okhttp/4.x/okhttp/okhttp3/-response-body/#the-response-body-must-be-closed
	 * 
	 * @throws RequestFailedException if the given contact does not exist or does not have a profile picture
	 */
	public ResponseBody downloadProfilePicture(String contactId) throws IOException, RequestFailedException {
		HttpUrl.Builder url = getInstanceUrl(contactId).addPathSegment("picture");
		return client.download(authUser, url);
	}
}
