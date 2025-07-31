.PHONY: help list java kotlin

# Default target
help:
	@echo "Nylas Java/Kotlin SDK Examples"
	@echo ""
	@echo "Available targets:"
	@echo "  help      - Show this help message"
	@echo "  list      - List available examples"
	@echo "  java-notetaker      - Run the Java Notetaker example"
	@echo "  java-events      - Run the Java Events example"
	@echo "  java-folders     - Run the Java Folders example"
	@echo "  java-large-attachments     - Run the Java Large Attachments example"
	@echo "  kotlin-notetaker    - Run the Kotlin Notetaker example"
	@echo "  kotlin-folders   - Run the Kotlin Folders example"

# List available examples
list:
	@cd .. && ./gradlew :examples:listExamples

# Run the Java example
java-notetaker:
	@cd .. && ./gradlew :examples:run -PmainClass=com.nylas.examples.NotetakerExample

java-messages:
	@cd .. && ./gradlew :examples:run -PmainClass=com.nylas.examples.MessagesExample

java-large-attachments:
	@cd .. && ./gradlew :examples:run -PmainClass=com.nylas.examples.LargeAttachmentsExample	

java-events:
	@cd .. && ./gradlew :examples:run -PmainClass=com.nylas.examples.EventsExample

java-folders:
	@cd .. && ./gradlew :examples:run -PmainClass=com.nylas.examples.FoldersExample

# Run the Kotlin example
kotlin-notetaker:
	@cd .. && ./gradlew :examples:run -PmainClass=com.nylas.examples.KotlinNotetakerExampleKt

kotlin-folders:
	@cd .. && ./gradlew :examples:run -PmainClass=com.nylas.examples.KotlinFoldersExampleKt 