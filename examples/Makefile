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
	@echo "  kotlin-notetaker    - Run the Kotlin Notetaker example"

# List available examples
list:
	@cd .. && ./gradlew :examples:listExamples

# Run the Java example
java-notetaker:
	@cd .. && ./gradlew :examples:run -PmainClass=com.nylas.examples.NotetakerExample

java-events:
	@cd .. && ./gradlew :examples:run -PmainClass=com.nylas.examples.EventsExample

# Run the Kotlin example
kotlin-notetaker:
	@cd .. && ./gradlew :examples:run -PmainClass=com.nylas.examples.KotlinNotetakerExampleKt 