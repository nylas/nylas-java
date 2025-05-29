package com.nylas.examples

import com.nylas.NylasClient
import com.nylas.models.*
import okhttp3.OkHttpClient
import java.io.File
import java.io.IOException
import java.nio.file.Files
import kotlin.system.exitProcess

/**
 * Example demonstrating the new message features in Nylas Java/Kotlin SDK:
 * - MessageFields.INCLUDE_TRACKING_OPTIONS and MessageFields.RAW_MIME
 * - tracking_options property in Message model
 * - raw_mime property in Message model  
 * - FindMessageQueryParams with fields parameter
 */
fun main() {
    try {
        // Load configuration from environment variables or .env file
        val config = loadConfig()
        
        // Initialize the Nylas client with your API key
        val nylas = NylasClient(
            config["NYLAS_API_KEY"]!!,
            OkHttpClient.Builder(),
            config.getOrDefault("NYLAS_API_URI", "https://api.us.nylas.com")
        )

        // Run the messages example workflow
        runMessagesExample(nylas, config)

        // Exit successfully
        exitProcess(0)
        
    } catch (e: NylasApiError) {
        println("\n❌ Nylas API Error: ${e.message}")
        println("   Status code: ${e.statusCode}")
        println("   Request ID: ${e.requestId}")
        exitProcess(1)
    } catch (e: IllegalArgumentException) {
        println("\n❌ Configuration Error: ${e.message}")
        exitProcess(1)
    } catch (e: Exception) {
        println("\n❌ Unexpected Error: ${e.message}")
        e.printStackTrace()
        exitProcess(1)
    }
}

/**
 * Loads configuration from environment variables and .env file
 * @throws IllegalArgumentException if required configuration is missing
 */
private fun loadConfig(): Map<String, String> {
    val config = mutableMapOf<String, String>()
    
    // Try loading from environment variables first
    System.getenv().filter { it.key.startsWith("NYLAS_") }
        .forEach { config[it.key] = it.value }
    
    // Then try loading from .env file if needed
    val envPaths = listOf("examples/.env", ".env")
    for (path in envPaths) {
        val envFile = File(path)
        if (envFile.exists()) {
            println("📝 Loading configuration from ${envFile.absolutePath}")
            try {
                Files.lines(envFile.toPath())
                    .filter { it.trim().isNotEmpty() && !it.startsWith("#") }
                    .forEach { line ->
                        val parts = line.split("=", limit = 2)
                        if (parts.size == 2) {
                            val key = parts[0].trim()
                            val value = parts[1].trim()
                            if (key !in config) {
                                config[key] = value
                            }
                        }
                    }
            } catch (e: IOException) {
                println("Warning: Failed to load .env file: ${e.message}")
            }
        }
    }
    
    // Validate required configuration
    val requiredKeys = listOf("NYLAS_API_KEY", "NYLAS_GRANT_ID")
    val missingKeys = requiredKeys.filter { it !in config }
    
    if (missingKeys.isNotEmpty()) {
        throw IllegalArgumentException(
            "Missing required configuration: ${missingKeys.joinToString(", ")}\n" +
            "Please set these in examples/.env or as environment variables."
        )
    }
    
    return config
}

private fun runMessagesExample(nylas: NylasClient, config: Map<String, String>) {
    val grantId = config["NYLAS_GRANT_ID"]!!
    
    println("🔍 Demonstrating Nylas Messages API with new features...\n")
    
    // 1. List messages with standard fields (default behavior)
    demonstrateStandardMessageListing(nylas, grantId)
    
    // 2. List messages with include_headers field
    demonstrateIncludeHeadersListing(nylas, grantId)
    
    // 3. List messages with include_tracking_options field (new feature)
    demonstrateTrackingOptionsListing(nylas, grantId)
    
    // 4. List messages with raw_mime field (new feature)
    demonstrateRawMimeListing(nylas, grantId)
    
    // 5. Find a specific message with different field options
    demonstrateMessageFinding(nylas, grantId)
}

private fun demonstrateStandardMessageListing(nylas: NylasClient, grantId: String) {
    println("📧 1. Listing messages with standard fields:")
    
    val queryParams = ListMessagesQueryParams.Builder()
        .limit(5)
        .fields(MessageFields.STANDARD)
        .build()
        
    val messages = nylas.messages().list(grantId, queryParams)
    
    println("   Found ${messages.data.size} messages")
    messages.data.forEach { message ->
        println("   - ID: ${message.id}")
        println("     Subject: ${message.subject ?: "No subject"}")
        println("     Headers: ${if (message.headers != null) "Present" else "Not included"}")
        println("     Tracking Options: ${if (message.trackingOptions != null) "Present" else "Not included"}")
        println("     Raw MIME: ${if (message.rawMime != null) "Present" else "Not included"}")
        println()
    }
}

