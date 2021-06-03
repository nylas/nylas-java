package com.nylas;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.squareup.moshi.JsonAdapter;

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
	
	public static class Attributes {
		private String received_date;
		private String thread_id;

		// message.bounced specific fields
		private String message_id;
		private String original_message_id;
		private String original_thread_id;
		private String bounce_reason;
		private String bounce_diagnostic;
		
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

		/**
		 * The message ID of the bounce reply message
		 */
		public String getMessageId() {
			return message_id;
		}

		/**
		 * The message ID of the email that was actually bounced
		 */
		public String getOriginalMessageId() {
			return original_message_id;
		}

		/**
		 * The thread ID that the bounced message belonged to
		 */
		public String getOriginalThreadId() {
			return original_thread_id;
		}

		/**
		 * The reason for the bounce parsed directly from the bounce reply
		 */
		public String getBounceReason() {
			return bounce_reason;
		}

		/**
		 * The detailed error message parsed directly from the bounce reply
		 */
		public String getBounceDiagnostic() {
			return bounce_diagnostic;
		}

		@Override
		public String toString() {
			return "Attributes [receivedDate=" + received_date + ", threadId=" + thread_id + ", messageId=" + message_id
					+ ", originalMessageId=" + original_message_id + ", originalThreadId=" + original_thread_id
					+ ", bounceReason=" + bounce_reason + ", bounceDiagnostic=" + bounce_diagnostic + "]";
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

		@Override
		public String toString() {
			return "MessageTrackingData [messageId=" + message_id + ", senderAppId=" + sender_app_id + ", payload="
					+ payload + ", timestamp=" + getTimestamp() + ", threadId=" + thread_id + ", fromSelf=" + from_self
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
