package com.nylas;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import com.nylas.NylasClient.HttpMethod;
import com.squareup.moshi.Types;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Files extends RestfulCollection<File, FileQuery> {

	Files(NylasClient client, String accessToken) {
		super(client, File.class, "files", accessToken);
	}
	
	public List<String> ids(FileQuery query) throws IOException, RequestFailedException {
		return super.ids(query);
	}
	
	public long count(FileQuery query) throws IOException, RequestFailedException {
		return super.count(query);
	}
	
	/**
	 * Download the give file. If the request is successful, returns the raw response body, exposing useful headers
	 * such as Content-Type and Content-Length.
	 * 
	 * The returned ResponseBody must be closed
	 * @see https://square.github.io/okhttp/4.x/okhttp/okhttp3/-response-body/#the-response-body-must-be-closed
	 * 
	 * Alternately, use downloadBytes to buffer the full response in memory.
	 */
	public ResponseBody download(String fileId) throws IOException, RequestFailedException {
		HttpUrl.Builder url = getInstanceUrlBuilder(fileId).addPathSegment("download");
		Request request = client.buildRequest(authUser, url, HttpMethod.GET, null);
		Response response = client.getHttpClient().newCall(request).execute();
		if (!response.isSuccessful()) {
			response.close();
			throw new RequestFailedException(response.code(), response.body().string());
		}
		return response.body();
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
		HttpUrl.Builder url = getCollectionUrlBuilder();
		Type listType = Types.newParameterizedType(List.class, modelClass);
		List<File> resultList
			= client.executeRequestWithAuth(authUser, url, HttpMethod.POST, requestBody, listType);
		if (resultList.size() != 1) {
			throw new RuntimeException("Server failed to respond with exactly 1 file object, got " + resultList.size()
				+ " instead");
		}
		return resultList.get(0);
	}
	
	public void delete(String folderId) throws IOException, RequestFailedException {
		super.delete(folderId);
	}

}
