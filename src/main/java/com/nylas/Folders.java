package com.nylas;

import java.io.IOException;
import java.util.List;

/**
 * <a href="https://docs.nylas.com/reference#folders">https://docs.nylas.com/reference#folders</a>
 */
public class Folders extends RestfulCollection<Folder, FolderQuery>{

	Folders(NylasClient client, String accessToken) {
		super(client, Folder.class, "folders", accessToken);
	}
	
	@Override
	public List<Folder> list() throws IOException, RequestFailedException {
		return super.list();
	}

	@Override
	public List<Folder> list(FolderQuery query) throws IOException, RequestFailedException {
		return super.list(query);
	}

	@Override
	public Folder get(String id) throws IOException, RequestFailedException {
		return super.get(id);
	}

	public Folder create(String displayName) throws IOException, RequestFailedException {
		return super.create(Maps.of("display_name", displayName));
	}
	
	@Override
	public void delete(String folderId) throws IOException, RequestFailedException {
		super.delete(folderId);
	}
	
	/**
	 * Change the display name of the given folder.
	 * Note that the core folders such as INBOX, Trash, etc. often cannot be renamed
	 */
	public Folder setDisplayName(String folderId, String displayName) throws IOException, RequestFailedException {
		return super.update(folderId, Maps.of("display_name", displayName));
	}
	
	/**
	 * Update the given folder as the primary sent folder.
	 * This feature is supported for custom IMAP accounts only.
	 */
	public Folder setAsSentFolder(String folderId) throws IOException, RequestFailedException {
		return super.update(folderId, Maps.of("name", "sent"));
	}

}
