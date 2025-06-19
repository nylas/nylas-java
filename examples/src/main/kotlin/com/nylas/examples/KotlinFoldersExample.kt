package com.nylas.examples

import com.nylas.NylasClient
import com.nylas.models.ListFoldersQueryParams

/**
 * This example demonstrates how to list folders and use the new single_level parameter
 * for Microsoft accounts to control folder hierarchy traversal.
 */
fun main() {
    val nylasClient = NylasClient.Builder("<NYLAS_API_KEY>").build()
    val grantId = "<GRANT_ID>" // Replace with your grant ID

    try {
        println("📁 Listing all folders with multi-level hierarchy (default behavior)...")
        
        // List all folders with default behavior (multi-level hierarchy)
        val allFolders = nylasClient.folders().list(grantId)
        println("Found ${allFolders.data.size} folders:")
        allFolders.data.forEach { folder ->
            println("  - ${folder.name} (ID: ${folder.id})")
            if (folder.parentId != null) {
                println("    └─ Parent ID: ${folder.parentId}")
            }
        }

        println("\n📁 Listing folders with single-level hierarchy (Microsoft only)...")
        
        // List folders using single-level hierarchy parameter (Microsoft only)
        val queryParams = ListFoldersQueryParams.Builder()
            .singleLevel(true)  // This is the new parameter - Microsoft only
            .limit(50)
            .build()

        val singleLevelFolders = nylasClient.folders().list(grantId, queryParams)
        println("Found ${singleLevelFolders.data.size} folders in single-level hierarchy:")
        singleLevelFolders.data.forEach { folder ->
            println("  - ${folder.name} (ID: ${folder.id})")
        }

        println("\n📁 Demonstrating builder pattern with other parameters...")
        
        // Example with multiple parameters including the new single_level
        val detailedQueryParams = ListFoldersQueryParams.Builder()
            .singleLevel(false)  // Explicitly set to false for multi-level
            .limit(10)
            .select("id,name,parent_id,unread_count")
            .build()

        val detailedFolders = nylasClient.folders().list(grantId, detailedQueryParams)
        println("Found ${detailedFolders.data.size} folders with detailed info:")
        detailedFolders.data.forEach { folder ->
            println("  - ${folder.name}")
            println("    ID: ${folder.id}")
            println("    Unread Count: ${folder.unreadCount ?: 0}")
            if (folder.parentId != null) {
                println("    Parent ID: ${folder.parentId}")
            }
            println()
        }

        println("\n📁 Listing folders including hidden ones (Microsoft only)...")
        
        // List folders including hidden folders (Microsoft only)
        val hiddenFoldersParams = ListFoldersQueryParams.Builder()
            .includeHiddenFolders(true)  // This is the new parameter - Microsoft only
            .limit(50)
            .build()

        val hiddenFolders = nylasClient.folders().list(grantId, hiddenFoldersParams)
        println("Found ${hiddenFolders.data.size} folders including hidden ones:")
        hiddenFolders.data.forEach { folder ->
            println("  - ${folder.name} (ID: ${folder.id})")
        }

        println("\n📁 Demonstrating all parameters together...")
        
        // Example with all parameters including both new ones
        val comprehensiveParams = ListFoldersQueryParams.Builder()
            .singleLevel(false)  // Multi-level hierarchy
            .includeHiddenFolders(true)  // Include hidden folders
            .limit(10)
            .select("id,name,parent_id,unread_count")
            .build()

        val comprehensiveFolders = nylasClient.folders().list(grantId, comprehensiveParams)
        println("Found ${comprehensiveFolders.data.size} folders with comprehensive options:")
        comprehensiveFolders.data.forEach { folder ->
            println("  - ${folder.name}")
            println("    ID: ${folder.id}")
            println("    Unread Count: ${folder.unreadCount ?: 0}")
            if (folder.parentId != null) {
                println("    Parent ID: ${folder.parentId}")
            }
            println()
        }

    } catch (exception: Exception) {
        println("❌ Error listing folders: ${exception.message}")
        println("Note: The single_level parameter is Microsoft-specific and may not work with other providers.")
    }
} 