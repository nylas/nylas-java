import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    id("application")
    id("org.jetbrains.kotlin.jvm") version "1.8.21"
}

group = "com.nylas.examples"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal() // For local Nylas SDK development
}

dependencies {
    // Add the Nylas SDK dependency
    implementation(project(":")) // References the main Nylas SDK project
    
    // Core dependencies
    implementation("org.json:json:20230227")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    
    // Test dependencies
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

application {
    // The mainClass will be overridden by the -PmainClass command-line parameter
    mainClass.set(findProperty("mainClass") as String? ?: "com.nylas.examples.NotetakerExample")
}

tasks.test {
    useJUnitPlatform()
}

// Task to list available examples
tasks.register("listExamples") {
    doLast {
        println("Available examples:")
        println("- Java-Notetaker: com.nylas.examples.NotetakerExample")
        println("- Java-Events: com.nylas.examples.EventsExample")
        println("- Java-Messages: com.nylas.examples.MessagesExample")
        println("- Kotlin-Notetaker: com.nylas.examples.KotlinNotetakerExampleKt")
        println("- Kotlin-Messages: com.nylas.examples.KotlinMessagesExampleKt")
        println("\nRun an example with: ./gradlew :examples:run -PmainClass=<example class name>")
    }
}

// Configure source sets for Java and Kotlin
sourceSets {
    main {
        java {
            srcDir("src/main/java")
        }
        kotlin {
            srcDir("src/main/kotlin")
        }
    }
} 