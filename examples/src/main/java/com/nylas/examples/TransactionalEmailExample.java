package com.nylas.examples;

import com.nylas.NylasClient;
import com.nylas.models.*;
import okhttp3.OkHttpClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Example demonstrating how to send transactional emails using the Nylas Java SDK.
 *
 * <p>Transactional send lets you send individual emails from a verified domain without
 * a connected end-user grant. Common use cases include:
 * <ul>
 *   <li>Receipts and order confirmations</li>
 *   <li>Password-reset and account-verification emails</li>
 *   <li>Notification and alert emails</li>
 * </ul>
 *
 * <p>Prerequisites:
 * <ul>
 *   <li>A Nylas application with at least one verified domain</li>
 *   <li>{@code NYLAS_API_KEY}       — your Nylas API key</li>
 *   <li>{@code NYLAS_DOMAIN}        — the verified domain to send from (e.g. "acme.com")</li>
 *   <li>{@code NYLAS_SENDER_EMAIL}  — a sender address on that domain (e.g. "noreply@acme.com")</li>
 *   <li>{@code NYLAS_RECIPIENT_EMAIL} — the address that will receive the test email</li>
 * </ul>
 */
public class TransactionalEmailExample {

    public static void main(String[] args) {
        try {
            Map<String, String> config = loadConfig();

            NylasClient nylas = new NylasClient(
                    config.get("NYLAS_API_KEY"),
                    new OkHttpClient.Builder(),
                    config.getOrDefault("NYLAS_API_URI", "https://api.us.nylas.com")
            );

            runTransactionalEmailExample(nylas, config);
            System.exit(0);

        } catch (NylasApiError e) {
            System.out.println("\n❌ Nylas API Error: " + e.getMessage());
            System.out.println("   Status code: " + e.getStatusCode());
            System.out.println("   Request ID: " + e.getRequestId());
            System.exit(1);
        } catch (IllegalArgumentException e) {
            System.out.println("\n❌ Configuration Error: " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.out.println("\n❌ Unexpected Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    // -------------------------------------------------------------------------
    // Config loader
    // -------------------------------------------------------------------------

    private static Map<String, String> loadConfig() {
        Map<String, String> config = new HashMap<>();

        System.getenv().entrySet().stream()
                .filter(entry -> entry.getKey().startsWith("NYLAS_"))
                .forEach(entry -> config.put(entry.getKey(), entry.getValue()));

        List<String> envPaths = Arrays.asList("examples/.env", ".env");
        for (String path : envPaths) {
            File envFile = new File(path);
            if (envFile.exists()) {
                System.out.println("📝 Loading configuration from " + envFile.getAbsolutePath());
                try {
                    Files.lines(envFile.toPath())
                            .filter(line -> !line.trim().isEmpty() && !line.startsWith("#"))
                            .forEach(line -> {
                                String[] parts = line.split("=", 2);
                                if (parts.length == 2) {
                                    config.putIfAbsent(parts[0].trim(), parts[1].trim());
                                }
                            });
                } catch (IOException e) {
                    System.out.println("Warning: Failed to load .env file: " + e.getMessage());
                }
            }
        }

        List<String> requiredKeys = Arrays.asList(
                "NYLAS_API_KEY", "NYLAS_DOMAIN", "NYLAS_SENDER_EMAIL", "NYLAS_RECIPIENT_EMAIL");
        List<String> missingKeys = requiredKeys.stream()
                .filter(key -> !config.containsKey(key))
                .collect(Collectors.toList());

        if (!missingKeys.isEmpty()) {
            throw new IllegalArgumentException(
                    "Missing required configuration: " + String.join(", ", missingKeys) + "\n" +
                    "Please set these in examples/.env or as environment variables.");
        }

        return config;
    }

    // -------------------------------------------------------------------------
    // Examples
    // -------------------------------------------------------------------------

    private static void runTransactionalEmailExample(NylasClient nylas, Map<String, String> config)
            throws NylasApiError, NylasSdkTimeoutError {
        String domainName = config.get("NYLAS_DOMAIN");
        String senderEmail = config.get("NYLAS_SENDER_EMAIL");
        String recipientEmail = config.get("NYLAS_RECIPIENT_EMAIL");

        System.out.println("📧 Nylas Transactional Email Examples\n");

        sendSimpleEmail(nylas, domainName, senderEmail, recipientEmail);
        sendPlaintextEmail(nylas, domainName, senderEmail, recipientEmail);
        sendEmailWithTracking(nylas, domainName, senderEmail, recipientEmail);
        sendScheduledEmail(nylas, domainName, senderEmail, recipientEmail);
        sendEmailWithExtras(nylas, domainName, senderEmail, recipientEmail);
    }

    /**
     * 1. Send a simple HTML email from a verified domain.
     */
    private static void sendSimpleEmail(
            NylasClient nylas, String domainName, String senderEmail, String recipientEmail)
            throws NylasApiError, NylasSdkTimeoutError {
        System.out.println("1️⃣  Sending a simple HTML email...");

        SendTransactionalEmailRequest request = new SendTransactionalEmailRequest.Builder(
                Collections.singletonList(new EmailName(recipientEmail, "Recipient")),
                new EmailName(senderEmail, "Sender")
        )
                .subject("Hello from Nylas Transactional Send!")
                .body("<html><body><h1>Hello!</h1>" +
                      "<p>This is a transactional email sent via the <b>Nylas Java SDK</b>.</p>" +
                      "</body></html>")
                .build();

        Response<Message> response = nylas.domains().sendTransactionalEmail(domainName, request);
        System.out.println("   ✅ Sent! Message ID: " + response.getData().getId() + "\n");
    }

    /**
     * 2. Send a plain-text-only email (no HTML MIME part).
     */
    private static void sendPlaintextEmail(
            NylasClient nylas, String domainName, String senderEmail, String recipientEmail)
            throws NylasApiError, NylasSdkTimeoutError {
        System.out.println("2️⃣  Sending a plain-text email...");

        SendTransactionalEmailRequest request = new SendTransactionalEmailRequest.Builder(
                Collections.singletonList(new EmailName(recipientEmail, "Recipient")),
                new EmailName(senderEmail, "Sender")
        )
                .subject("Plain-text transactional email")
                .body("Hello!\n\nThis message contains no HTML.\n\nRegards,\nNylas SDK")
                .isPlaintext(true)
                .build();

        Response<Message> response = nylas.domains().sendTransactionalEmail(domainName, request);
        System.out.println("   ✅ Sent! Message ID: " + response.getData().getId() + "\n");
    }

    /**
     * 3. Send an email with open/link tracking enabled.
     */
    private static void sendEmailWithTracking(
            NylasClient nylas, String domainName, String senderEmail, String recipientEmail)
            throws NylasApiError, NylasSdkTimeoutError {
        System.out.println("3️⃣  Sending an email with tracking options...");

        TrackingOptions tracking = new TrackingOptions("transactional-demo", true, true, true);

        SendTransactionalEmailRequest request = new SendTransactionalEmailRequest.Builder(
                Collections.singletonList(new EmailName(recipientEmail, "Recipient")),
                new EmailName(senderEmail, "Sender")
        )
                .subject("Tracked transactional email")
                .body("<p>This email has open and link tracking enabled.</p>")
                .trackingOptions(tracking)
                .build();

        Response<Message> response = nylas.domains().sendTransactionalEmail(domainName, request);
        System.out.println("   ✅ Sent! Message ID: " + response.getData().getId() + "\n");
    }

    /**
     * 4. Schedule an email to be delivered in the future.
     */
    private static void sendScheduledEmail(
            NylasClient nylas, String domainName, String senderEmail, String recipientEmail)
            throws NylasApiError, NylasSdkTimeoutError {
        System.out.println("4️⃣  Scheduling an email to send in 10 minutes...");

        // Unix timestamp 10 minutes from now
        long sendAt = (System.currentTimeMillis() / 1000) + 600;

        SendTransactionalEmailRequest request = new SendTransactionalEmailRequest.Builder(
                Collections.singletonList(new EmailName(recipientEmail, "Recipient")),
                new EmailName(senderEmail, "Sender")
        )
                .subject("Scheduled transactional email")
                .body("<p>This email was scheduled via <code>send_at</code>.</p>")
                .sendAt(sendAt)
                .build();

        Response<Message> response = nylas.domains().sendTransactionalEmail(domainName, request);
        System.out.println("   ✅ Scheduled! Message ID: " + response.getData().getId() + "\n");
    }

    /**
     * 5. Send an email with CC, BCC, reply-to, and custom headers.
     */
    private static void sendEmailWithExtras(
            NylasClient nylas, String domainName, String senderEmail, String recipientEmail)
            throws NylasApiError, NylasSdkTimeoutError {
        System.out.println("5️⃣  Sending an email with CC, BCC, reply-to, and custom headers...");

        String domainPart = senderEmail.contains("@") ? senderEmail.substring(senderEmail.indexOf('@') + 1) : domainName;

        SendTransactionalEmailRequest request = new SendTransactionalEmailRequest.Builder(
                Collections.singletonList(new EmailName(recipientEmail, "Primary Recipient")),
                new EmailName(senderEmail, "Sender")
        )
                .subject("Transactional email with extras")
                .body("<p>This email includes CC, BCC, reply-to, and custom headers.</p>")
                .cc(Collections.singletonList(new EmailName(recipientEmail, "CC Recipient")))
                .bcc(Collections.singletonList(new EmailName(recipientEmail, "BCC Recipient")))
                .replyTo(Collections.singletonList(new EmailName("support@" + domainPart, "Support")))
                .customHeaders(Collections.singletonList(new CustomHeader("X-Campaign-ID", "transactional-demo-001")))
                .build();

        Response<Message> response = nylas.domains().sendTransactionalEmail(domainName, request);
        System.out.println("   ✅ Sent! Message ID: " + response.getData().getId() + "\n");
    }
}
