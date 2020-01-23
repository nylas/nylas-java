# Nylas Java SDK

The Nylas Java SDK provides all of the functionality of the Nylas [REST API](https://docs.nylas.com/reference) in an easy-to-use Java API. With the SDK, you can programmatically access an email account (e.g. Gmail, Yahoo, etc.) and perform functionality such as getting messages and listing message threads.

## Requirements
- Java 8 or above
- [Nylas developer account and application](https://docs.nylas.com/docs/get-your-developer-api-keys) 

## Adding the SDK to your project

For projects using Gradle, add the following to your dependencies section of build.gradle:

    implementation("com.nylas.sdk:nylas-java-sdk:1.X.X")

For projects using Maven, add the following to your POM file:

    <dependency>
      <groupId>com.nylas.sdk</groupId>
      <artifactId>nylas-java-sdk</artifactId>
      <version>1.X.X</version>
    </dependency>

## Running examples
- Check out the repo
- Create an example.properties file from the template, and enter your nylas application client id and client secret:
  - `cp src/examples/resources/example.properties.template src/examples/resources/example.properties`
  - Edit `src/examples/resources/example.properties`
- Run `gradlew runExample -Pexample=com.nylas.examples.HostedAuthUrlExample` or any other example from `src/examples/java/com/nylas/examples`

## Building the SDK from source

The Nylas Java SDK uses Gradle as its build tool.
If you modify the source and want to build it yourself, you can run

    ./gradlew build

This will create a new jar file in the `build/libs` subdirectory.

See Gradle documentation on [Building Java Libraries](https://guides.gradle.org/building-java-libraries/)
or the [Gradle User Manual](https://docs.gradle.org/current/userguide/userguide.html) for more information.
