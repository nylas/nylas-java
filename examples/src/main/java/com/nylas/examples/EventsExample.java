package com.nylas.examples;

import com.nylas.NylasClient;
import com.nylas.models.NylasApiError;
import com.nylas.models.NylasSdkTimeoutError;
import com.nylas.models.Calendar;
import com.nylas.models.Event;
import com.nylas.models.ListEventQueryParams;
import com.nylas.models.Response;
import com.nylas.models.ListResponse;
import com.nylas.models.EventQueryOrderBy;
import com.nylas.models.EventQueryOrderBy;
import okhttp3.OkHttpClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EventsExample {
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
            runEventsExample(nylas, config);

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
    
    private static void runEventsExample(NylasClient nylas, Map<String, String> config) throws NylasApiError, NylasSdkTimeoutError {
        // Get the primary calendar
        Response<Calendar> primaryCalendar = nylas.calendars().find(config.get("NYLAS_GRANT_ID"), "primary");

        // Get the start and end times for the query
        long start = (System.currentTimeMillis() / 1000L) - (60 * 60 * 24 * 30); // 30 days ago in Unix timestamp
        long end = (System.currentTimeMillis() / 1000L) + (60 * 60 * 24 * 30); // 30 days from now in Unix timestamp

        // List events
        ListEventQueryParams.Builder eventQueryBuilder = new ListEventQueryParams.Builder(primaryCalendar.getData().getId())
                .start(String.valueOf(start))
                .end(String.valueOf(end))
                .expandRecurring(false)
                .showCancelled(true)
                .orderBy(EventQueryOrderBy.START)
                .limit(200);

        // Get the events   
        ListResponse<Event> events = nylas.events().list(config.get("NYLAS_GRANT_ID"), eventQueryBuilder.build());

        // Print the events
        System.out.println("Found " + events.getData().size() + " events");
        for (Event event : events.getData()) {
            System.out.println(event);
        }
    }
}
