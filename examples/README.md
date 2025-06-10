# Nylas Java/Kotlin SDK Examples

Simple examples demonstrating how to use the Nylas Java/Kotlin SDK.

## Available Examples

### Messages Example

The `MessagesExample` and `KotlinMessagesExample` demonstrate how to use the new message features in the Nylas Java/Kotlin SDK:

- Use the new `MessageFields.INCLUDE_TRACKING_OPTIONS` and `MessageFields.RAW_MIME` enum values
- Access tracking options (opens, thread_replies, links, label) from messages  
- Retrieve raw MIME content for messages
- Use the new `FindMessageQueryParams` to specify fields when finding specific messages
- Compare different field options and their effects on returned data

### Events Example

The `EventsExample` demonstrates how to use the Nylas Java/Kotlin SDK to interact with the Events API:

- List events from a calendar
- Filter events by date range
- Show event details

### Notetaker Example

The `NotetakerExample` demonstrates how to use the Nylas Java/Kotlin SDK to interact with the Notetakers API:

- Invite a Notetaker to a meeting
- List all Notetakers
- Get media from a Notetaker (if available)
- Leave a Notetaker session

### Folders Example

The `FoldersExample` and `KotlinFoldersExample` demonstrate how to use the Nylas Java/Kotlin SDK to interact with the Folders API:

- List all folders with default multi-level hierarchy
- Use the new `single_level` parameter (Microsoft only) to retrieve folders from a single-level hierarchy
- Demonstrate the builder pattern with various query parameters
- Show folder details including parent relationships and unread counts

## Setup

### 1. Environment Setup

Copy the `.env.example` file to `.env` and fill in your credentials:

```bash
cp .env.example .env
```

Edit the `.env` file with your details:
```
# Get your API key from the Nylas Dashboard
NYLAS_API_KEY=your_api_key_here

# Your grant ID (required for message examples)
NYLAS_GRANT_ID=your_grant_id_here

# Add your meeting link (Zoom, Google Meet, or Microsoft Teams) - for Notetaker example
MEETING_LINK=your_meeting_link_here
```

### 2. Running the Examples

#### Option 1: Using Gradle

Run Java Messages example:
```bash
./gradlew :examples:run -PmainClass=com.nylas.examples.MessagesExample
```

Run Kotlin Messages example:
```bash
./gradlew :examples:run -PmainClass=com.nylas.examples.KotlinMessagesExampleKt
```

Run Java Events example:
```bash
./gradlew :examples:run -PmainClass=com.nylas.examples.EventsExample
```

Run Java Notetaker example:
```bash
./gradlew :examples:run -PmainClass=com.nylas.examples.NotetakerExample
```

Run Kotlin Notetaker example:
```bash
./gradlew :examples:run -PmainClass=com.nylas.examples.KotlinNotetakerExampleKt
```

Run Java Folders example:
```bash
./gradlew :examples:run -PmainClass=com.nylas.examples.FoldersExample
```

Run Kotlin Folders example:
```bash
./gradlew :examples:run -PmainClass=com.nylas.examples.KotlinFoldersExampleKt
```

#### Option 2: Using the Makefile

List available examples:
```bash
make list
```

Run the Java Notetaker example:
```bash
make java
```

Run the Kotlin Notetaker example:
```bash
make kotlin-way
```

#### Option 3: From an IDE

1. Open the project in your IDE (IntelliJ IDEA, Eclipse, etc.)
2. Set the required environment variables in your run configuration
3. Run the main method in any of the example files:
   - `MessagesExample.java` (Java - demonstrates new message features)
   - `KotlinMessagesExample.kt` (Kotlin - demonstrates new message features)  
   - `EventsExample.java` (Java - demonstrates events)
   - `FoldersExample.java` (Java - demonstrates folders and single_level parameter)
   - `NotetakerExample.java` (Java - demonstrates notetakers)
   - `KotlinNotetakerExample.kt` (Kotlin - demonstrates notetakers)
   - `KotlinFoldersExample.kt` (Kotlin - demonstrates folders and single_level parameter)

## Project Structure

```
examples/
├── .env.example       # Template for environment variables
├── build.gradle.kts   # Gradle build file for examples
├── Makefile           # Helpful commands for running examples
├── README.md          # This file
└── src/
    └── main/
        ├── java/      # Java examples
        │   └── com/nylas/examples/
        │       ├── MessagesExample.java     # NEW: Message features demo
        │       ├── EventsExample.java       # Events API demo
        │       ├── FoldersExample.java      # NEW: Folders API demo with single_level parameter
        │       └── NotetakerExample.java    # Notetaker API demo
        └── kotlin/    # Kotlin examples
            └── com/nylas/examples/
                ├── KotlinMessagesExample.kt     # NEW: Message features demo
                ├── KotlinFoldersExample.kt      # NEW: Folders API demo with single_level parameter
                └── KotlinNotetakerExample.kt    # Notetaker API demo
```

## Message Features Demonstrated

The Messages examples showcase the following new features added to the Nylas SDK:

1. **New MessageFields enum values:**
   - `MessageFields.INCLUDE_TRACKING_OPTIONS` - Returns tracking options data
   - `MessageFields.RAW_MIME` - Returns raw MIME message content

2. **New Message model properties:**
   - `trackingOptions` - Contains opens, thread_replies, links, and label tracking data
   - `rawMime` - Contains Base64url-encoded raw message data

3. **Enhanced Messages API methods:**
   - `Messages.find()` now accepts `FindMessageQueryParams` to specify which fields to include
   - Both list and find operations support the new field options

## Additional Information

For more information about the Nylas API, refer to the [Nylas API documentation](https://developer.nylas.com/). 