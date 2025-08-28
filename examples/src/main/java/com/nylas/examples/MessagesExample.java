package com.nylas.examples;

import com.nylas.NylasClient;
import com.nylas.models.EmailName;
import com.nylas.models.FindMessageQueryParams;
import com.nylas.models.ListMessagesQueryParams;
import com.nylas.models.ListResponse;
import com.nylas.models.Message;
import com.nylas.models.MessageFields;
import com.nylas.models.NylasApiError;
import com.nylas.models.NylasSdkTimeoutError;
import com.nylas.models.Response;
import com.nylas.models.SendMessageRequest;
import com.nylas.models.TrackingOptions;
import okhttp3.OkHttpClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Example demonstrating the new message features in Nylas Java SDK:
 * - MessageFields.INCLUDE_TRACKING_OPTIONS and MessageFields.RAW_MIME
 * - tracking_options property in Message model
 * - raw_mime property in Message model  
 * - FindMessageQueryParams with fields parameter
 */
public class MessagesExample {
    public static void main(String[] args) {
        try {
            // Load configuration from environment variables or .env file
            Map<String, String> config = loadConfig();
            
            // Initialize the Nylas client with your API key
            NylasClient nylas = new NylasClient(
                config.get("NYLAS_API_KEY"),
                new OkHttpClient.Builder(),
                config.getOrDefault("NYLAS_API_URI", "https://api.us.nylas.com")
            );

            // Run the messages example workflow
            runMessagesExample(nylas, config);

            // Exit successfully
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
    
    /**
     * Loads configuration from environment variables and .env file
     * @throws IllegalArgumentException if required configuration is missing
     */
    private static Map<String, String> loadConfig() {
        Map<String, String> config = new HashMap<>();
        
        // Try loading from environment variables first
        System.getenv().entrySet().stream()
            .filter(entry -> entry.getKey().startsWith("NYLAS_"))
            .forEach(entry -> config.put(entry.getKey(), entry.getValue()));
        
        // Then try loading from .env file if needed
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
                                String key = parts[0].trim();
                                String value = parts[1].trim();
                                if (!config.containsKey(key)) {
                                    config.put(key, value);
                                }
                            }
                        });
                } catch (IOException e) {
                    System.out.println("Warning: Failed to load .env file: " + e.getMessage());
                }
            }
        }
        
        // Validate required configuration
        List<String> requiredKeys = Arrays.asList("NYLAS_API_KEY", "NYLAS_GRANT_ID");
        List<String> missingKeys = requiredKeys.stream()
            .filter(key -> !config.containsKey(key))
            .collect(Collectors.toList());
        
        if (!missingKeys.isEmpty()) {
            throw new IllegalArgumentException(
                "Missing required configuration: " + String.join(", ", missingKeys) + "\n" +
                "Please set these in examples/.env or as environment variables."
            );
        }
        
        return config;
    }
    
    private static void runMessagesExample(NylasClient nylas, Map<String, String> config) throws NylasApiError, NylasSdkTimeoutError {
        String grantId = config.get("NYLAS_GRANT_ID");
        
        System.out.println("🔍 Demonstrating Nylas Messages API with new features...\n");
        
        // 1. List messages with standard fields (default behavior)
        demonstrateStandardMessageListing(nylas, grantId);
        
        // 2. List messages with include_headers field
        demonstrateIncludeHeadersListing(nylas, grantId);
        
        // 3. List messages with include_tracking_options field (new feature)
        demonstrateTrackingOptionsListing(nylas, grantId);
        
        // 4. List messages with raw_mime field (new feature)
        demonstrateRawMimeListing(nylas, grantId);
        
        // 5. Find a specific message with different field options
        demonstrateMessageFinding(nylas, grantId);
        
        // 6. Demonstrate is_plaintext feature for sending messages
        demonstratePlaintextFeature(nylas, grantId, config);
    }
    
    private static void demonstrateStandardMessageListing(NylasClient nylas, String grantId) throws NylasApiError, NylasSdkTimeoutError {
        System.out.println("📧 1. Listing messages with standard fields:");
        
        ListMessagesQueryParams queryParams = new ListMessagesQueryParams.Builder()
            .limit(5)
            .fields(MessageFields.STANDARD)
            .build();
            
        ListResponse<Message> messages = nylas.messages().list(grantId, queryParams);
        
        System.out.println("   Found " + messages.getData().size() + " messages");
        for (Message message : messages.getData()) {
            System.out.println("   - ID: " + message.getId());
            System.out.println("     Subject: " + (message.getSubject() != null ? message.getSubject() : "No subject"));
            System.out.println("     Headers: " + (message.getHeaders() != null ? "Present" : "Not included"));
            System.out.println("     Tracking Options: " + (message.getTrackingOptions() != null ? "Present" : "Not included"));
            System.out.println("     Raw MIME: " + (message.getRawMime() != null ? "Present" : "Not included"));
            System.out.println();
        }
    }
    
    private static void demonstrateIncludeHeadersListing(NylasClient nylas, String grantId) throws NylasApiError, NylasSdkTimeoutError {
        System.out.println("📧 2. Listing messages with headers included:");
        
        ListMessagesQueryParams queryParams = new ListMessagesQueryParams.Builder()
            .limit(3)
            .fields(MessageFields.INCLUDE_HEADERS)
            .build();
            
        ListResponse<Message> messages = nylas.messages().list(grantId, queryParams);
        
        System.out.println("   Found " + messages.getData().size() + " messages");
        for (Message message : messages.getData()) {
            System.out.println("   - ID: " + message.getId());
            System.out.println("     Subject: " + (message.getSubject() != null ? message.getSubject() : "No subject"));
            System.out.println("     Headers: " + (message.getHeaders() != null ? message.getHeaders().size() + " headers" : "Not included"));
            System.out.println("     Tracking Options: " + (message.getTrackingOptions() != null ? "Present" : "Not included"));
            System.out.println("     Raw MIME: " + (message.getRawMime() != null ? "Present" : "Not included"));
            System.out.println();
        }
    }
    
    private static void demonstrateTrackingOptionsListing(NylasClient nylas, String grantId) throws NylasApiError, NylasSdkTimeoutError {
        System.out.println("📊 3. Listing messages with tracking options included (NEW FEATURE):");
        
        ListMessagesQueryParams queryParams = new ListMessagesQueryParams.Builder()
            .limit(3)
            .fields(MessageFields.INCLUDE_TRACKING_OPTIONS)
            .build();
            
        ListResponse<Message> messages = nylas.messages().list(grantId, queryParams);
        
        System.out.println("   Found " + messages.getData().size() + " messages");
        for (Message message : messages.getData()) {
            System.out.println("   - ID: " + message.getId());
            System.out.println("     Subject: " + (message.getSubject() != null ? message.getSubject() : "No subject"));
            System.out.println("     Headers: " + (message.getHeaders() != null ? "Present" : "Not included"));
            System.out.println("     Raw MIME: " + (message.getRawMime() != null ? "Present" : "Not included"));
            
            if (message.getTrackingOptions() != null) {
                TrackingOptions tracking = message.getTrackingOptions();
                System.out.println("     ✅ Tracking Options:");
                System.out.println("       - Opens: " + tracking.getOpens());
                System.out.println("       - Thread Replies: " + tracking.getThreadReplies());
                System.out.println("       - Links: " + tracking.getLinks());
                System.out.println("       - Label: " + tracking.getLabel());
            } else {
                System.out.println("     Tracking Options: Not available");
            }
            System.out.println();
        }
    }
    
    private static void demonstrateRawMimeListing(NylasClient nylas, String grantId) throws NylasApiError, NylasSdkTimeoutError {
        System.out.println("📄 4. Listing messages with raw MIME included (NEW FEATURE):");
        
        ListMessagesQueryParams queryParams = new ListMessagesQueryParams.Builder()
            .limit(2)
            .fields(MessageFields.RAW_MIME)
            .build();
            
        ListResponse<Message> messages = nylas.messages().list(grantId, queryParams);
        
        System.out.println("   Found " + messages.getData().size() + " messages");
        for (Message message : messages.getData()) {
            System.out.println("   - ID: " + message.getId());
            System.out.println("     Subject: " + (message.getSubject() != null ? message.getSubject() : "No subject"));
            System.out.println("     Headers: " + (message.getHeaders() != null ? "Present" : "Not included"));
            System.out.println("     Tracking Options: " + (message.getTrackingOptions() != null ? "Present" : "Not included"));
            
            if (message.getRawMime() != null) {
                String rawMime = message.getRawMime();
                System.out.println("     ✅ Raw MIME: Present (" + rawMime.length() + " characters)");
                // Show first 100 characters as preview
                if (rawMime.length() > 100) {
                    System.out.println("       Preview: " + rawMime.substring(0, 100) + "...");
                } else {
                    System.out.println("       Content: " + rawMime);
                }
            } else {
                System.out.println("     Raw MIME: Not available");
            }
            System.out.println();
        }
    }
    
    private static void demonstrateMessageFinding(NylasClient nylas, String grantId) throws NylasApiError, NylasSdkTimeoutError {
        System.out.println("🔍 5. Finding specific messages with query parameters (NEW FEATURE):");
        
        // First get a message ID to work with
        ListMessagesQueryParams listParams = new ListMessagesQueryParams.Builder()
            .limit(1)
            .build();
        ListResponse<Message> messages = nylas.messages().list(grantId, listParams);
        
        if (messages.getData().isEmpty()) {
            System.out.println("   No messages found to demonstrate with.");
            return;
        }
        
        String messageId = messages.getData().get(0).getId();
        System.out.println("   Using message ID: " + messageId);
        
        // Find message with standard fields
        System.out.println("\n   📧 Finding with standard fields:");
        Response<Message> standardMessage = nylas.messages().find(grantId, messageId);
        printMessageDetails(standardMessage.getData(), "standard");
        
        // Find message with tracking options using new FindMessageQueryParams
        System.out.println("\n   📊 Finding with tracking options (using new FindMessageQueryParams):");
        FindMessageQueryParams trackingParams = new FindMessageQueryParams.Builder()
            .fields(MessageFields.INCLUDE_TRACKING_OPTIONS)
            .build();
        Response<Message> trackingMessage = nylas.messages().find(grantId, messageId, trackingParams);
        printMessageDetails(trackingMessage.getData(), "tracking options");
        
        // Find message with raw MIME using new FindMessageQueryParams
        System.out.println("\n   📄 Finding with raw MIME (using new FindMessageQueryParams):");
        FindMessageQueryParams rawMimeParams = new FindMessageQueryParams.Builder()
            .fields(MessageFields.RAW_MIME)
            .build();
        Response<Message> rawMimeMessage = nylas.messages().find(grantId, messageId, rawMimeParams);
        printMessageDetails(rawMimeMessage.getData(), "raw MIME");
    }
    
    private static void printMessageDetails(Message message, String requestType) {
        System.out.println("     Request type: " + requestType);
        System.out.println("     ID: " + message.getId());
        System.out.println("     Subject: " + (message.getSubject() != null ? message.getSubject() : "No subject"));
        System.out.println("     Headers included: " + (message.getHeaders() != null));
        System.out.println("     Tracking options included: " + (message.getTrackingOptions() != null));
        System.out.println("     Raw MIME included: " + (message.getRawMime() != null));
        
        if (message.getTrackingOptions() != null) {
            TrackingOptions tracking = message.getTrackingOptions();
            System.out.println("     Tracking details: opens=" + tracking.getOpens() + 
                              ", replies=" + tracking.getThreadReplies() + 
                              ", links=" + tracking.getLinks() + 
                              ", label=" + tracking.getLabel());
        }
        
        if (message.getRawMime() != null) {
            System.out.println("     Raw MIME length: " + message.getRawMime().length() + " characters");
        }
    }
    
    private static void demonstratePlaintextFeature(NylasClient nylas, String grantId, Map<String, String> config) throws NylasApiError, NylasSdkTimeoutError {
        System.out.println("📝 6. Demonstrating is_plaintext feature for sending messages (NEW FEATURE):");
        
        String recipientEmail = config.get("NYLAS_RECIPIENT_EMAIL");
        if (recipientEmail == null) {
            System.out.println("   ⚠️  Skipping send examples - NYLAS_RECIPIENT_EMAIL not configured");
            System.out.println("   To enable this demo, set NYLAS_RECIPIENT_EMAIL in your .env file");
            return;
        }
        
        System.out.println("   Sending test messages to: " + recipientEmail);
        
        // 1. Send HTML message (default behavior)
        System.out.println("\n   📧 Sending HTML message (is_plaintext = false or not specified):");
        
        SendMessageRequest htmlRequest = new SendMessageRequest.Builder(
            Arrays.asList(new EmailName(recipientEmail, "Test Recipient"))
        )
            .subject("HTML Message Test - Nylas SDK")
            .body("<html><body><h1>Hello from Nylas!</h1><p>This is an <b>HTML</b> message with <i>formatting</i>.</p></body></html>")
            .isPlaintext(false) // Explicitly set to false (this is also the default)
            .build();
        
        try {
            Response<Message> htmlResponse = nylas.messages().send(grantId, htmlRequest);
            System.out.println("     ✅ HTML message sent successfully");
            System.out.println("     Message ID: " + htmlResponse.getData().getId());
        } catch (Exception e) {
            System.out.println("     ❌ Failed to send HTML message: " + e.getMessage());
        }
        
        // 2. Send plain text message using is_plaintext feature
        System.out.println("\n   📄 Sending plain text message (is_plaintext = true):");
        
        SendMessageRequest plaintextRequest = new SendMessageRequest.Builder(
            Arrays.asList(new EmailName(recipientEmail, "Test Recipient"))
        )
            .subject("Plain Text Message Test - Nylas SDK")
            .body("Hello from Nylas!\n\nThis is a PLAIN TEXT message.\nNo HTML formatting will be applied.\n\nBest regards,\nNylas SDK")
            .isPlaintext(true) // NEW FEATURE: Force plain text mode
            .build();
        
        try {
            Response<Message> plaintextResponse = nylas.messages().send(grantId, plaintextRequest);
            System.out.println("     ✅ Plain text message sent successfully");
            System.out.println("     Message ID: " + plaintextResponse.getData().getId());
        } catch (Exception e) {
            System.out.println("     ❌ Failed to send plain text message: " + e.getMessage());
        }
        
        System.out.println("\n   💡 Key differences:");
        System.out.println("     - HTML message (is_plaintext=false): Message body is sent as HTML with MIME formatting");
        System.out.println("     - Plain text message (is_plaintext=true): Message body is sent as plain text, no HTML in MIME data");
        System.out.println("     - Default behavior: is_plaintext=false (HTML formatting)");
    }
} 