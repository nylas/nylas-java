package com.nylas.examples;

import com.nylas.NylasClient;
import com.nylas.models.CreateNotetakerRequest;
import com.nylas.models.LeaveNotetakerResponse;
import com.nylas.models.ListResponse;
import com.nylas.models.Notetaker;
import com.nylas.models.NotetakerMediaResponse;
import com.nylas.models.Response;
import com.nylas.models.NylasApiError;
import com.nylas.models.NylasSdkTimeoutError;
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
 * This example demonstrates how to use the Nylas Java SDK to work with Notetakers.
 * A Notetaker is an AI assistant that can join your meetings to take notes, record,
 * and transcribe the meeting content.
 *
 * This example shows how to:
 * 1. Create and configure a Nylas client
 * 2. Invite a Notetaker to join a meeting
 * 3. List all your Notetakers
 * 4. Download media (recordings/transcripts) from a Notetaker
 * 5. Leave a Notetaker session
 *
 * Prerequisites:
 * 1. A Nylas API key (sign up at https://www.nylas.com if you don't have one)
 * 2. A meeting link (e.g., Google Meet, Zoom, etc.)
 *
 * To run this example:
 * 1. Copy examples/.env.example to examples/.env and configure your settings
 * 2. Run using Gradle:
 *    ./gradlew :examples:run -PmainClass=com.nylas.examples.NotetakerExample
 */
public class NotetakerExample {
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
            
            // Run the example workflow
            runNotetakerExample(nylas, config);
            
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
     * Runs through the complete Notetaker example workflow
     */
    private static void runNotetakerExample(NylasClient nylas, Map<String, String> config) throws NylasApiError, NylasSdkTimeoutError {
        System.out.println("\n🚀 Starting Nylas Notetaker Example");
        
        // Step 1: Invite a Notetaker to a meeting
        Response<Notetaker> notetaker = inviteNotetaker(nylas, config);
        System.out.println("\n✅ Successfully invited Notetaker");
        
        // Step 2: List all Notetakers
        listNotetakers(nylas);
        System.out.println("\n✅ Successfully listed all Notetakers");
        
        // Step 3: Get media from the Notetaker (if available)
        if (notetaker.getData().getState() == Notetaker.NotetakerState.MEDIA_AVAILABLE) {
            getNotetakerMedia(nylas, notetaker.getData().getId());
            System.out.println("\n✅ Successfully retrieved Notetaker media");
        } else {
            System.out.println("\nℹ️ No media available yet for this Notetaker");
        }
        
        // Step 4: Leave the Notetaker session
        leaveNotetaker(nylas, notetaker.getData().getId());
        System.out.println("\n✅ Successfully left Notetaker session");
    }
    
    /**
     * Loads configuration from environment variables and .env file
     * @throws IllegalArgumentException if required configuration is missing
     */
    private static Map<String, String> loadConfig() {
        Map<String, String> config = new HashMap<>();
        
        // Try loading from environment variables first
        System.getenv().entrySet().stream()
            .filter(entry -> entry.getKey().startsWith("NYLAS_") || entry.getKey().equals("MEETING_LINK"))
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
        List<String> requiredKeys = Arrays.asList("NYLAS_API_KEY", "MEETING_LINK");
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
    
    /**
     * Invites a Notetaker to join a meeting
     */
    private static Response<Notetaker> inviteNotetaker(NylasClient nylas, Map<String, String> config) throws NylasApiError, NylasSdkTimeoutError {
        System.out.println("\n📋 Inviting Notetaker to Meeting");
        
        String meetingLink = getOrThrow(config, "MEETING_LINK");
        
        // Configure the Notetaker's meeting settings
        CreateNotetakerRequest.MeetingSettings meetingSettings = new CreateNotetakerRequest.MeetingSettings(
            true,  // videoRecording
            true,  // audioRecording
            true   // transcription
        );
        
        // Create and configure the Notetaker
        CreateNotetakerRequest request = new CreateNotetakerRequest(
            meetingLink,
            null,  // joinTime
            "Nylas Notetaker Example",
            meetingSettings
        );
        
        // Send the invitation
        Response<Notetaker> notetaker = nylas.notetakers().create(request);
        
        System.out.println("Notetaker Details:");
        System.out.println("  • ID: " + notetaker.getData().getId());
        System.out.println("  • Name: " + notetaker.getData().getName());
        System.out.println("  • State: " + notetaker.getData().getState());
        System.out.println("  • Meeting Link: " + meetingLink);
        
        return notetaker;
    }
    
    /**
     * Lists all Notetakers in your account
     */
    private static void listNotetakers(NylasClient nylas) throws NylasApiError, NylasSdkTimeoutError {
        System.out.println("\n📋 Listing All Notetakers");
        
        ListResponse<Notetaker> notetakers = nylas.notetakers().list();
        
        System.out.println("Found " + notetakers.getData().size() + " Notetakers:");
        for (Notetaker notetaker : notetakers.getData()) {
            System.out.println("  • " + notetaker.getName());
            System.out.println("    - ID: " + notetaker.getId());
            System.out.println("    - State: " + notetaker.getState());
        }
    }
    
    /**
     * Downloads available media (recordings/transcripts) from a Notetaker
     */
    private static void getNotetakerMedia(NylasClient nylas, String notetakerId) throws NylasApiError, NylasSdkTimeoutError {
        System.out.println("\n📋 Getting Notetaker Media");
        
        Response<NotetakerMediaResponse> media = nylas.notetakers().downloadMedia(notetakerId);
        
        if (media.getData().getRecording() != null) {
            System.out.println("Recording:");
            System.out.println("  • URL: " + media.getData().getRecording().getUrl());
            System.out.println("  • Size: " + media.getData().getRecording().getSize() + " MB");
        }
        
        if (media.getData().getTranscript() != null) {
            System.out.println("Transcript:");
            System.out.println("  • URL: " + media.getData().getTranscript().getUrl());
            System.out.println("  • Size: " + media.getData().getTranscript().getSize() + " MB");
        }
    }
    
    /**
     * Leaves a Notetaker session
     */
    private static void leaveNotetaker(NylasClient nylas, String notetakerId) throws NylasApiError, NylasSdkTimeoutError {
        System.out.println("\n📋 Leaving Notetaker Session");
        
        Response<LeaveNotetakerResponse> response = nylas.notetakers().leave(notetakerId);
        System.out.println("Left Notetaker:");
        System.out.println("  • ID: " + response.getData().getId());
        System.out.println("  • Message: " + response.getData().getMessage());
    }
    
    /**
     * Helper method to get a required config value or throw an exception
     */
    private static String getOrThrow(Map<String, String> config, String key) {
        String value = config.get(key);
        if (value == null) {
            throw new IllegalArgumentException(key + " is required but not configured");
        }
        return value;
    }
    
    /**
     * Helper method to get a config value with a default
     */
    private static String getOrDefault(Map<String, String> config, String key, String defaultValue) {
        String value = config.get(key);
        return value != null ? value : defaultValue;
    }
} 