package com.nylas.server_bindings;

import com.nylas.Notification;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface WebhookNotificationCallback {

	/**
	 * Callback invoked after a webhook notification was received
	 * @param request Incoming request data
	 * @param response Response object that can be written to
	 * @param webhookNotification The parsed notification
	 */
	void onNotification(HttpServletRequest request, HttpServletResponse response, Notification webhookNotification);

	/**
	 * Callback invoked after a webhook notification was received, but was not able to be verified from Nylas
	 * @param request Incoming request data
	 * @param response Response object that can be written to
	 */
	void onUnverifiedNotification(HttpServletRequest request, HttpServletResponse response);
}
