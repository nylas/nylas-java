package com.nylas.examples;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.nylas.Notification;

public class WebhookServer {

	public static void main(String[] args) throws Exception {
		Properties props = Examples.loadExampleProperties();
		String clientSecret = props.getProperty("nylas.client.secret");
		Server server = new Server(8080);
		server.setHandler(new LoggingHandler(clientSecret));
		server.start();
	}
	
	public static class LoggingHandler extends AbstractHandler {

		private static final Logger log = LogManager.getLogger("HTTP");
		private String clientSecret;
		
		public LoggingHandler(String clientSecret) {
			this.clientSecret = clientSecret;
		}

		@Override
		public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException {
			
			String queryString = request.getQueryString();
			log.info(request.getMethod() + " " + request.getRequestURL()
				+ (queryString == null ? "" : "?" + queryString));
			Enumeration<String> headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String headerName = headerNames.nextElement();
				log.info(headerName + ": " + request.getHeader(headerName));
			}
			String body = CharStreams.toString(new InputStreamReader(
					request.getInputStream(), Charsets.UTF_8));
			log.info(body);
			log.info("---");
			
			// Handle verification challenge
			if (HttpMethod.GET.is(request.getMethod())) {
				String challenge = request.getParameter("challenge");
				if (challenge != null) {
					response.setContentType("text/plain;charset=utf-8");
					response.setStatus(HttpServletResponse.SC_OK);
					baseRequest.setHandled(true);
					response.getWriter().print(challenge);
					return;
				}
			}
			
			String signature = request.getHeader("X-Nylas-Signature");
			if (signature == null) {
				log.info("Callback missing X-Nylas-Signature header.  Ignoring.");
			} else if (!Notification.isSignatureValid(body, clientSecret, signature)) {
				log.info("Invalid signature.  Ignoring.");
			} else {
				Notification notification = Notification.parseNotification(body);
				log.info("Received callback with valid signature: " + notification);
			}
			
			// ack callbacks
			response.setContentLength(0);
			response.setStatus(HttpServletResponse.SC_OK);
			baseRequest.setHandled(true);
		}

	}
	
}
