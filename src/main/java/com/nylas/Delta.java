package com.nylas;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory;

public class Delta extends RestfulModel {

	private String cursor;
	private String object;
	private String event;
	private Attributes attributes;
	
	public String getCursor() {
		return cursor;
	}

	public String getObject() {
		return object;
	}

	public String getEvent() {
		return event;
	}

	public Attributes getAttributes() {
		return attributes;
	}

	@Override
	public String toString() {
		return "Delta [id=" + getId() + ", cursor=" + cursor + ", object=" + object + ", event=" + event
				+ ", attributes=" + attributes + "]";
	}

	public static JsonAdapter.Factory getAttributesJsonFactory() {
		return ATTRIBUTES_JSON_FACTORY;
	}

	public static interface Attributes extends JsonObject {}
	
	public static final JsonAdapter.Factory ATTRIBUTES_JSON_FACTORY
		= PolymorphicJsonAdapterFactory.of(Delta.Attributes.class, "object")
			.withSubtype(Contact.class, "contact")
			.withSubtype(Draft.class, "draft")
			.withSubtype(Event.class, "event")
			.withSubtype(File.class, "file")
			.withSubtype(Folder.class, "folder")
			.withSubtype(Label.class, "label")
			.withSubtype(Message.class, "message")
			.withSubtype(Thread.class, "thread")
			;


}
