// TODO::Add back the tasks we took out

plugins {
    kotlin("jvm") version "1.8.21"
    application
}

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

kotlin {
    jvmToolchain(8)
}

dependencies {
    //TODO::Better clean up dependencies
    //////////////////////////////////
    // Public dependencies

    // OkHttp 3 - Http client (without Kotlin dependency of version 4)
    api("com.squareup.okhttp3:okhttp:3.14.5")

    // Moshi JSON library
    implementation("com.squareup.moshi:moshi:1.15.0")
    implementation("com.squareup.moshi:moshi-adapters:1.15.0")

    // SLF4J for logging facade
    implementation("org.slf4j:slf4j-api:2.0.7")

    ///////////////////////////////////
    // Test dependencies

    // jUnit 5 for Testing
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")

    // use log4j logging for tests
    testRuntimeOnly("org.apache.logging.log4j:log4j-slf4j-impl:2.17.0")

    // use wiremock for mocking the nylas server during tests
    testImplementation("com.github.tomakehurst:wiremock-jre8:2.26.3")

    // mockito for dependency mocking
    dependencies {
        testImplementation("org.mockito:mockito-core:3.+")
        testImplementation("org.mockito:mockito-inline:2.13.0")
    }
}
