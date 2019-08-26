package com.nylas;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.squareup.moshi.Types;

import okhttp3.HttpUrl;

/**
 * @see https://docs.nylas.com/reference#folders
 */
public class Folders {

	private final NylasClient client;
	private final String accessToken;
	
	Folders(NylasClient client, String accessToken) {
		this.client = client;
		this.accessToken = accessToken;
	}
	
	private static final Type FOLDER_LIST_TYPE = Types.newParameterizedType(List.class, Folder.class);
	public List<Folder> list() throws IOException, RequestFailedException {
		HttpUrl foldersUrl = client.getBaseUrl().resolve("folders");
		return client.executeGetWithToken(accessToken, foldersUrl, FOLDER_LIST_TYPE);
	}
	
	public Folder get(String folderId) throws IOException, RequestFailedException {
		HttpUrl folderUrl = getFolderUrl(folderId);
		return client.executeGetWithToken(accessToken, folderUrl, Folder.class);
	}
	
	public Folder create(String displayName) throws IOException, RequestFailedException {
		HttpUrl foldersUrl = client.getBaseUrl().resolve("folders");
		Map<String, Object> params = Maps.of("display_name", displayName);
		return client.executePostWithToken(accessToken, foldersUrl, params, Folder.class);
	}
	
	public void delete(String folderId) throws IOException, RequestFailedException {
		HttpUrl folderUrl = getFolderUrl(folderId);
		client.executeDeleteWithToken(accessToken, folderUrl, null);
	}
	
	public Folder setDisplayName(String folderId, String displayName) throws IOException, RequestFailedException {
		return updateFolder(folderId, Maps.of("display_name", displayName));
	}
	
	/**
	 * Update the given folder as the primary sent folder.
	 * This feature is supported for custom IMAP accounts only.
	 */
	public Folder setAsSentFolder(String folderId) throws IOException, RequestFailedException {
		return updateFolder(folderId, Maps.of("name", "sent"));
	}
	
	private Folder updateFolder(String folderId, Map<String, Object> params)
			throws IOException, RequestFailedException {
		HttpUrl folderUrl = getFolderUrl(folderId);
		return client.executePutWithToken(accessToken, folderUrl, params, Folder.class);
	}
	
	private HttpUrl getFolderUrl(String folderId) {
		return client.getBaseUrl().newBuilder()
				.addPathSegment("folders")
				.addPathSegment(folderId)
				.build();
	}
}
