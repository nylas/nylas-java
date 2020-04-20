package com.nylas;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.ResponseBody;

/**
 * <a href="https://docs.nylas.com/reference#contacts">https://docs.nylas.com/reference#contacts</a>
 */
public class Contacts extends RestfulDAO<Contact> {

	Contacts(NylasClient client, String accessToken) {
		super(client, Contact.class, "contacts", accessToken);
	}
	
	public RemoteCollection<Contact> list(ContactQuery query) throws IOException, RequestFailedException {
		return super.list(query);
	}

	@Override
	public Contact get(String id) throws IOException, RequestFailedException {
		return super.get(id);
	}

	@Override
	public Contact create(Contact contact) throws IOException, RequestFailedException {
		return super.create(contact);
	}
	
	@Override
	public Contact update(Contact contact) throws IOException, RequestFailedException {
		return super.update(contact);
	}

	@Override
	public void delete(String id) throws IOException, RequestFailedException {
		super.delete(id);
	}
	
	/**
	 * Download the profile picture for the given contact.
	 * If the request is successful, returns the raw response body, exposing useful headers
	 * such as Content-Type and Content-Length.
	 * <p>
	 * The returned ResponseBody must be closed:<br>
	 * <a href="https://square.github.io/okhttp/4.x/okhttp/okhttp3/-response-body/#the-response-body-must-be-closed">
	 * https://square.github.io/okhttp/4.x/okhttp/okhttp3/-response-body/#the-response-body-must-be-closed</a>
	 * 
	 * @throws RequestFailedException if the given contact does not exist or does not have a profile picture
	 */
	public ResponseBody downloadProfilePicture(String contactId) throws IOException, RequestFailedException {
		HttpUrl.Builder url = getInstanceUrl(contactId).addPathSegment("picture");
		return client.download(authUser, url);
	}
}
