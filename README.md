# nylas-java

The Nylas Java SDK provides all of the functionality of the Nylas [REST API](https://docs.nylas.com/reference) in an easy-to-use Java API. With the SDK, you can programmatically access an email account (e.g. Gmail, Yahoo, etc.) and perform functionality such as getting messages and listing message threads.

## Requirements
- Java 8 or above
- Nylas account and application client id & secret - https://docs.nylas.com/docs/get-your-developer-api-keys 

## Adding the Nylas Java SDK to your build

For projects using Gradle, add the following to your dependencies section of build.gradle:

    implementation("com.nylas.sdk:nylas-java-sdk:1.X.X")

For project Maven, add the following to your POM file:

    <dependency>
      <groupId>com.nylas.sdk</groupId>
      <artifactId>nylas-java-sdk</artifactId>
      <version>1.X.X</version>
    </dependency>

## Running Examples
- Check out the repo
- Create an example.properties file from the template, and enter your nylas application client id and client secret:
  - `cp src/examples/resources/example.properties.template src/examples/resources/example.properties`
  - Edit `src/examples/resources/example.properties`
- Run `gradlew runExample -Pexample=com.nylas.examples.HostedAuthUrlExample` or any other example from `src/examples/java/com/nylas/examples`
