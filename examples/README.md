# Nylas Java/Kotlin SDK Examples

Simple examples demonstrating how to use the Nylas Java/Kotlin SDK.

## Available Examples

### Notetaker Example

The `NotetakerExample` demonstrates how to use the Nylas Java/Kotlin SDK to interact with the Notetakers API:

- Invite a Notetaker to a meeting
- List all Notetakers
- Get media from a Notetaker (if available)
- Leave a Notetaker session

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

# Add your meeting link (Zoom, Google Meet, or Microsoft Teams)
MEETING_LINK=your_meeting_link_here
```

### 2. Running the Examples

#### Option 1: Using Gradle

Run Java example:
```bash
./gradlew :examples:run -PmainClass=com.nylas.examples.NotetakerExample
```

Run Kotlin example:
```bash
./gradlew :examples:run -PmainClass=com.nylas.examples.KotlinNotetakerExampleKt
```

#### Option 2: Using the Makefile

List available examples:
```bash
make list
```

Run the Java example:
```bash
make java
```

Run the Kotlin example:
```bash
make kotlin-way
```

#### Option 3: From an IDE

1. Open the project in your IDE (IntelliJ IDEA, Eclipse, etc.)
2. Set the required environment variables in your run configuration
3. Run the main method in either:
   - `NotetakerExample.java` (Java)
   - `KotlinNotetakerExample.kt` (Kotlin)

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
        │       └── NotetakerExample.java
        └── kotlin/    # Kotlin examples
            └── com/nylas/examples/
                └── KotlinNotetakerExample.kt
```

## Additional Information

For more information about the Nylas API, refer to the [Nylas API documentation](https://developer.nylas.com/). 