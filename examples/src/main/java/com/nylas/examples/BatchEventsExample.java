package com.nylas.examples;

import com.nylas.NylasClient;
import com.nylas.models.BatchEventOperation;
import com.nylas.models.BatchEventResult;
import com.nylas.models.BatchEventsQueryParams;
import com.nylas.models.BatchEventsRequest;
import com.nylas.models.BatchEventsResponse;
import com.nylas.models.CreateEventRequest;
import com.nylas.models.NylasApiError;
import com.nylas.models.UpdateEventRequest;
import okhttp3.OkHttpClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Runs several event operations in one request with the Events batch endpoint (Google and Microsoft only).
 *
 * Reads configuration from environment variables, then from examples/.env or .env.
 * Required: NYLAS_API_KEY, NYLAS_GRANT_ID, NYLAS_EVENT_ID (an existing event to update).
 * Optional: NYLAS_CALENDAR_ID (defaults to "primary"), NYLAS_API_URI.
 */
public class BatchEventsExample {
    private static final int MAX_ATTEMPTS = 3;
    private static final double DEFAULT_RETRY_SECONDS = 1.0;

    public static void main(String[] args) throws Exception {
        Map<String, String> config = loadConfig();
        NylasClient nylas = new NylasClient(
            require(config, "NYLAS_API_KEY"),
            new OkHttpClient.Builder(),
            config.getOrDefault("NYLAS_API_URI", "https://api.us.nylas.com")
        );
        String grantId = require(config, "NYLAS_GRANT_ID");
        String eventId = require(config, "NYLAS_EVENT_ID");
        BatchEventsQueryParams queryParams =
            new BatchEventsQueryParams.Builder(config.getOrDefault("NYLAS_CALENDAR_ID", "primary")).build();

        long start = System.currentTimeMillis() / 1000L + 60 * 60 * 24;
        List<BatchEventOperation> operations = Arrays.asList(
            BatchEventOperation.create(new CreateEventRequest.Builder(
                new CreateEventRequest.When.Timespan.Builder((int) start, (int) start + 1800).build()
            ).title("Batch standup").build()),
            BatchEventOperation.update(eventId, new UpdateEventRequest.Builder().title("Renamed by batch").build()),
            BatchEventOperation.delete("event-id-that-does-not-exist")
        );

        try {
            for (int attempt = 1; attempt <= MAX_ATTEMPTS && !operations.isEmpty(); attempt++) {
                // A 207 (some or all operations failed) is returned, not thrown.
                // Any other non-2xx status for the whole call (e.g. 400 for an invalid batch) throws NylasApiError.
                BatchEventsResponse response = nylas.events().batch(grantId, new BatchEventsRequest(operations), queryParams);
                System.out.println("Attempt " + attempt + " (request_id " + response.getRequestId() + "):");

                List<BatchEventOperation> retry = new ArrayList<>();
                double waitSeconds = 0;

                // data.get(i) is the result of operations.get(i).
                for (int i = 0; i < response.getData().size(); i++) {
                    BatchEventResult result = response.getData().get(i);
                    if (result.getError() == null) {
                        System.out.println("  " + i + ": OK " + result.getStatus()
                            + (result.getData() != null ? " event " + result.getData().getId() : ""));
                        continue;
                    }
                    System.out.println("  " + i + ": FAILED " + result.getStatus() + " " + result.getError().getMessage());

                    // Never retry an item the provider already created: retrying would create a duplicate event.
                    if (Boolean.TRUE.equals(result.getProviderEventCreated())) {
                        System.out.println("     not retrying: the provider already created this event");
                        continue;
                    }
                    if (result.getStatus() == 429 || result.getStatus() == 503) {
                        retry.add(operations.get(i));
                        double retryAfter = result.getRetryAfter() != null ? result.getRetryAfter() : DEFAULT_RETRY_SECONDS;
                        waitSeconds = Math.max(waitSeconds, retryAfter);
                    }
                }

                if (!retry.isEmpty() && attempt < MAX_ATTEMPTS) {
                    System.out.println("Retrying " + retry.size() + " throttled operation(s) after " + waitSeconds + "s");
                    Thread.sleep((long) (waitSeconds * 1000));
                }
                operations = retry;
            }
        } catch (NylasApiError e) {
            System.out.println("Batch request failed (HTTP " + e.getStatusCode() + "): " + e.getMessage()
                + " request_id=" + e.getRequestId());
        }
    }

    private static Map<String, String> loadConfig() {
        Map<String, String> config = new HashMap<>(System.getenv());
        for (String path : Arrays.asList("examples/.env", ".env")) {
            File envFile = new File(path);
            if (!envFile.exists()) {
                continue;
            }
            try {
                for (String line : Files.readAllLines(envFile.toPath())) {
                    String[] parts = line.split("=", 2);
                    if (parts.length == 2 && !line.trim().startsWith("#")) {
                        config.putIfAbsent(parts[0].trim(), parts[1].trim());
                    }
                }
            } catch (IOException e) {
                System.out.println("Warning: failed to read " + path + ": " + e.getMessage());
            }
        }
        return config;
    }

    private static String require(Map<String, String> config, String key) {
        String value = config.get(key);
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(
                "Missing required configuration: " + key + ". Set it in examples/.env or as an environment variable.");
        }
        return value;
    }
}
