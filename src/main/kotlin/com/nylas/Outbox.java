package com.nylas;

import com.squareup.moshi.JsonAdapter;
import okhttp3.HttpUrl;

import com.nylas.NylasClient.AuthMethod;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class Outbox {

	private final NylasClient client;
	private final String accessToken;

	public Outbox(NylasClient client, String accessToken) {
		this.client = client;
		this.accessToken = accessToken;
	}

	/**
	 * Send a message via Outbox
	 * <br> {@code retryLimitDatetime} is set to null.
	 * @see Outbox#send(Draft, Long, Long) 
	 */
	public OutboxJobStatus send(Draft draft, Long sendAt) throws RequestFailedException, IOException {
		return send(draft, sendAt, null);
	}

	/**
	 * Send a message via Outbox
	 * @param draft The draft to send
	 * @param sendAt The date and time to send the message. If set to 0, Outbox will send this message immediately.
	 * @param retryLimitDatetime Optional date and time to stop retry attempts for a message.
	 * @see Outbox#send(OutboxMessage)
	 */
	public OutboxJobStatus send(Draft draft, Long sendAt, Long retryLimitDatetime)
			throws RequestFailedException, IOException {
		OutboxMessage message = new OutboxMessage(draft);
		message.setSendAt(sendAt);
		message.setRetryLimitDatetime(retryLimitDatetime);
		return send(message);
	}

	/**
	 * Send a message via Outbox
	 * @param message The message to send
	 * @return A {@link OutboxJobStatus} object containing the status and the outbox message
	 * @throws IllegalArgumentException if the fields are not valid
	 * @see <a href="https://developer.nylas.com/docs/api#post/v2/outbox">Outbox - Send a Message</a>
	 */
	public OutboxJobStatus send(OutboxMessage message) throws RequestFailedException, IOException {
		message.validateFields();
		Map<String, Object> body = message.getWritableFields(true);
		return client.executePost(accessToken, outboxEndpoint(), body, OutboxJobStatus.class, AuthMethod.BEARER);
	}

	/**
	 * Update a scheduled Outbox message
	 * @param message The message object with updated values
	 * @param jobStatusId The ID of the outbox job status
	 * @return A {@link OutboxJobStatus} object containing the status and the outbox message
	 * @throws IllegalArgumentException if the fields are not valid
	 * @see <a href="https://developer.nylas.com/docs/api#patch/v2/outbox/job_status_id">Update Outbox Message</a>
	 */
	public OutboxJobStatus update(OutboxMessage message, String jobStatusId)
			throws RequestFailedException, IOException {
		message.validateFields();
		HttpUrl.Builder url = outboxEndpoint().addPathSegment(jobStatusId);
		Map<String, Object> body = message.getWritableFields(true);
		return client.executePatch(accessToken, url, body, OutboxJobStatus.class, AuthMethod.BEARER);
	}

	/**
	 * Delete a scheduled Outbox message
	 * @param jobStatusId The ID of the job status
	 * @see <a href="https://developer.nylas.com/docs/api#delete/v2/outbox/job_status_id">Delete Scheduled Message</a>
	 */
	public void delete(String jobStatusId) throws RequestFailedException, IOException {
		HttpUrl.Builder url = outboxEndpoint().addPathSegment(jobStatusId);
		client.executeDelete(accessToken, url, null, AuthMethod.BEARER);
	}

	/**
	 * SendGrid - Check Authentication and Verification Status
	 * @see <a href="https://developer.nylas.com/docs/api#get/v2/outbox/onboard/verified_status">Check Authentication and Verification Status</a>
	 */
	@SuppressWarnings("unchecked")
	public SendGridVerifiedStatus sendGridVerificationStatus() throws RequestFailedException, IOException {
		HttpUrl.Builder url = outboxEndpoint().addPathSegment("onboard").addPathSegment("verified_status");
		Map<String, Object> response = client.executeGet(accessToken, url, Map.class, AuthMethod.BEARER);
		if(response.get("results") == null) {
			throw new RuntimeException("Verification status not present in response");
		}

		String resultJson = JsonHelper.mapToJson((Map<String, Object>) response.get("results"));
		JsonAdapter<SendGridVerifiedStatus> adapter = JsonHelper.moshi().adapter(SendGridVerifiedStatus.class);
		return adapter.fromJson(resultJson);
	}

	/**
	 * SendGrid -  Delete SendGrid Subuser and UAS Grant
	 * @param email Email address for SendGrid subuser to delete
	 * @see <a href="https://developer.nylas.com/docs/api#delete/v2/outbox/onboard/subuser">Delete SendGrid Subuser and UAS Grant</a>
	 */
	public boolean deleteSendGridSubUser(String email) throws RequestFailedException, IOException {
		HttpUrl.Builder url = outboxEndpoint().addPathSegment("onboard").addPathSegment("subuser");
		Map<String, String> body = Collections.singletonMap("email", email);
		client.executeDelete(accessToken, url, null, AuthMethod.BEARER);
		return true;
	}

	private HttpUrl.Builder outboxEndpoint() {
		return client.newUrlBuilder().addPathSegment("v2").addPathSegment("outbox");
	}
}
