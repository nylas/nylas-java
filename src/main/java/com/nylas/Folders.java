package com.nylas;

import java.io.IOException;

/**
 * @see https://docs.nylas.com/reference#folders
 */
public class Folders extends RestfulCollection<Folder, FolderQuery>{

	Folders(NylasClient client, String accessToken) {
		super(client, Folder.class, "folders", accessToken);
	}
	
	public Folder create(String displayName) throws IOException, RequestFailedException {
		return super.create(Maps.of("display_name", displayName));
	}
	
	public void delete(String folderId) throws IOException, RequestFailedException {
		super.delete(folderId);
	}
	
	/**
	 * Change the display name of the given folder.
	 * Note that the core folders such as INBOX, Trash, etc. often cannot be renamed
	 */
	public Folder setDisplayName(String folderId, String displayName) throws IOException, RequestFailedException {
		return super.put(folderId, Maps.of("display_name", displayName));
	}
	
	/**
	 * Update the given folder as the primary sent folder.
	 * This feature is supported for custom IMAP accounts only.
	 */
	public Folder setAsSentFolder(String folderId) throws IOException, RequestFailedException {
		return super.put(folderId, Maps.of("name", "sent"));
	}

}
