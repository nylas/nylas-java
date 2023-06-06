package com.nylas.delta;

import com.nylas.*;
import com.nylas.Thread;
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

	// Adapter for identifying the class of object in the delta to serialize to
	public static final JsonAdapter.Factory ACCOUNT_OWNED_MODEL_JSON_FACTORY
			= PolymorphicJsonAdapterFactory.of(AccountOwnedModel.class, "object")
			.withSubtype(Account.class, "account")
			.withSubtype(Calendar.class, "calendar")
			.withSubtype(RoomResource.class, "room_resource")
			.withSubtype(Contact.class, "contact")
			.withSubtype(File.class, "file")
			.withSubtype(Message.class, "message")
			.withSubtype(Draft.class, "draft")
			.withSubtype(Thread.class, "thread")
			.withSubtype(Event.class, "event")
			.withSubtype(Folder.class, "folder")
			.withSubtype(Label.class, "label");
}
