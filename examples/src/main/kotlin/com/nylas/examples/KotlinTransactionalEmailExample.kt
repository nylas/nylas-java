package com.nylas.examples

import com.nylas.NylasClient
import com.nylas.models.*
import okhttp3.OkHttpClient
import java.io.File
import java.io.IOException
import java.nio.file.Files
import kotlin.system.exitProcess

/**
 * Example demonstrating how to send transactional emails using the Nylas Java/Kotlin SDK.
 *
 * Transactional send lets you send individual emails from a verified domain without
 * a connected end-user grant. Common use cases include:
 *  - Receipts and order confirmations
 *  - Password-reset and account-verification emails
 *  - Notification and alert emails
 *
 * Prerequisites:
 *  - A Nylas application with at least one verified domain
 *  - NYLAS_API_KEY  — your Nylas API key
 *  - NYLAS_DOMAIN   — the verified domain to send from (e.g. "acme.com")
 *  - NYLAS_SENDER_EMAIL    — a sender address on that domain (e.g. "noreply@acme.com")
 *  - NYLAS_RECIPIENT_EMAIL — the address that will receive the test email
 */
fun main() {
    try {
        val config = loadTransactionalConfig()

        val nylas = NylasClient(
            config["NYLAS_API_KEY"]!!,
            OkHttpClient.Builder(),
            config.getOrDefault("NYLAS_API_URI", "https://api.us.nylas.com"),
        )

        runTransactionalEmailExample(nylas, config)
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

// ---------------------------------------------------------------------------
// Config loader
// ---------------------------------------------------------------------------

private fun loadTransactionalConfig(): Map<String, String> {
    val config = mutableMapOf<String, String>()

    System.getenv().filter { it.key.startsWith("NYLAS_") }
        .forEach { config[it.key] = it.value }

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
                            if (key !in config) config[key] = value
                        }
                    }
            } catch (e: IOException) {
                println("Warning: Failed to load .env file: ${e.message}")
            }
        }
    }

    val requiredKeys = listOf("NYLAS_API_KEY", "NYLAS_DOMAIN", "NYLAS_SENDER_EMAIL", "NYLAS_RECIPIENT_EMAIL")
    val missingKeys = requiredKeys.filter { it !in config }
    if (missingKeys.isNotEmpty()) {
        throw IllegalArgumentException(
            "Missing required configuration: ${missingKeys.joinToString(", ")}\n" +
                "Please set these in examples/.env or as environment variables.",
        )
    }

    return config
}

// ---------------------------------------------------------------------------
// Examples
// ---------------------------------------------------------------------------

private fun runTransactionalEmailExample(nylas: NylasClient, config: Map<String, String>) {
    val domainName = config["NYLAS_DOMAIN"]!!
    val senderEmail = config["NYLAS_SENDER_EMAIL"]!!
    val recipientEmail = config["NYLAS_RECIPIENT_EMAIL"]!!

    println("📧 Nylas Transactional Email Examples\n")

    // 1. Simple HTML email
    sendSimpleEmail(nylas, domainName, senderEmail, recipientEmail)

    // 2. Plain-text email
    sendPlaintextEmail(nylas, domainName, senderEmail, recipientEmail)

    // 3. Email with tracking options
    sendEmailWithTracking(nylas, domainName, senderEmail, recipientEmail)

    // 4. Scheduled email (send_at)
    sendScheduledEmail(nylas, domainName, senderEmail, recipientEmail)

    // 5. Email with custom headers and CC/BCC
    sendEmailWithExtras(nylas, domainName, senderEmail, recipientEmail)
}

/**
 * 1. Send a simple HTML email from a verified domain.
 */
private fun sendSimpleEmail(
    nylas: NylasClient,
    domainName: String,
    senderEmail: String,
    recipientEmail: String,
) {
    println("1️⃣  Sending a simple HTML email...")

    val request = SendTransactionalEmailRequest.Builder(
        to = listOf(EmailName(recipientEmail, "Recipient")),
        from = EmailName(senderEmail, "Sender"),
    )
        .subject("Hello from Nylas Transactional Send!")
        .body(
            """
            <html>
              <body>
                <h1>Hello!</h1>
                <p>This is a transactional email sent via the <b>Nylas Java SDK</b>.</p>
              </body>
            </html>
            """.trimIndent(),
        )
        .build()

    val response = nylas.domains().sendTransactionalEmail(domainName, request)
    println("   ✅ Sent! Message ID: ${response.data.id}\n")
}

