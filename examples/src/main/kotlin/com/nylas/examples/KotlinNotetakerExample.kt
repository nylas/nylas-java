package com.nylas.examples

import com.nylas.NylasClient
import com.nylas.models.*
import java.io.File

/**
 * This example demonstrates how to use the Nylas Kotlin SDK to work with Notetakers.
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
 *    ./gradlew :examples:run -PmainClass=com.nylas.examples.KotlinNotetakerExampleKt
 *    Or use the Makefile: make kotlin-way
 */

fun main() {
    try {
        // Load configuration from environment variables or .env file
        val config = loadConfig()
        
        // Initialize the Nylas client with your API key
        val nylas = NylasClient(
            apiKey = config.getOrThrow("NYLAS_API_KEY"),
            apiUri = config.getOrDefault("NYLAS_API_URI", "https://api.us.nylas.com")
        )
        
        // Example workflow
        runNotetakerExample(nylas, config)
        
    } catch (e: NylasApiError) {
        println("❌ Nylas API Error: ${e.message}")
        println("   Status code: ${e.statusCode}")
        println("   Request ID: ${e.requestId}")
        System.exit(1)
    } catch (e: IllegalArgumentException) {
        println("❌ Configuration Error: ${e.message}")
        System.exit(1)
    } catch (e: Exception) {
        println("❌ Unexpected Error: ${e.message}")
        e.printStackTrace()
        System.exit(1)
    }
}

/**
 * Runs through the complete Notetaker example workflow
 */
private fun runNotetakerExample(nylas: NylasClient, config: Map<String, String>) {
    println("\n🚀 Starting Nylas Notetaker Example")
    
    // Step 1: Invite a Notetaker to a meeting
    val notetaker = inviteNotetaker(nylas, config)
    println("\n✅ Successfully invited Notetaker")
    
    // Step 2: List all Notetakers
    listNotetakers(nylas)
    println("\n✅ Successfully listed all Notetakers")
    
    // Step 3: Get media from the Notetaker (if available)
    if (notetaker.data.state == Notetaker.NotetakerState.MEDIA_AVAILABLE) {
        getNotetakerMedia(nylas, notetaker.data.id)
        println("\n✅ Successfully retrieved Notetaker media")
    } else {
        println("\nℹ️ No media available yet for this Notetaker")
    }
    
    // Step 4: Leave the Notetaker session
    leaveNotetaker(nylas, notetaker.data.id)
    println("\n✅ Successfully left Notetaker session")
}

/**
 * Loads configuration from environment variables and .env file
 * @throws IllegalArgumentException if required configuration is missing
 */
private fun loadConfig(): Map<String, String> {
    val config = mutableMapOf<String, String>()
    
    // Try loading from environment variables first
    System.getenv()
        .filter { (key, _) -> key.startsWith("NYLAS_") || key == "MEETING_LINK" }
        .forEach { (key, value) -> config[key] = value }
    
    // Then try loading from .env file if needed
    listOf("examples/.env", ".env").forEach { path ->
        val envFile = File(path)
        if (envFile.exists()) {
            println("📝 Loading configuration from ${envFile.absolutePath}")
            envFile.useLines { lines ->
                lines
                    .filter { it.isNotBlank() && !it.startsWith("#") }
                    .forEach { line ->
                        val (key, value) = line.split("=", limit = 2)
                            .map { it.trim() }
                        if (!config.containsKey(key)) {
                            config[key] = value
                        }
                    }
            }
        }
    }
    
    // Validate required configuration
    val requiredKeys = listOf("NYLAS_API_KEY", "MEETING_LINK")
    val missingKeys = requiredKeys.filter { !config.containsKey(it) }
    if (missingKeys.isNotEmpty()) {
        throw IllegalArgumentException(
            "Missing required configuration: ${missingKeys.joinToString(", ")}\n" +
            "Please set these in examples/.env or as environment variables."
        )
    }
    
    return config
}

/**
 * Invites a Notetaker to join a meeting
 * @param nylas The Nylas client
 * @param config Configuration containing the meeting link
 * @return Response containing the created Notetaker
 */
private fun inviteNotetaker(nylas: NylasClient, config: Map<String, String>): Response<Notetaker> {
    println("\n📋 Inviting Notetaker to Meeting")
    
    val meetingLink = config.getOrThrow("MEETING_LINK")
    
    // Configure the Notetaker's meeting settings
    val meetingSettings = CreateNotetakerRequest.MeetingSettings(
        videoRecording = true,
        audioRecording = true,
        transcription = true
    )
    
    // Create and configure the Notetaker
    val request = CreateNotetakerRequest(
        meetingLink = meetingLink,
        name = "Nylas Notetaker Example",
        meetingSettings = meetingSettings
    )
    
    // Send the invitation
    val notetaker = nylas.notetakers().create(request)
    
    println("""
        |Notetaker Details:
        |  • ID: ${notetaker.data.id}
        |  • Name: ${notetaker.data.name}
        |  • State: ${notetaker.data.state}
        |  • Meeting Link: $meetingLink
    """.trimMargin())
    
    return notetaker
}

/**
 * Lists all Notetakers in your account
 */
private fun listNotetakers(nylas: NylasClient) {
    println("\n📋 Listing All Notetakers")
    
    val notetakers = nylas.notetakers().list()
    
    println("Found ${notetakers.data.size} Notetakers:")
    notetakers.data.forEach { notetaker ->
        println("""
            |  • ${notetaker.name}
            |    - ID: ${notetaker.id}
            |    - State: ${notetaker.state}
        """.trimMargin())
    }
}

/**
 * Downloads available media (recordings/transcripts) from a Notetaker
 */
private fun getNotetakerMedia(nylas: NylasClient, notetakerId: String) {
    println("\n📋 Getting Notetaker Media")
    
    val media = nylas.notetakers().downloadMedia(notetakerId)
    
    media.data.recording?.let { recording ->
        println("""
            |Recording:
            |  • URL: ${recording.url}
            |  • Size: ${recording.size} MB
        """.trimMargin())
    }
    
    media.data.transcript?.let { transcript ->
        println("""
            |Transcript:
            |  • URL: ${transcript.url}
            |  • Size: ${transcript.size} MB
        """.trimMargin())
    }
}

/**
 * Leaves a Notetaker session
 */
private fun leaveNotetaker(nylas: NylasClient, notetakerId: String) {
    println("\n📋 Leaving Notetaker Session")
    
    val response = nylas.notetakers().leave(notetakerId)
    println("""
        |Left Notetaker:
        |  • ID: ${response.data.id}
        |  • Message: ${response.data.message}
    """.trimMargin())
}

// Extension function to safely get a required config value
private fun Map<String, String>.getOrThrow(key: String): String =
    this[key] ?: throw IllegalArgumentException("$key is required but not configured") 