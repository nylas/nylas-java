.PHONY: help list java kotlin

# Default target
help:
	@echo "Nylas Java/Kotlin SDK Examples"
	@echo ""
	@echo "Available targets:"
	@echo "  help      - Show this help message"
	@echo "  list      - List available examples"
	@echo "  java      - Run the Java Notetaker example"
	@echo "  kotlin    - Run the Kotlin Notetaker example"

# List available examples
list:
	@cd .. && ./gradlew :examples:listExamples

# Run the Java example
java:
	@cd .. && ./gradlew :examples:run -PmainClass=com.nylas.examples.NotetakerExample

# Run the Kotlin example
kotlin:
	@cd .. && ./gradlew :examples:run -PmainClass=com.nylas.examples.KotlinNotetakerExampleKt 