private fun demonstrateIncludeHeadersListing(nylas: NylasClient, grantId: String) {
    println("📧 2. Listing messages with headers included:")
    
    val queryParams = ListMessagesQueryParams.Builder()
        .limit(3)
        .fields(MessageFields.INCLUDE_HEADERS)
        .build()
        
    val messages = nylas.messages().list(grantId, queryParams)
    
    println("   Found ${messages.data.size} messages")
    messages.data.forEach { message ->
        println("   - ID: ${message.id}")
        println("     Subject: ${message.subject ?: "No subject"}")
        val headers = message.headers
        println("     Headers: ${if (headers != null) "${headers.size} headers" else "Not included"}")
        println("     Tracking Options: ${if (message.trackingOptions != null) "Present" else "Not included"}")
        println("     Raw MIME: ${if (message.rawMime != null) "Present" else "Not included"}")
        println()
    }
}

private fun demonstrateTrackingOptionsListing(nylas: NylasClient, grantId: String) {
    println("📊 3. Listing messages with tracking options included (NEW FEATURE):")
    
    val queryParams = ListMessagesQueryParams.Builder()
        .limit(3)
        .fields(MessageFields.INCLUDE_TRACKING_OPTIONS)
        .build()
        
    val messages = nylas.messages().list(grantId, queryParams)
    
    println("   Found ${messages.data.size} messages")
    messages.data.forEach { message ->
        println("   - ID: ${message.id}")
        println("     Subject: ${message.subject ?: "No subject"}")
        println("     Headers: ${if (message.headers != null) "Present" else "Not included"}")
        println("     Raw MIME: ${if (message.rawMime != null) "Present" else "Not included"}")
        
        message.trackingOptions?.let { tracking ->
            println("     ✅ Tracking Options:")
            println("       - Opens: ${tracking.opens}")
            println("       - Thread Replies: ${tracking.threadReplies}")
            println("       - Links: ${tracking.links}")
            println("       - Label: ${tracking.label}")
        } ?: println("     Tracking Options: Not available")
        println()
    }
}

private fun demonstrateRawMimeListing(nylas: NylasClient, grantId: String) {
    println("📄 4. Listing messages with raw MIME included (NEW FEATURE):")
    
    val queryParams = ListMessagesQueryParams.Builder()
        .limit(2)
        .fields(MessageFields.RAW_MIME)
        .build()
        
    val messages = nylas.messages().list(grantId, queryParams)
    
    println("   Found ${messages.data.size} messages")
    messages.data.forEach { message ->
        println("   - ID: ${message.id}")
        println("     Subject: ${message.subject ?: "No subject"}")
        println("     Headers: ${if (message.headers != null) "Present" else "Not included"}")
        println("     Tracking Options: ${if (message.trackingOptions != null) "Present" else "Not included"}")
        
        message.rawMime?.let { rawMime ->
            println("     ✅ Raw MIME: Present (${rawMime.length} characters)")
            // Show first 100 characters as preview
            if (rawMime.length > 100) {
                println("       Preview: ${rawMime.substring(0, 100)}...")
            } else {
                println("       Content: $rawMime")
            }
        } ?: println("     Raw MIME: Not available")
        println()
    }
}

private fun demonstrateMessageFinding(nylas: NylasClient, grantId: String) {
    println("🔍 5. Finding specific messages with query parameters (NEW FEATURE):")
    
    // First get a message ID to work with
    val listParams = ListMessagesQueryParams.Builder()
        .limit(1)
        .build()
    val messages = nylas.messages().list(grantId, listParams)
    
    if (messages.data.isEmpty()) {
        println("   No messages found to demonstrate with.")
        return
    }
    
    val messageId = messages.data[0].id!!
    println("   Using message ID: $messageId")
    
    // Find message with standard fields
    println("\n   📧 Finding with standard fields:")
    val standardMessage = nylas.messages().find(grantId, messageId)
    printMessageDetails(standardMessage.data, "standard")
    
    // Find message with tracking options using new FindMessageQueryParams
    println("\n   📊 Finding with tracking options (using new FindMessageQueryParams):")
    val trackingParams = FindMessageQueryParams.Builder()
        .fields(MessageFields.INCLUDE_TRACKING_OPTIONS)
        .build()
    val trackingMessage = nylas.messages().find(grantId, messageId, trackingParams)
    printMessageDetails(trackingMessage.data, "tracking options")
    
    // Find message with raw MIME using new FindMessageQueryParams
    println("\n   📄 Finding with raw MIME (using new FindMessageQueryParams):")
    val rawMimeParams = FindMessageQueryParams.Builder()
        .fields(MessageFields.RAW_MIME)
        .build()
    val rawMimeMessage = nylas.messages().find(grantId, messageId, rawMimeParams)
    printMessageDetails(rawMimeMessage.data, "raw MIME")
}

private fun printMessageDetails(message: Message, requestType: String) {
    println("     Request type: $requestType")
    println("     ID: ${message.id}")
    println("     Subject: ${message.subject ?: "No subject"}")
    println("     Headers included: ${message.headers != null}")
    println("     Tracking options included: ${message.trackingOptions != null}")
    println("     Raw MIME included: ${message.rawMime != null}")
    
    message.trackingOptions?.let { tracking ->
        println("     Tracking details: opens=${tracking.opens}, " +
               "replies=${tracking.threadReplies}, " +
               "links=${tracking.links}, " +
               "label=${tracking.label}")
    }
    
    message.rawMime?.let { rawMime ->
        println("     Raw MIME length: ${rawMime.length} characters")
    }
} 