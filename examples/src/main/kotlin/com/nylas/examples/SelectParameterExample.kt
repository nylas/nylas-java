package com.nylas.examples

import com.nylas.NylasClient
import com.nylas.models.*

/**
 * Example demonstrating how to use the select parameter with various Nylas endpoints.
 * 
 * The select parameter allows you to specify which fields you want returned in the response,
 * which can help optimize response size and reduce latency by limiting queries to only 
 * the information that you need.
 * 
 * When using select, fields not included in the select parameter will be null in the response objects.
 */
class SelectParameterExample {

    private val client = NylasClient(
        apiKey = "YOUR_API_KEY"
    )

    /**
     * Example: List folders with select parameter
     * Only returns id and name fields, grant_id will be null
     */
    fun listFoldersWithSelect() {
        try {
            val queryParams = ListFoldersQueryParams.Builder()
                .limit(10)
                .select("id,name")  // Only return id and name
                .build()

            val response = client.folders().list("grant-id", queryParams)
            
            response.data.forEach { folder ->
                println("Folder ID: ${folder.id}")
                println("Folder Name: ${folder.name}")
                println("Grant ID: ${folder.grantId}") // This will be null because not in select
                println("---")
            }
        } catch (e: Exception) {
            println("Error listing folders: ${e.message}")
        }
    }

    /**
     * Example: Find folder with select parameter
     * Only returns id, name, and grant_id fields
     */
    fun findFolderWithSelect() {
        try {
            val queryParams = FindFolderQueryParams.Builder()
                .select("id,name,grant_id")
                .build()

            val response = client.folders().find("grant-id", "folder-id", queryParams)
            val folder = response.data
            
            println("Folder ID: ${folder.id}")
            println("Folder Name: ${folder.name}")
            println("Grant ID: ${folder.grantId}")
            println("Parent ID: ${folder.parentId}") // This will be null because not in select
        } catch (e: Exception) {
            println("Error finding folder: ${e.message}")
        }
    }

    /**
     * Example: List contacts with select parameter
     * Only returns id, display_name, and emails
     */
    fun listContactsWithSelect() {
        try {
            val queryParams = ListContactsQueryParams.Builder()
                .limit(5)
                .select("id,display_name,emails")
                .build()

            val response = client.contacts().list("grant-id", queryParams)
            
            response.data.forEach { contact ->
                println("Contact ID: ${contact.id}")
                println("Display Name: ${contact.displayName}")
                println("Emails: ${contact.emails?.map { it.email }}")
                println("Grant ID: ${contact.grantId}") // This will be null because not in select
                println("---")
            }
        } catch (e: Exception) {
            println("Error listing contacts: ${e.message}")
        }
    }

    /**
     * Example: Find contact with select parameter
     */
    fun findContactWithSelect() {
        try {
            val queryParams = FindContactQueryParams.Builder()
                .select("id,display_name,grant_id")
                .profilePicture(true)
                .build()

            val response = client.contacts().find("grant-id", "contact-id", queryParams)
            val contact = response.data
            
            println("Contact ID: ${contact.id}")
            println("Display Name: ${contact.displayName}")
            println("Grant ID: ${contact.grantId}")
            println("Phone Numbers: ${contact.phoneNumbers}") // This will be null because not in select
        } catch (e: Exception) {
            println("Error finding contact: ${e.message}")
        }
    }

    /**
     * Example: List messages with select parameter
     * Only returns id, subject, and from fields
     */
    fun listMessagesWithSelect() {
        try {
            val queryParams = ListMessagesQueryParams.Builder()
                .limit(10)
                .select("id,subject,from")
                .build()

            val response = client.messages().list("grant-id", queryParams)
            
            response.data.forEach { message ->
                println("Message ID: ${message.id}")
                println("Subject: ${message.subject}")
                println("From: ${message.from?.map { "${it.name} <${it.email}>" }}")
                println("Grant ID: ${message.grantId}") // This will be null because not in select
                println("---")
            }
        } catch (e: Exception) {
            println("Error listing messages: ${e.message}")
        }
    }

    /**
     * Example: Find message with select parameter
     */
    fun findMessageWithSelect() {
        try {
            val queryParams = FindMessageQueryParams.Builder()
                .select("id,subject,body,grant_id")
                .fields(MessageFields.STANDARD)
                .build()

            val response = client.messages().find("grant-id", "message-id", queryParams)
            val message = response.data
            
            println("Message ID: ${message.id}")
            println("Subject: ${message.subject}")
            println("Body: ${message.body}")
            println("Grant ID: ${message.grantId}")
            println("Attachments: ${message.attachments}") // This will be null because not in select
        } catch (e: Exception) {
            println("Error finding message: ${e.message}")
        }
    }

    /**
     * Example: List threads with select parameter
     */
    fun listThreadsWithSelect() {
        try {
            val queryParams = ListThreadsQueryParams.Builder()
                .limit(10)
                .select("id,subject,latest_draft_or_message")
                .build()

            val response = client.threads().list("grant-id", queryParams)
            
            response.data.forEach { thread ->
                println("Thread ID: ${thread.id}")
                println("Subject: ${thread.subject}")
                println("Latest Message: ${thread.latestDraftOrMessage}")
                println("Grant ID: ${thread.grantId}") // This will be null because not in select
                println("---")
            }
        } catch (e: Exception) {
            println("Error listing threads: ${e.message}")
        }
    }

