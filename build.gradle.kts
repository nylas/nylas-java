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

configurations {
    examplesImplementation.extendsFrom implementation
    examplesRuntimeOnly.extendsFrom runtimeOnly
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}

dependencies {
    //////////////////////////////////
    // Public dependencies
    
    // OkHttp 3 - Http client (without Kotlin dependency of version 4)
    api('com.squareup.okhttp3:okhttp:3.14.5')
    
    // Moshi JSON library
    implementation('com.squareup.moshi:moshi:1.9.2')
    implementation('com.squareup.moshi:moshi-adapters:1.9.2')
    
    // SLF4J for logging facade
    implementation('org.slf4j:slf4j-api:1.7.30')

    // Websocket dependency
    api('org.java-websocket:Java-WebSocket:1.5.3')

    ///////////////////////////////////
    // Test dependencies
    
    // jUnit 5 for Testing
    testImplementation('org.junit.jupiter:junit-jupiter-api:5.6.2')
    testRuntimeOnly('org.junit.jupiter:junit-jupiter-engine:5.6.2')
    
    // use log4j logging for tests
    testRuntimeOnly('org.apache.logging.log4j:log4j-slf4j-impl:2.17.0')
    
    // use wiremock for mocking the nylas server during tests
    testImplementation('com.github.tomakehurst:wiremock-jre8:2.26.3')

    // mockito for dependency mocking
    dependencies {
        testImplementation "org.mockito:mockito-core:3.+"
        testImplementation 'org.mockito:mockito-inline:2.13.0'
    }

    
    ///////////////////////////////////
    // Examples dependencies
    
    // use log4j2 logging for examples
    examplesImplementation('org.apache.logging.log4j:log4j-api:2.17.0')
    examplesImplementation('org.apache.logging.log4j:log4j-core:2.17.0')
    examplesImplementation('org.apache.logging.log4j:log4j-slf4j-impl:2.17.0')
    
    // Guava for examples since it makes all Java better and we don't need to worry about
    // conflicting dependencies downstream for examples
    examplesImplementation('com.google.guava:guava:28.2-jre')
    
    // jetty http server for examples that use callbacks 
    examplesImplementation('org.eclipse.jetty:jetty-server:9.4.28.v20200408')
    examplesImplementation('org.eclipse.jetty:jetty-servlet:9.4.28.v20200408')
    
    // freemarker template library for serving basic html in some examples
    examplesImplementation('org.freemarker:freemarker:2.3.29')
    
}

test {
    useJUnitPlatform()
    testLogging {
        events "passed", "skipped", "failed", "standardError"
    }
    afterSuite { desc, result ->
        if (!desc.parent) { // will match the outermost suite
            def output = "Results: ${result.resultType} (${result.testCount} tests, ${result.successfulTestCount} passed, ${result.failedTestCount} failed, ${result.skippedTestCount} skipped)"
            def startItem = '|  ', endItem = '  |'
            def repeatLength = startItem.length() + output.length() + endItem.length()
            println('\n' + ('-' * repeatLength) + '\n' + startItem + output + endItem + '\n' + ('-' * repeatLength))
        }
    }
}

task runExample(type: JavaExec) {
    classpath = sourceSets.examples.runtimeClasspath
    main = project.hasProperty("example") ? project.getProperty("example") : "com.nylas.examples.Examples"
}

def getGitCommitHash = { ->
    def buffer = new ByteArrayOutputStream()
    exec {
        commandLine 'git', 'rev-parse', 'HEAD'
        standardOutput = buffer
    }
    return buffer.toString().trim()
}

task createProperties(dependsOn: processResources) {
    doLast {
        mkdir "$buildDir/resources/main"
        new File("$buildDir/resources/main/build.properties").withWriter { w ->
            Properties p = new Properties()
            p['version'] = project.version.toString()
            p['commit.hash'] = getGitCommitHash()
            p['build.timestamp'] = Instant.now().toString()
            p.store w, null
        }
    }
}

classes {
    dependsOn createProperties
}

task sourceJar(type: Jar) {
    classifier "sources"
    from sourceSets.main.allJava
}

task javadocJar(type: Jar, dependsOn: javadoc) {
    classifier "javadoc"
    from javadoc.destinationDir
}

javadoc.options.addStringOption('Xdoclint:none', '-quiet')

artifacts {
    archives jar
    archives sourceJar
    archives javadocJar
}

publishing {
    publications {
        mavenJava(MavenPublication) {
            from components.java
            artifact sourceJar
            artifact javadocJar
            pom {
                name = "Nylas Java SDK"
                description = "Java SDK for the Nylas Communication Platform"
                url = "https://github.com/nylas/nylas-java"
                licenses {
                    license {
                        name = "MIT"
                        url = "http://www.opensource.org/licenses/mit-license.php"
                    }
                }
                developers {
                    developer {
                        id = "ddlatham"
                        name = "David Latham"
                        organization = "Nylas"
                        organizationUrl = "https://www.nylas.com/"
                    }
                    developer {
                        id = "mrashed-dev"
                        name = "Mostafa Rashed"
                        organization = "Nylas"
                        organizationUrl = "https://www.nylas.com/"
                    }
                }
                scm {
                    url = "https://github.com/nylas/nylas-java"
                    connection = "scm:git:https://github.com/nylas/nylas-java.git"
                    developerConnection = "scm:git:https://github.com/nylas/nylas-java.git"
                }
            }
        }
    }
    repositories {
        maven {
            def releasesRepoUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2"
            def snapshotsRepoUrl = "https://oss.sonatype.org/content/repositories/snapshots"
            url = version.endsWith('SNAPSHOT') ? snapshotsRepoUrl : releasesRepoUrl
            credentials {
                username = "$ossrhUser"
                password = "$ossrhPassword"
            }
        }
    }
}

signing {
    sign publishing.publications.mavenJava
}

// workaround for https://github.com/eclipse/buildship/issues/476
eclipse {
    classpath {
        plusConfigurations.add configurations.examplesCompileClasspath
    }
}
