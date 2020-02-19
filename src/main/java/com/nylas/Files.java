package com.nylas;

import java.io.IOException;
import java.util.List;

import com.nylas.NylasClient.HttpMethod;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class Files extends RestfulCollection<File, FileQuery> {

	Files(NylasClient client, String accessToken) {
		super(client, File.class, "files", accessToken);
	}
	
	@Override
	public List<File> list() throws IOException, RequestFailedException {
		return super.list();
	}

	@Override
	public List<File> list(FileQuery query) throws IOException, RequestFailedException {
		return super.list(query);
	}

	@Override
	public File get(String id) throws IOException, RequestFailedException {
		return super.get(id);
	}

	@Override
	public List<String> ids(FileQuery query) throws IOException, RequestFailedException {
		return super.ids(query);
	}
	
	@Override
	public long count(FileQuery query) throws IOException, RequestFailedException {
		return super.count(query);
	}
	
	/**
	 * Download the give file. If the request is successful, returns the raw response body, exposing useful headers
	 * such as Content-Type and Content-Length.
	 * <p>
	 * The returned ResponseBody must be closed:<br>
	 * <a href="https://square.github.io/okhttp/4.x/okhttp/okhttp3/-response-body/#the-response-body-must-be-closed">
	 * https://square.github.io/okhttp/4.x/okhttp/okhttp3/-response-body/#the-response-body-must-be-closed</a>
	 * <p>
	 * Alternately, use downloadBytes to buffer the full response in memory.
	 */
	public ResponseBody download(String fileId) throws IOException, RequestFailedException {
		HttpUrl.Builder url = getInstanceUrl(fileId).addPathSegment("download");
		return client.download(authUser, url);
	}
	
	public byte[] downloadBytes(String fileId) throws IOException, RequestFailedException {
		return download(fileId).bytes();
	}
	
	public File upload(String filename, String contentType, byte[] data) throws IOException, RequestFailedException {
		MediaType mediaType = contentType == null ? null : MediaType.get(contentType);
		RequestBody fileData = RequestBody.create(mediaType, data);
		RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
				.addFormDataPart("file", filename, fileData)
				.build();
		HttpUrl.Builder url = getCollectionUrl();
		List<File> resultList
			= client.executeRequestWithAuth(authUser, url, HttpMethod.POST, requestBody, getModelListType());
		if (resultList.size() != 1) {
			throw new RuntimeException("Server failed to respond with exactly 1 file object, got " + resultList.size()
				+ " instead");
		}
		return resultList.get(0);
	}
	
	@Override
	public void delete(String folderId) throws IOException, RequestFailedException {
		super.delete(folderId);
	}

}
