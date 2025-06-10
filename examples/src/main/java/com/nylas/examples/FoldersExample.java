package com.nylas.examples;

import com.nylas.NylasClient;
import com.nylas.models.ListFoldersQueryParams;
import com.nylas.models.ListResponse;
import com.nylas.models.Folder;

/**
 * This example demonstrates how to list folders and use the new single_level parameter
 * for Microsoft accounts to control folder hierarchy traversal.
 */
public class FoldersExample {
    public static void main(String[] args) {
        NylasClient nylasClient = new NylasClient.Builder("<NYLAS_API_KEY>").build();
        String grantId = "<GRANT_ID>"; // Replace with your grant ID

        try {
            System.out.println("📁 Listing all folders with multi-level hierarchy (default behavior)...");
            
            // List all folders with default behavior (multi-level hierarchy)
            ListResponse<Folder> allFolders = nylasClient.folders().list(grantId);
            System.out.println("Found " + allFolders.getData().size() + " folders:");
            for (Folder folder : allFolders.getData()) {
                System.out.println("  - " + folder.getName() + " (ID: " + folder.getId() + ")");
                if (folder.getParentId() != null) {
                    System.out.println("    └─ Parent ID: " + folder.getParentId());
                }
            }

            System.out.println("\n📁 Listing folders with single-level hierarchy (Microsoft only)...");
            
            // List folders using single-level hierarchy parameter (Microsoft only)
            ListFoldersQueryParams queryParams = new ListFoldersQueryParams.Builder()
                .singleLevel(true)  // This is the new parameter - Microsoft only
                .limit(50)
                .build();

            ListResponse<Folder> singleLevelFolders = nylasClient.folders().list(grantId, queryParams);
            System.out.println("Found " + singleLevelFolders.getData().size() + " folders in single-level hierarchy:");
            for (Folder folder : singleLevelFolders.getData()) {
                System.out.println("  - " + folder.getName() + " (ID: " + folder.getId() + ")");
            }

            System.out.println("\n📁 Demonstrating builder pattern with other parameters...");
            
            // Example with multiple parameters including the new single_level
            ListFoldersQueryParams detailedQueryParams = new ListFoldersQueryParams.Builder()
                .singleLevel(false)  // Explicitly set to false for multi-level
                .limit(10)
                .select("id,name,parent_id,unread_count")
                .build();

            ListResponse<Folder> detailedFolders = nylasClient.folders().list(grantId, detailedQueryParams);
            System.out.println("Found " + detailedFolders.getData().size() + " folders with detailed info:");
            for (Folder folder : detailedFolders.getData()) {
                System.out.println("  - " + folder.getName());
                System.out.println("    ID: " + folder.getId());
                System.out.println("    Unread Count: " + (folder.getUnreadCount() != null ? folder.getUnreadCount() : 0));
                if (folder.getParentId() != null) {
                    System.out.println("    Parent ID: " + folder.getParentId());
                }
                System.out.println();
            }

        } catch (Exception exception) {
            System.out.println("❌ Error listing folders: " + exception.getMessage());
            System.out.println("Note: The single_level parameter is Microsoft-specific and may not work with other providers.");
        }
    }
} 