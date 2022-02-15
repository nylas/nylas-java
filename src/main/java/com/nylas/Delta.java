package com.nylas;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory;

public class Delta<T extends AccountOwnedModel> extends RestfulModel {

	private String cursor;
	private String event;
	private String object;
	private T attributes;

	public String getCursor() {
		return cursor;
	}

	public String getEvent() {
		return event;
	}

	public String getObject() {
		return object;
	}

	public T getAttributes() {
		return attributes;
	}

	@Override
	public String toString() {
		return "Delta [" +
				"cursor='" + cursor + '\'' +
				", event='" + event + '\'' +
				", object='" + object + '\'' +
				", attributes=" + attributes +
				']';
	}

}
