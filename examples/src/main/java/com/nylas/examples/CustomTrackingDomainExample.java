package com.nylas.examples;

import com.nylas.NylasClient;
import com.nylas.models.CreateDraftRequest;
import com.nylas.models.Draft;
import com.nylas.models.EmailName;
import com.nylas.models.Message;
import com.nylas.models.NylasApiError;
import com.nylas.models.NylasSdkTimeoutError;
import com.nylas.models.Response;
import com.nylas.models.SendMessageRequest;
import com.nylas.models.SendTransactionalEmailRequest;
import com.nylas.models.TrackingOptions;
import okhttp3.OkHttpClient;

import java.util.Collections;

/**
 * Demonstrates custom tracking hostnames for regular, draft, scheduled, and Transactional Send requests.
 */
public class CustomTrackingDomainExample {
    private static final String LINK_BODY = "<a href=\"https://example.com\">Open example</a>";

    public static void main(String[] args) throws NylasApiError, NylasSdkTimeoutError {
        String operation = System.getenv().getOrDefault("NYLAS_CUSTOM_TRACKING_OPERATION", "regular");
        String apiKey = requireEnvironmentVariable("NYLAS_API_KEY");
        String recipientEmail = requireEnvironmentVariable("RECIPIENT_EMAIL");
        String trackingHostname = requireEnvironmentVariable("NYLAS_TRACKING_HOSTNAME");
        String apiUri = System.getenv().getOrDefault("NYLAS_API_URI", "https://api.us.nylas.com");

        NylasClient nylas = new NylasClient(apiKey, new OkHttpClient.Builder(), apiUri);

        switch (operation) {
            case "regular":
                sendRegularMessage(
                        nylas,
                        requireEnvironmentVariable("NYLAS_GRANT_ID"),
                        recipientEmail,
                        trackingHostname);
                break;
            case "draft":
                createTrackedDraft(
                        nylas,
                        requireEnvironmentVariable("NYLAS_GRANT_ID"),
                        recipientEmail,
                        trackingHostname);
                break;
            case "scheduled":
                scheduleTrackedMessage(
                        nylas,
                        requireEnvironmentVariable("NYLAS_GRANT_ID"),
                        recipientEmail,
                        trackingHostname,
                        requireSendAt());
                break;
            case "transactional":
                sendTransactionalMessage(
                        nylas,
                        requireEnvironmentVariable("NYLAS_TRANSACTIONAL_SENDER_DOMAIN"),
                        requireEnvironmentVariable("SENDER_EMAIL"),
                        recipientEmail,
                        trackingHostname);
                break;
            default:
                throw new IllegalArgumentException(
                        "NYLAS_CUSTOM_TRACKING_OPERATION must be regular, draft, scheduled, or transactional");
        }
    }

    private static TrackingOptions buildTrackingOptions(String trackingHostname) {
        return new TrackingOptions.Builder()
                .links(true)
                .opens(true)
                .domainName(trackingHostname)
                .build();
    }

    private static void sendRegularMessage(
            NylasClient nylas, String grantId, String recipientEmail, String trackingHostname)
            throws NylasApiError, NylasSdkTimeoutError {
        SendMessageRequest request = new SendMessageRequest.Builder(
                Collections.singletonList(new EmailName(recipientEmail, "Recipient")))
                .subject("Tracked update")
                .body(LINK_BODY)
                .trackingOptions(buildTrackingOptions(trackingHostname))
                .build();

        Response<Message> response = nylas.messages().send(grantId, request);
        System.out.println("Sent message: " + response.getData().getId());
    }

    private static void createTrackedDraft(
            NylasClient nylas, String grantId, String recipientEmail, String trackingHostname)
            throws NylasApiError, NylasSdkTimeoutError {
        CreateDraftRequest request = new CreateDraftRequest.Builder()
                .to(Collections.singletonList(new EmailName(recipientEmail, "Recipient")))
                .subject("Tracked draft")
                .body(LINK_BODY)
                .trackingOptions(buildTrackingOptions(trackingHostname))
                .build();

        Response<Draft> response = nylas.drafts().create(grantId, request);
        System.out.println("Created draft: " + response.getData().getId());
    }

    private static void scheduleTrackedMessage(
            NylasClient nylas,
            String grantId,
            String recipientEmail,
            String trackingHostname,
            long sendAt)
            throws NylasApiError, NylasSdkTimeoutError {
        SendMessageRequest request = new SendMessageRequest.Builder(
                Collections.singletonList(new EmailName(recipientEmail, "Recipient")))
                .subject("Scheduled tracked update")
                .body(LINK_BODY)
                .sendAt(sendAt)
                .trackingOptions(buildTrackingOptions(trackingHostname))
                .build();

        Response<Message> response = nylas.messages().send(grantId, request);
        System.out.println("Scheduled message: " + response.getData().getScheduleId());
    }

    private static void sendTransactionalMessage(
            NylasClient nylas,
            String senderDomain,
            String senderEmail,
            String recipientEmail,
            String trackingHostname)
            throws NylasApiError, NylasSdkTimeoutError {
        SendTransactionalEmailRequest request = new SendTransactionalEmailRequest.Builder(
                Collections.singletonList(new EmailName(recipientEmail, "Recipient")),
                new EmailName(senderEmail, "Sender"))
                .subject("Transactional tracked update")
                .body(LINK_BODY)
                .trackingOptions(buildTrackingOptions(trackingHostname))
                .build();

        // The route value is the verified sender domain. The nested domainName is the tracking hostname.
        Response<Message> response = nylas.domains().sendTransactionalEmail(senderDomain, request);
        System.out.println("Sent transactional message: " + response.getData().getId());
    }

    private static String requireEnvironmentVariable(String name) {
        String value = System.getenv(name);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(name + " environment variable is required");
        }
        return value;
    }

    private static long requireSendAt() {
        String value = requireEnvironmentVariable("NYLAS_SEND_AT");
        try {
            long sendAt = Long.parseLong(value);
            if (sendAt <= 0) {
                throw new IllegalArgumentException("NYLAS_SEND_AT must be a positive Unix timestamp");
            }
            return sendAt;
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException("NYLAS_SEND_AT must be a Unix timestamp", error);
        }
    }
}
