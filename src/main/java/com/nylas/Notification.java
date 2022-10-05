package com.nylas;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.nylas.JobStatus.Action;
import com.squareup.moshi.JsonReader;

public class Notification {

	public static boolean isSignatureValid(String jsonNotification, String clientSecret, String expectedSignature) {
		try {
			byte[] expectedBytes = hexStringToByteArray(expectedSignature);
			Mac mac = Mac.getInstance("HmacSHA256");
			SecretKeySpec secretKeySpec
				= new SecretKeySpec(clientSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
			mac.init(secretKeySpec);
			byte[] sigBytes = mac.doFinal(jsonNotification.getBytes(StandardCharsets.UTF_8));
			return Arrays.equals(expectedBytes, sigBytes);
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);  // should never happen
		}
	}
	
	private static final JsonAdapter<Notification> JSON_ADAPTER =  JsonHelper.moshi().adapter(Notification.class);
	public static Notification parseNotification(String jsonNotification) {
		return JsonHelper.fromJsonUnchecked(JSON_ADAPTER, jsonNotification);
	}
	
	private static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
								+ Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}
	
	private List<Delta> deltas;
	
	public List<Delta> getDeltas() {
		return deltas;
	}

	@Override
	public String toString() {
		return "Notification [deltas=" + deltas + "]";
	}

	public static class Delta {
		private Long date;
		private String type;
		private String object;
		private ObjectData object_data;
		
		/**
		 * The timestamp of the notification
		 */
		public Instant getDate() {
			return Instants.toNullableInstant(date);
		}
		
		/**
		 * The trigger for the notification
		 */
		public String getTrigger() {
			return type;
		}
		
		/**
		 * The type of the object that triggered this notification
		 */
		public String getObjectType() {
			return object;
		}
		
		/**
		 * Data about the object that triggered this notification
		 */
		public ObjectData getObjectData() {
			return object_data;
		}

		@Override
		public String toString() {
			return "Delta [date=" + getDate() + ", trigger=" + type + ", objectType=" + object + ", objectData="
					+ object_data + "]";
		}
	}
	
	public static class ObjectData {
		private String object;
		private String account_id;
		private String id;
		private MessageTrackingData metadata;
		private transient Attributes attributes;
		
		/**
		 * The type of the object that triggered this notification
		 */
		public String getObjectType() {
			return object;
		}
		
		/**
		 * The Nylas ID for the account that triggered this notification
		 */
		public String getAccountId() {
			return account_id;
		}
		
		/**
		 * The Nylas ID of the object that triggered this notification
		 */
		public String getId() {
			return id;
		}
		
		/**
		 * Extra information about the object that triggered the notification.
		 * Currently only available for the message.created trigger
		 */
		public Attributes getAttributes() {
			return attributes;
		}
		
		/**
		 * Message tracking specific data.
		 * Only available for message tracking triggers:
		 * message.opened, thread.replied, and message.link_clicked
		 */
		public MessageTrackingData getMessageTrackingData() {
			return metadata;
		}

		@Override
		public String toString() {
			return "ObjectData [objectType=" + object + ", accountId=" + account_id
					+ ", id=" + id + ", attributes=" + attributes + ", messageTrackingData=" + metadata + "]";
		}
		
	}

	/**
	 * Attributes for a message notification
	 * @deprecated Attributes will become an interface, this class will become "MessageNotificationAttributes"
	 */
	public static class Attributes {
		private String received_date;
		private String thread_id;
		
		/**
		 * A timestamp indicating when the message was originally received.
		 */
		public String getReceivedDate() {
			return received_date;
		}
		
		/**
		 * The Nylas ID for the thread the created message belongs to
		 */
		public String getThreadId() {
			return thread_id;
		}

		@Override
		public String toString() {
			return "Attributes [receivedDate=" + received_date + ", threadId=" + thread_id + "]";
		}
	}

	/**
	 * Attributes for event notifications
	 */
	public static class EventNotificationAttributes extends Attributes {
		private String calendar_id;
		private Boolean created_before_account_connection;

		/**
		 * Calendar ID of the event
		 */
		public String getCalendarId() {
			return calendar_id;
		}

		/**
		 * Indicates if the event was created before the account was connected to Nylas
		 */
		public Boolean getCreatedBeforeAccountConnection() {
			return created_before_account_connection;
		}

		@Override
		public String toString() {
			return "EventNotificationAttributes [calendarId=" + calendar_id + ", createdBeforeAccountConnection=" + created_before_account_connection + "]";
		}
	}

	/**
	 * Attributes for job status notifications
	 */
	public static class JobStatusNotificationAttributes extends Attributes {
		private String action;
		private String message_id;
		private String job_status_id;
		private Extras extras;

		/**
		 * Event that triggered the job status webhook
		 */
		public Action getAction() {
			try {
				return Action.valueOf(action.toUpperCase());
			} catch (IllegalArgumentException | NullPointerException e) {
				return null;
			}
		}

		/**
		 * Event that triggered the job status webhook, as a string
		 */
		public String getActionString() {
			return action;
		}

		/**
		 * ID of the message associated with the Job
		 */
		public String getMessageId() {
			return message_id;
		}

		/**
		 * ID of the job
		 */
		public String getJobStatusId() {
			return job_status_id;
		}

		/**
		 * If the job has a status of cancelled, delayed, or failed then extras will contain more information
		 */
		public Extras getExtras() {
			return extras;
		}

		@Override
		public String toString() {
			return "JobStatusNotificationAttributes [action=" + action + ", threadId=" + getThreadId() + ", messageId="
					+ message_id + ", jobStatusId=" + job_status_id + ", extras=" + extras + "]";
		}

		/**
		 * Extra information in the event that a job was cancelled, delayed or failed
		 */
		public static class Extras {
			private String reason;
			private Long send_at;
			private Long original_send_at;

			/**
			 * Reason for status
			 */
			public String getReason() {
				return reason;
			}

			/**
			 * Unix timestamp for when the message was sent
			 */
			public Instant getSendAt() {
				return Instants.toNullableInstant(send_at);
			}

			/**
			 * Unix timestamp assigned from sending a message
			 */
			public Instant getOriginalSendAt() {
				return Instants.toNullableInstant(original_send_at);
			}

			@Override
			public String toString() {
				return "Extras [reason=" + reason + ", sendAt=" + getSendAt() + ", originalSendAt=" + getOriginalSendAt() + "]";
			}
		}
	}

	/**
	 * This adapter identifies the type of object this notification is for,
	 * and deserializes it to the appropriate Attributes class.
	 */
	@SuppressWarnings("unchecked")
	static class WebhookDeltaAdapter {
		@FromJson
		Delta fromJson(JsonReader reader, JsonAdapter<Delta> delegate) throws IOException {
			Map<String, Object> json = JsonHelper.jsonToMap(reader);
			Delta delta = delegate.fromJson(JsonHelper.mapToJson(json));
			if(delta != null && delta.object_data != null) {
				Map<String, Object> objectDataJson = (Map<String, Object>) json.get("object_data");
				if(objectDataJson.get("attributes") != null) {
					Map<String, Object> attributesJson = (Map<String, Object>) objectDataJson.get("attributes");
					if(attributesJson != null) {
						if(delta.object != null) {
							Class<? extends Attributes> attributeClass;
							switch(delta.object) {
								case "event":
									attributeClass = EventNotificationAttributes.class;
									break;
								case "job_status":
									attributeClass = JobStatusNotificationAttributes.class;
									break;
								case "message":
								default:
									attributeClass = Attributes.class;
							}
							delta.object_data.attributes = JsonHelper
								.moshi()
								.adapter(attributeClass)
								.fromJson(JsonHelper.mapToJson(attributesJson));
						}
					}
				}
			}

			return delta;
		}
	}
	
	/*
	 * Perhaps this class could be better served by splitting into subclasses for specific
	 * notification types.  Not sure it's worth the time investment yet to get the Moshi JSON parsing done.
	 */
	public static class MessageTrackingData {
		private String message_id;
		private String sender_app_id;
		private String payload;
		
		// thread.replied specific fields
		private Long timestamp;
		private String thread_id;
		private Boolean from_self;
		private String reply_to_message_id;
		
		// message.opened specific fields
		private Integer count;
		
		// message.link_clicked specific fields
		private List<LinkClickCount> link_data;
		private List<LinkClick> recents;  // also in message.opened

		// event specific fields
		@Json(name = "event-type")
		private String eventType;
		private String message;
		
		/**
		 * Nylas message ID for the tracked message
		 * 
		 * Available for all tracking notifications
		 */
		public String getMessageId() {
			return message_id;
		}
		
		/**
		 * The sender's Nylas application ID.
		 * 
		 * Available for all tracking notifications
		 */
		public String getSenderAppId() {
			return sender_app_id;
		}
		
		/**
		 * The tracking payload string set when the tracked message was sent (optional).
		 * 
		 * Available for all tracking notifications
		 */
		public String getPayload() {
			return payload;
		}
		
		/**
		 * The timestamp of the reply message.
		 * 
		 * Available for thread.replied notifications only
		 */
		public Instant getTimestamp() {
			return Instants.toNullableInstant(timestamp);
		}
		
		/**
		 * The Nylas ID of the replied thread.
		 * 
		 * Available for thread.replied notifications only
		 */
		public String getThreadId() {
			return thread_id;
		}
		
		/**
		 * Whether the reply came from the account that enabled this tracking.
		 * 
		 * Available for thread.replied notifications only
		 */
		public Boolean getFromSelf() {
			return from_self;
		}
		
		/**
		 * The Nylas ID for the message to which the new message is replying.
		 * 
		 * Available for thread.replied notifications only
		 */
		public String getReplyToMessageId() {
			return reply_to_message_id;
		}
		
		/**
		 * The number of times the message has been opened.
		 * 
		 * Available for message.opened notifications only
		 */		
		public Integer getOpenedCount() {
			return count;
		}
		
		/**
		 * A list of links with each url and how many times it was clicked.
		 * 
		 * Available for message.link_clicked notifications only
		 */		
		public List<LinkClickCount> getLinkClickCounts() {
			return link_data;
		}
		
		/**
		 * A list of recent link clicks
		 * 
		 * Available for message.link_clicked and message.opened notifications only
		 */		
		public List<LinkClick> getRecentClicks() {
			return recents;
		}

		/**
		 * The custom event type set for the Event
		 *
		 * Available for event notifications only
		 */
		public String getEventType() {
			return eventType;
		}

		/**
		 * The custom message set for the Event
		 *
		 * Available for event notifications only
		 */
		public String getMessage() {
			return message;
		}

		@Override
		public String toString() {
			return "MessageTrackingData [messageId=" + message_id + ", senderAppId=" + sender_app_id + ", payload="
					+ payload + ", timestamp=" + getTimestamp() + ", threadId=" + thread_id + ", fromSelf=" + from_self
					+ ", replyToMessageId=" + reply_to_message_id + ", openedCount=" + count + ", linkClickCounts="
					+ link_data + ", recentClicks=" + recents + ", eventType=" + eventType + ", message=" + message + "]";
		}
	}
	
	public static class LinkClick {
		private int id;
		private String ip;
		private String user_agent;
		private int link_index;
		
		/**
		 * An ID, unique to each message, that increments with each click.
		 */
		public int getId() {
			return id;
		}
		
		/**
		 * The IP address of the user who clicked the link.
		 */
		public String getIp() {
			return ip;
		}
		
		/**
		 * The user agent for the user who clicked the link.
		 */
		public String getUserAgent() {
			return user_agent;
		}
		
		/**
		 * Returns the index of the corresponding link in the link click counts list
		 */
		public int getLinkClickCountIndex() {
			return link_index;
		}

		@Override
		public String toString() {
			return "LinkClick [id=" + id + ", ip=" + ip + ", userAgent=" + user_agent + ", linkClickCountIndex="
					+ link_index + "]";
		}
	}
	
	public static class LinkClickCount {
		private String url;
		private int count;
		
		/**
		 * The URL that was clicked
		 */
		public String getUrl() {
			return url;
		}
		
		/**
		 * Number of times the link was clicked
		 */
		public int getClickCount() {
			return count;
		}

		@Override
		public String toString() {
			return "LinkClickCount [url=" + getUrl() + ", clickCount=" + getClickCount() + "]";
		}
	}
	
}
