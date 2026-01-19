plugins {
  kotlin("jvm") version "1.8.21"
  id("org.jmailen.kotlinter") version "3.15.0"
  id("org.jetbrains.dokka") version "1.8.20"
  `maven-publish`
  `java-library`
  application
  signing
  jacoco
  id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
}

repositories {
  mavenCentral()
}

java {
  withJavadocJar()
  withSourcesJar()
}

kotlin {
  jvmToolchain(8)
}

dependencies {
  // ////////////////////////////////
  // Public dependencies

  // OkHttp 4 - Http client
  api("com.squareup.okhttp3:okhttp:4.12.0")

  // Moshi JSON library
  implementation("com.squareup.moshi:moshi:1.15.0")
  implementation("com.squareup.moshi:moshi-kotlin:1.15.0")
  implementation("com.squareup.moshi:moshi-adapters:1.15.0")

  // SLF4J for logging facade
  implementation("org.slf4j:slf4j-api:2.0.7")

  // Render dokka from the Java perspective
  dokkaPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.8.20")

  // Test dependencies
  testImplementation(kotlin("test"))
  testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
  testImplementation("org.mockito:mockito-inline:4.11.0")
  testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
  implementation(kotlin("stdlib-jdk8"))
}

tasks.jacocoTestReport {
  classDirectories.setFrom(
    files(
      classDirectories.files.map {
        fileTree(it) {
          exclude(
            "com/nylas/models/**",
            "com/nylas/interceptors/**",
          )
        }
      },
    ),
  )
  reports {
    xml.required.set(true)
    csv.required.set(true)
    html.required.set(true)
  }
}

tasks.test {
  finalizedBy(tasks.jacocoTestReport)
  useJUnitPlatform()
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
}
nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://ossrh-staging-api.central.sonatype.com/service/local/"))
            snapshotRepositoryUrl.set(uri("https://central.sonatype.com/repository/maven-snapshots/"))
        }
    }
}

signing {
  sign(publishing.publications["mavenJava"])
}
