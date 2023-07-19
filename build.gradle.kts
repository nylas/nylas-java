plugins {
  kotlin("jvm") version "1.8.21"
  id("org.jmailen.kotlinter") version "3.15.0"
  id("org.jetbrains.dokka") version "1.8.20"
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
  // ////////////////////////////////
  // Public dependencies

  // OkHttp 3 - Http client (without Kotlin dependency of version 4)
  api("com.squareup.okhttp3:okhttp:3.14.5")

  // Moshi JSON library
  implementation("com.squareup.moshi:moshi:1.15.0")
  implementation("com.squareup.moshi:moshi-kotlin:1.15.0")
  implementation("com.squareup.moshi:moshi-adapters:1.15.0")

  // SLF4J for logging facade
  implementation("org.slf4j:slf4j-api:2.0.7")
}

tasks.processResources {
  expand("artifact_version" to project.version)
}

tasks.register<Jar>("uberJar") {
  archiveClassifier.set("uber")

  duplicatesStrategy = DuplicatesStrategy.EXCLUDE

  from(sourceSets.main.get().output)

  dependsOn(configurations.runtimeClasspath)
  from({
    configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
  })
}