    /**
     * Example: Find thread with select parameter
     */
    fun findThreadWithSelect() {
        try {
            val queryParams = FindThreadQueryParams.Builder()
                .select("id,subject,grant_id")
                .build()

            val response = client.threads().find("grant-id", "thread-id", queryParams)
            val thread = response.data
            
            println("Thread ID: ${thread.id}")
            println("Subject: ${thread.subject}")
            println("Grant ID: ${thread.grantId}")
            println("Message IDs: ${thread.messageIds}") // This will be null because not in select
        } catch (e: Exception) {
            println("Error finding thread: ${e.message}")
        }
    }

    /**
     * Example: List events with select parameter
     */
    fun listEventsWithSelect() {
        try {
            val queryParams = ListEventQueryParams.Builder("primary")
                .limit(10)
                .select("id,title,when")
                .build()

            val response = client.events().list("grant-id", queryParams)
            
            response.data.forEach { event ->
                println("Event ID: ${event.id}")
                println("Title: ${event.title}")
                println("When: ${event.`when`}")
                println("Grant ID: ${event.grantId}") // This will be null because not in select
                println("---")
            }
        } catch (e: Exception) {
            println("Error listing events: ${e.message}")
        }
    }

    /**
     * Example: Find event with select parameter
     */
    fun findEventWithSelect() {
        try {
            val queryParams = FindEventQueryParams.Builder("primary")
                .select("id,title,description,grant_id")
                .build()

            val response = client.events().find("grant-id", "event-id", queryParams)
            val event = response.data
            
            println("Event ID: ${event.id}")
            println("Title: ${event.title}")
            println("Description: ${event.description}")
            println("Grant ID: ${event.grantId}")
            println("Participants: ${event.participants}") // This will be null because not in select
        } catch (e: Exception) {
            println("Error finding event: ${e.message}")
        }
    }

    /**
     * Example: List calendars with select parameter
     */
    fun listCalendarsWithSelect() {
        try {
            val queryParams = ListCalendersQueryParams.Builder()
                .limit(10)
                .select("id,name,description")
                .build()

            val response = client.calendars().list("grant-id", queryParams)
            
            response.data.forEach { calendar ->
                println("Calendar ID: ${calendar.id}")
                println("Name: ${calendar.name}")
                println("Description: ${calendar.description}")
                println("Grant ID: ${calendar.grantId}") // This will be null because not in select
                println("---")
            }
        } catch (e: Exception) {
            println("Error listing calendars: ${e.message}")
        }
    }

    /**
     * Example: Find calendar with select parameter
     */
    fun findCalendarWithSelect() {
        try {
            val queryParams = FindCalendarQueryParams.Builder()
                .select("id,name,grant_id")
                .build()

            val response = client.calendars().find("grant-id", "calendar-id", queryParams)
            val calendar = response.data
            
            println("Calendar ID: ${calendar.id}")
            println("Name: ${calendar.name}")
            println("Grant ID: ${calendar.grantId}")
            println("Timezone: ${calendar.timezone}") // This will be null because not in select
        } catch (e: Exception) {
            println("Error finding calendar: ${e.message}")
        }
    }

    /**
     * Example: Find attachment with select parameter
     */
    fun findAttachmentWithSelect() {
        try {
            val queryParams = FindAttachmentQueryParams.Builder("message-id")
                .select("id,filename,content_type")
                .build()

            val response = client.attachments().find("grant-id", "attachment-id", queryParams)
            val attachment = response.data
            
            println("Attachment ID: ${attachment.id}")
            println("Filename: ${attachment.filename}")
            println("Content Type: ${attachment.contentType}")
            println("Grant ID: ${attachment.grantId}") // This will be null because not in select
        } catch (e: Exception) {
            println("Error finding attachment: ${e.message}")
        }
    }

    /**
     * Example: Download attachment with select parameter
     * Note: The download itself returns the binary data, but you can still use select 
     * to limit the metadata returned in other operations
     */
    fun downloadAttachmentWithSelect() {
        try {
            val queryParams = FindAttachmentQueryParams.Builder("message-id")
                .select("id,filename,content_type,size")
                .build()

            // Download the attachment data
            val responseBody = client.attachments().download("grant-id", "attachment-id", queryParams)
            val data = responseBody.bytes()
            responseBody.close()
            
            println("Downloaded ${data.size} bytes")
        } catch (e: Exception) {
            println("Error downloading attachment: ${e.message}")
        }
    }
}

fun main() {
    val example = SelectParameterExample()
    
    println("=== Select Parameter Examples ===")
    println()
    
    println("1. List folders with select:")
    example.listFoldersWithSelect()
    println()
    
    println("2. Find folder with select:")
    example.findFolderWithSelect()
    println()
    
    println("3. List contacts with select:")
    example.listContactsWithSelect()
    println()
    
    // Add more examples as needed...
}