/**
 * 2. Send a plain-text-only email (no HTML MIME part).
 */
private fun sendPlaintextEmail(
    nylas: NylasClient,
    domainName: String,
    senderEmail: String,
    recipientEmail: String,
) {
    println("2️⃣  Sending a plain-text email...")

    val request = SendTransactionalEmailRequest.Builder(
        to = listOf(EmailName(recipientEmail, "Recipient")),
        from = EmailName(senderEmail, "Sender"),
    )
        .subject("Plain-text transactional email")
        .body("Hello!\n\nThis message contains no HTML.\n\nRegards,\nNylas SDK")
        .isPlaintext(true)
        .build()

    val response = nylas.domains().sendTransactionalEmail(domainName, request)
    println("   ✅ Sent! Message ID: ${response.data.id}\n")
}

/**
 * 3. Send an email with open/link tracking enabled.
 */
private fun sendEmailWithTracking(
    nylas: NylasClient,
    domainName: String,
    senderEmail: String,
    recipientEmail: String,
) {
    println("3️⃣  Sending an email with tracking options...")

    val tracking = TrackingOptions(
        opens = true,
        links = true,
        threadReplies = true,
        label = "transactional-demo",
    )

    val request = SendTransactionalEmailRequest.Builder(
        to = listOf(EmailName(recipientEmail, "Recipient")),
        from = EmailName(senderEmail, "Sender"),
    )
        .subject("Tracked transactional email")
        .body("<p>This email has open and link tracking enabled.</p>")
        .trackingOptions(tracking)
        .build()

    val response = nylas.domains().sendTransactionalEmail(domainName, request)
    println("   ✅ Sent! Message ID: ${response.data.id}\n")
}

/**
 * 4. Schedule an email to be delivered in the future.
 */
private fun sendScheduledEmail(
    nylas: NylasClient,
    domainName: String,
    senderEmail: String,
    recipientEmail: String,
) {
    println("4️⃣  Scheduling an email to send in 10 minutes...")

    // Unix timestamp 10 minutes from now
    val sendAt = (System.currentTimeMillis() / 1000) + 600

    val request = SendTransactionalEmailRequest.Builder(
        to = listOf(EmailName(recipientEmail, "Recipient")),
        from = EmailName(senderEmail, "Sender"),
    )
        .subject("Scheduled transactional email")
        .body("<p>This email was scheduled via <code>send_at</code>.</p>")
        .sendAt(sendAt)
        .build()

    val response = nylas.domains().sendTransactionalEmail(domainName, request)
    println("   ✅ Scheduled! Message ID: ${response.data.id}\n")
}

/**
 * 5. Send an email with CC, BCC, reply-to, and custom headers.
 */
private fun sendEmailWithExtras(
    nylas: NylasClient,
    domainName: String,
    senderEmail: String,
    recipientEmail: String,
) {
    println("5️⃣  Sending an email with CC, BCC, reply-to, and custom headers...")

    val request = SendTransactionalEmailRequest.Builder(
        to = listOf(EmailName(recipientEmail, "Primary Recipient")),
        from = EmailName(senderEmail, "Sender"),
    )
        .subject("Transactional email with extras")
        .body("<p>This email includes CC, BCC, reply-to, and custom headers.</p>")
        .cc(listOf(EmailName(recipientEmail, "CC Recipient")))
        .bcc(listOf(EmailName(recipientEmail, "BCC Recipient")))
        .replyTo(listOf(EmailName("support@${senderEmail.substringAfter('@')}", "Support")))
        .customHeaders(listOf(CustomHeader("X-Campaign-ID", "transactional-demo-001")))
        .build()

    val response = nylas.domains().sendTransactionalEmail(domainName, request)
    println("   ✅ Sent! Message ID: ${response.data.id}\n")
}
