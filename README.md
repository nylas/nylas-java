# Nylas Java SDK
This is the GitHub repository for the Nylas Java SDK and this repo is primarily for anyone who wants to make contributions to the SDK or install it from source. If you are looking to use Java to access the Nylas Email, Calendar, or Contacts API you should refer to our official [Java SDK Quickstart Guide](https://docs.nylas.com/docs/quickstart-java).

The Nylas Communications Platform provides REST APIs for [Email](https://docs.nylas.com/docs/quickstart-email), [Calendar](https://docs.nylas.com/docs/quickstart-calendar), and [Contacts](https://docs.nylas.com/docs/quickstart-contacts), and the Java SDK is the quickest way to build your integration using Java.

Here are some resources to help you get started:

- [Nylas SDK Tutorials](https://docs.nylas.com/docs/tutorials)
- [Get Started with the Nylas Communications Platform](https://docs.nylas.com/docs/getting-started)
- [Sign up for your Nylas developer account.](https://nylas.com/register)
- [Nylas API Reference](https://docs.nylas.com/reference)

If you have a question about the Nylas Communications Platform, please reach out to support@nylas.com to get help.

# Install
**Note:** The Nylas Java SDK requires Java 8 or above.

**Setup via Gradle**: If you're using Gradle, add the following to your dependencies section of build.gradle:

    implementation("com.nylas.sdk:nylas-java-sdk:0.2.0")

**Setup via Maven**: For projects using Maven, add the following to your POM file:

    <dependency>
      <groupId>com.nylas.sdk</groupId>
      <artifactId>nylas-java-sdk</artifactId>
      <version>0.2.0</version>
    </dependency>
    
**Build from source**: To build from source, clone this repo and build the project with Gradle.

    git clone https://github.com/nylas/nylas-java.git && cd nylas-java
    ./gradlew build

This will create a new jar file in the `build/libs` subdirectory.

See Gradle documentation on [Building Java Libraries](https://guides.gradle.org/building-java-libraries/)
or the [Gradle User Manual](https://docs.gradle.org/current/userguide/userguide.html) for more information.

# Usage

To use this SDK, you first need to [sign up for a free Nylas developer account](https://nylas.com/register).

Then, follow our guide to [setup your first app and get your API access keys](https://docs.nylas.com/docs/get-your-developer-api-keys).

For code examples that demonstrate how to use this SDK, take a look at our [Java SDK Quickstart Guide](https://docs.nylas.com/docs/quickstart-java).


# Contributing

Please refer to [Contributing](Contributing.md) for information about how to make contributions to this project. We welcome questions, bug reports, and pull requests.

# License

This project is licensed under the terms of the MIT license. Please refer to [LICENSE](LICENSE) for the full terms. 
