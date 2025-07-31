package com.nylas.examples;

import com.nylas.NylasClient;
import com.nylas.models.CreateAttachmentRequest;
import com.nylas.models.EmailName;
import com.nylas.models.Message;
import com.nylas.models.NylasApiError;
import com.nylas.models.NylasSdkTimeoutError;
import com.nylas.models.Response;
import com.nylas.models.SendMessageRequest;
import com.nylas.models.TrackingOptions;
import com.nylas.util.FileUtils;
import okhttp3.OkHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Example demonstrating sending large file attachments with Nylas Java SDK:
 * - Creating test files of 3MB and 10MB 
 * - Sending messages with large attachments (automatically handled via multipart form data)
 * - Demonstrating the SDK's automatic switching between JSON and form data based on attachment size
 * - Using the FileUtils.attachFileRequestBuilder() helper method
 */
public class LargeAttachmentsExample {
    // File sizes for testing
    private static final int FILE_SIZE_3MB = 3 * 1024 * 1024; // 3MB
    private static final int FILE_SIZE_10MB = 10 * 1024 * 1024; // 10MB
    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");
    
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

            // Run the large attachments example workflow
            runLargeAttachmentsExample(nylas, config);

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
        List<String> requiredKeys = Arrays.asList("NYLAS_API_KEY", "NYLAS_GRANT_ID", "NYLAS_TEST_EMAIL");
        List<String> missingKeys = requiredKeys.stream()
            .filter(key -> !config.containsKey(key))
            .collect(Collectors.toList());
        
        if (!missingKeys.isEmpty()) {
            throw new IllegalArgumentException(
                "Missing required configuration: " + String.join(", ", missingKeys) + "\n" +
                "Please set these in examples/.env or as environment variables.\n" +
                "NYLAS_TEST_EMAIL should be set to a valid email address for testing."
            );
        }
        
