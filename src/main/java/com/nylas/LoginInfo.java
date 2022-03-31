package com.nylas;

import com.nylas.Authentication.Provider;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class LoginInfo {

	/** Login ID */
	private String id;
	/** Final OAuth login url */
	private String url;
	/** Date timestamp when this login url expires and is no longer valid */
	private Long expires_at;
	/** Details of the requested OAuth */
	private Request request;

	public String getId() {
		return id;
	}

	public String getUrl() {
		return url;
	}

	public Long getExpiresAt() {
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
		LoginInfo fromJson(JsonReader reader, JsonAdapter<LoginInfo> delegate) throws IOException {
			Map<String, Object> json = JsonHelper.jsonToMap(reader);

			if(json.get("data") != null) {
				json = (Map<String, Object>) json.get("data");
			}
			return delegate.fromJson(JsonHelper.mapToJson(json));
		}
	}

	public static class Request {

		/** Redirect URI that was requested as part of the final hosted OAuth step */
		private String redirect_uri;
		/** OAuth provider */
		private String provider;
		/** Hint to simplify the login flow */
		private String login_hint;
		/** State value to return after authentication flow is completed */
		private String state;
		/** Requested scopes for this OAuth */
		private List<String> scope;
		/** Metadata stored as part of the request */
		private Map<String, Object> metadata;

		public String getRedirectUri() {
			return redirect_uri;
		}

		public Provider getProvider() {
			return Provider.valueOf(this.provider);
		}

		public String getProviderString() {
			return provider;
		}

		public String getLoginHint() {
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
