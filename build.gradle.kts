plugins {
  kotlin("jvm") version "1.8.21"
  id("org.jmailen.kotlinter") version "3.15.0"
  id("org.jetbrains.dokka") version "1.8.20"
  `maven-publish`
  `java-library`
  application
  signing
}

repositories {
  mavenCentral()
}

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = JavaVersion.VERSION_1_8
  withJavadocJar()
  withSourcesJar()
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

  // Render dokka from the Java perspective
  dokkaPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.8.20")
}

tasks.processResources {
  expand("artifact_version" to project.version)
}

tasks.register<Jar>("sourceJar") {
  archiveClassifier.set("sources")
  from(sourceSets.main.get().allSource)
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

publishing {
  publications {
    create<MavenPublication>("mavenJava") {
      from(components["java"])
      pom {
        name.set("Nylas SDK for Kotlin & Java")
        description.set("Kotlin & Java SDK for the Nylas Communication Platform")
        url.set("https://github.com/nylas/nylas-java")
        licenses {
          license {
            name.set("MIT")
            url.set("http://www.opensource.org/licenses/mit-license.php")
          }
        }
        developers {
          developer {
            id.set("mrashed-dev")
            name.set("Mostafa Rashed")
            organization.set("Nylas")
            organizationUrl.set("https://www.nylas.com/")
          }
        }
        scm {
          url.set("https://github.com/nylas/nylas-java")
          connection.set("scm:git:https://github.com/nylas/nylas-java.git")
          developerConnection.set("scm:git:https://github.com/nylas/nylas-java.git")
        }
      }
    }
  }
  repositories {
    maven {
      name = "ossrh"
      url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2")
      credentials(PasswordCredentials::class)
    }
  }
}

signing {
  sign(publishing.publications["mavenJava"])
}