        return config;
    }
    
    private static void runLargeAttachmentsExample(NylasClient nylas, Map<String, String> config) 
            throws NylasApiError, NylasSdkTimeoutError, IOException {
        String grantId = config.get("NYLAS_GRANT_ID");
        String testEmail = config.get("NYLAS_TEST_EMAIL");
        
        System.out.println("🔗 Demonstrating Nylas Large Attachments API...\n");
        System.out.println("Test email recipient: " + testEmail);
        System.out.println("Temp directory: " + TEMP_DIR);
        System.out.println();
        
        File testFile3MB = null;
        File testFile10MB = null;
        
        try {
            // 1. Create test files
            testFile3MB = createTestFile("test-3mb.txt", FILE_SIZE_3MB);
            testFile10MB = createTestFile("test-10mb.txt", FILE_SIZE_10MB);
            
            // 2. Send email with 3MB attachment (should use form data)
            demonstrateLargeAttachmentSending(nylas, grantId, testEmail, testFile3MB, "3MB");
            
            // 3. Send email with 10MB attachment (should use form data)
            demonstrateLargeAttachmentSending(nylas, grantId, testEmail, testFile10MB, "10MB");
            
            // 4. Send email with both attachments
            demonstrateMultipleLargeAttachments(nylas, grantId, testEmail, testFile3MB, testFile10MB);
            
            // 5. Demonstrate small vs large attachment handling
            demonstrateAttachmentSizeHandling(nylas, grantId, testEmail);
            
        } finally {
            // Clean up test files
            if (testFile3MB != null && testFile3MB.exists()) {
                testFile3MB.delete();
                System.out.println("🧹 Cleaned up: " + testFile3MB.getName());
            }
            if (testFile10MB != null && testFile10MB.exists()) {
                testFile10MB.delete();
                System.out.println("🧹 Cleaned up: " + testFile10MB.getName());
            }
        }
    }
    
    /**
     * Creates a test file with the specified name and size
     */
    private static File createTestFile(String filename, int sizeInBytes) throws IOException {
        System.out.println("📁 Creating test file: " + filename + " (" + formatFileSize(sizeInBytes) + ")");
        
        File file = new File(TEMP_DIR, filename);
        
        // Generate random content to make the file realistic
        Random random = new Random();
        byte[] buffer = new byte[8192]; // 8KB buffer
        
        try (FileOutputStream fos = new FileOutputStream(file)) {
            int remainingBytes = sizeInBytes;
            
            while (remainingBytes > 0) {
                int bytesToWrite = Math.min(buffer.length, remainingBytes);
                
                // Fill buffer with random data
                random.nextBytes(buffer);
                
                // For readability, occasionally add line breaks
                if (random.nextInt(20) == 0 && bytesToWrite > 1) {
                    buffer[bytesToWrite - 1] = '\n';
                }
                
                fos.write(buffer, 0, bytesToWrite);
                remainingBytes -= bytesToWrite;
            }
        }
        
        System.out.println("   ✅ Created: " + file.getAbsolutePath());
        return file;
    }
    
    private static void demonstrateLargeAttachmentSending(NylasClient nylas, String grantId, 
            String testEmail, File attachmentFile, String fileDescription) 
            throws NylasApiError, NylasSdkTimeoutError {
        
        System.out.println("📧 Sending email with " + fileDescription + " attachment:");
        System.out.println("   File: " + attachmentFile.getName());
        System.out.println("   Size: " + formatFileSize((int) attachmentFile.length()));
        
        // Create attachment request using FileUtils helper
        CreateAttachmentRequest attachment = FileUtils.attachFileRequestBuilder(attachmentFile.getAbsolutePath());
        
        // Create the email request
        SendMessageRequest sendRequest = new SendMessageRequest.Builder(Arrays.asList(new EmailName(testEmail, "Test Recipient")))
            .subject("Large Attachment Test - " + fileDescription + " File")
            .body(createEmailBody(fileDescription, attachmentFile))
            .attachments(Arrays.asList(attachment))
            .trackingOptions(new TrackingOptions("large-attachment-test", true, true, true))
            .build();
        
        // Send the message
        long startTime = System.currentTimeMillis();
        Response<Message> response = nylas.messages().send(grantId, sendRequest);
        long endTime = System.currentTimeMillis();
        
        Message sentMessage = response.getData();
        System.out.println("   ✅ Email sent successfully!");
        System.out.println("   Message ID: " + sentMessage.getId());
        System.out.println("   Send time: " + (endTime - startTime) + "ms");
        System.out.println("   Attachments in response: " + 
            (sentMessage.getAttachments() != null ? sentMessage.getAttachments().size() : 0));
        System.out.println();
    }
    
    private static void demonstrateMultipleLargeAttachments(NylasClient nylas, String grantId, 
            String testEmail, File file3MB, File file10MB) 
            throws NylasApiError, NylasSdkTimeoutError {
        
        System.out.println("📧 Sending email with multiple large attachments:");
        System.out.println("   File 1: " + file3MB.getName() + " (" + formatFileSize((int) file3MB.length()) + ")");
        System.out.println("   File 2: " + file10MB.getName() + " (" + formatFileSize((int) file10MB.length()) + ")");
        System.out.println("   Total size: " + formatFileSize((int) (file3MB.length() + file10MB.length())));
        
        // Create attachment requests
        CreateAttachmentRequest attachment3MB = FileUtils.attachFileRequestBuilder(file3MB.getAbsolutePath());
        CreateAttachmentRequest attachment10MB = FileUtils.attachFileRequestBuilder(file10MB.getAbsolutePath());
        
        // Create the email request with multiple attachments
        SendMessageRequest sendRequest = new SendMessageRequest.Builder(Arrays.asList(new EmailName(testEmail, "Test Recipient")))
            .subject("Multiple Large Attachments Test - 13MB Total")
            .body(createMultipleAttachmentsEmailBody(file3MB, file10MB))
            .attachments(Arrays.asList(attachment3MB, attachment10MB))
            .trackingOptions(new TrackingOptions("multiple-large-attachments-test", true, true, null))
            .build();
        
        // Send the message
        long startTime = System.currentTimeMillis();
        Response<Message> response = nylas.messages().send(grantId, sendRequest);
        long endTime = System.currentTimeMillis();
        
        Message sentMessage = response.getData();
        System.out.println("   ✅ Email sent successfully!");
        System.out.println("   Message ID: " + sentMessage.getId());
        System.out.println("   Send time: " + (endTime - startTime) + "ms");
        System.out.println("   Attachments in response: " + 
            (sentMessage.getAttachments() != null ? sentMessage.getAttachments().size() : 0));
        System.out.println();
    }
    
    private static void demonstrateAttachmentSizeHandling(NylasClient nylas, String grantId, String testEmail) 
            throws IOException, NylasApiError, NylasSdkTimeoutError {
        
        System.out.println("🔄 Demonstrating SDK's automatic handling of different attachment sizes:");
        
        // Create a small file (under 3MB threshold)
        File smallFile = createTestFile("test-small.txt", 1024 * 1024); // 1MB
        
        try {
            System.out.println("   📄 Small attachment (1MB) - will use JSON encoding:");
            CreateAttachmentRequest smallAttachment = FileUtils.attachFileRequestBuilder(smallFile.getAbsolutePath());
            
            SendMessageRequest smallFileRequest = new SendMessageRequest.Builder(Arrays.asList(new EmailName(testEmail, "Test Recipient")))
                .subject("Small Attachment Test - 1MB File (JSON Encoding)")
                .body("<p>This email contains a small (1MB) attachment that will be sent using JSON encoding.</p>" +
                      "<p>The Nylas SDK automatically chooses the optimal encoding method based on file size.</p>" +
                      "<ul>" +
                      "<li>Files under 3MB: JSON encoding (faster for small files)</li>" +
                      "<li>Files 3MB and above: Multipart form encoding (more efficient for large files)</li>" +
                      "</ul>")
                .attachments(Arrays.asList(smallAttachment))
                .build();
            
            long startTime = System.currentTimeMillis();
            Response<Message> response = nylas.messages().send(grantId, smallFileRequest);
            long endTime = System.currentTimeMillis();
            
            System.out.println("     ✅ Small file sent via JSON encoding");
            System.out.println("     Message ID: " + response.getData().getId());
            System.out.println("     Send time: " + (endTime - startTime) + "ms");
            
        } finally {
            if (smallFile.exists()) {
                smallFile.delete();
                System.out.println("🧹 Cleaned up: " + smallFile.getName());
            }
        }
        
        System.out.println("\n   📊 Summary of SDK attachment handling:");
        System.out.println("     • Files < 3MB: Automatic JSON encoding (base64)");
        System.out.println("     • Files ≥ 3MB: Automatic multipart form encoding (streaming)");
        System.out.println("     • The switch is transparent to the developer");
        System.out.println("     • Large files use streaming to minimize memory usage");
        System.out.println();
    }
    
    private static String createEmailBody(String fileDescription, File attachmentFile) {
        return String.format(
            "<h2>Large Attachment Test - %s File</h2>" +
            "<p>This email demonstrates sending large file attachments using the Nylas Java SDK.</p>" +
            "<h3>Attachment Details:</h3>" +
            "<ul>" +
            "<li><strong>Filename:</strong> %s</li>" +
            "<li><strong>Size:</strong> %s</li>" +
            "<li><strong>Encoding:</strong> Multipart form data (automatic for files ≥ 3MB)</li>" +
            "</ul>" +
            "<p>The Nylas SDK automatically handles large attachments by switching from JSON encoding " +
            "to multipart form encoding when files are 3MB or larger. This provides optimal performance " +
            "and memory usage for both small and large files.</p>" +
            "<p><em>This is a test email sent from the Nylas Java SDK Large Attachments Example.</em></p>",
            fileDescription,
            attachmentFile.getName(),
            formatFileSize((int) attachmentFile.length())
        );
    }
    
    private static String createMultipleAttachmentsEmailBody(File file3MB, File file10MB) {
        return String.format(
            "<h2>Multiple Large Attachments Test</h2>" +
            "<p>This email demonstrates sending multiple large file attachments in a single message.</p>" +
            "<h3>Attached Files:</h3>" +
            "<ol>" +
            "<li><strong>%s</strong> - %s</li>" +
            "<li><strong>%s</strong> - %s</li>" +
            "</ol>" +
            "<h3>Total Size:</h3>" +
            "<p><strong>%s</strong> across 2 files</p>" +
            "<p>Both files exceed the 3MB threshold, so the entire message is sent using " +
            "multipart form encoding for optimal performance.</p>" +
            "<p><em>This is a test email sent from the Nylas Java SDK Large Attachments Example.</em></p>",
            file3MB.getName(),
            formatFileSize((int) file3MB.length()),
            file10MB.getName(),
            formatFileSize((int) file10MB.length()),
            formatFileSize((int) (file3MB.length() + file10MB.length()))
        );
    }
    
    /**
     * Formats file size in human-readable format
     */
    private static String formatFileSize(int bytes) {
        if (bytes < 1024) {
            return bytes + " bytes";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.1f KB", bytes / 1024.0);
        } else {
            return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
        }
    }
}