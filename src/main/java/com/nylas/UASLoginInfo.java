package com.nylas;

import com.nylas.UAS.Provider;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class UASLoginInfo {

	private String id;
	private String url;
	private Long expires_at;
	private Request request;

	public String getId() {
		return id;
	}

	public String getUrl() {
		return url;
	}

	public Long getExpires_at() {
		return expires_at;
	}

	public Request getRequest() {
		return request;
	}

	@Override
	public String toString() {
		return "UASLoginInfo [" +
				"id='" + id + '\'' +
				", url='" + url + '\'' +
				", expires_at=" + expires_at +
				", request=" + request +
				']';
	}

	/**
	 * This adapter works around the API returning the UAS login info object within a nested "data" key
	 */
	@SuppressWarnings("unchecked")
	static class UASLoginInfoCustomAdapter {
		@FromJson
		UASLoginInfo fromJson(JsonReader reader, JsonAdapter<UASLoginInfo> delegate) throws IOException {
			Map<String, Object> json = JsonHelper.jsonToMap(reader);

			if(json.get("data") != null) {
				json = (Map<String, Object>) json.get("data");
			}
			return delegate.fromJson(JsonHelper.mapToJson(json));
		}
	}

	public static class Request {

		private String redirect_uri;
		private String provider;
		private String login_hint;
		private String state;
		private List<String> scope;
		private Map<String, Object> metadata;

		public String getRedirect_uri() {
			return redirect_uri;
		}

		public Provider getProvider() {
			return Provider.valueOf(this.provider);
		}

		public String getProviderString() {
			return provider;
		}

		public String getLogin_hint() {
			return login_hint;
		}

		public String getState() {
			return state;
		}

		public List<String> getScope() {
			return scope;
		}

		public Map<String, Object> getMetadata() {
			return metadata;
		}

		@Override
		public String toString() {
			return "Request [" +
					"redirect_uri='" + redirect_uri + '\'' +
					", provider='" + provider + '\'' +
					", login_hint='" + login_hint + '\'' +
					", state='" + state + '\'' +
					", scope=" + scope +
					", metadata=" + metadata +
					']';
		}
	}
}
