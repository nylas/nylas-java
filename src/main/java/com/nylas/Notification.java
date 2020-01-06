package com.nylas;

import java.util.List;

import com.squareup.moshi.JsonAdapter;

public class Notification {

	private static final JsonAdapter<Notification> JSON_ADAPTER =  JsonHelper.moshi().adapter(Notification.class);
	public static Notification parseNotification(String jsonNotification) {
		return JsonHelper.fromJsonSafe(JSON_ADAPTER, jsonNotification);
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
		private String date;
		private String type;
		private String object;
		private ObjectData object_data;
		
		/**
		 * The timestamp of the notification
		 */
		public String getDate() {
			return date;
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
			return "Delta [date=" + date + ", trigger=" + type + ", objectType=" + object + ", objectData="
					+ object_data + "]";
		}
	}
	
	public static class ObjectData {
		private String object;
		private String account_id;
		private String namespace_id;
		private String id;
		private Attributes attributes;
		private MessageTrackingData metadata;
		
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
		
		public String getNamespaceId() {
			return namespace_id;
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
			return "ObjectData [objectType=" + object + ", accountId=" + account_id + ", namespaceId=" + namespace_id
					+ ", id=" + id + ", attributes=" + attributes + ", messageTrackingData=" + metadata + "]";
		}
		
	}
	
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
	
	/*
	 * Perhaps this class could be better served by splitting into subclasses for specific
	 * notification types.  Not sure it's worth the time investment yet to get the Moshi JSON parsing done.
	 */
	public static class MessageTrackingData {
		private String message_id;
		private String sender_app_id;
		private String payload;
		
		// thread.replied specific fields
		private String timestamp;
		private String thread_id;
		private Boolean from_self;
		private String reply_to_message_id;
		
		// message.opened specific fields
		private Integer count;
		
		// message.link_clicked specific fields
		private List<LinkClickCount> link_data;
		private List<LinkClick> recents;  // also in message.opened
		
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
		public String getTimestamp() {
			return timestamp;
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

		@Override
		public String toString() {
			return "MessageTrackingData [messageId=" + message_id + ", senderAppId=" + sender_app_id + ", payload="
					+ payload + ", timestamp=" + timestamp + ", threadId=" + thread_id + ", fromSelf=" + from_self
					+ ", replyToMessageId=" + reply_to_message_id + ", openedCount=" + count + ", linkClickCounts="
					+ link_data + ", recentClicks=" + recents + "]";
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
