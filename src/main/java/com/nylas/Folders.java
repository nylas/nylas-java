package com.nylas;

import java.io.IOException;

/**
 * <a href="https://docs.nylas.com/reference#folders">https://docs.nylas.com/reference#folders</a>
 */
public class Folders extends RestfulDAO<Folder> {

	Folders(NylasClient client, String accessToken) {
		super(client, Folder.class, "folders", accessToken);
	}
	
	public RemoteCollection<Folder> list() throws IOException, RequestFailedException {
		return list(new FolderQuery());
	}
	
	public RemoteCollection<Folder> list(FolderQuery query) throws IOException, RequestFailedException {
		return super.list(query);
	}

	@Override
	public Folder get(String id) throws IOException, RequestFailedException {
		return super.get(id);
	}

	public Folder create(String displayName) throws IOException, RequestFailedException {
		return super.create(Maps.of("display_name", displayName));
	}
	
	/**
	 * Delete the folder with the given id.
	 * Returns the id of job status for the deletion.
	 */
	@Override
	public String delete(String folderId) throws IOException, RequestFailedException {
		return super.delete(folderId);
	}

	@Override
	public Folder update(Folder folder) throws RequestFailedException, IOException {
		return super.update(folder);
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
