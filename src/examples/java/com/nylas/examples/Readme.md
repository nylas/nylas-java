# Nylas Java SDK Examples
The subdirectories found here contain example code that demonstrate how to use the Nylas Java SDK to take advantage of the features of the Nylas Communications Platform. If you're new here, please take a look at our [Java SDK quickstart guide](https://docs.nylas.com/docs/quickstart-java), it's the best resource to learn how to get up and running.

These are the examples you will find here: 

* [Hosted Authentication](./hostedAuth) - Learn how to connect user accounts to Nylas via Hosted Authentication. This is the quickest and easiest way to authenticate user accounts.
* [Native Authentication](./nativeAuth) - Learn how to connect user accounts to Nylas via Native Authentication. This is for developers who want to whitelabel the login experience.
* [Webhooks](./webhooks) - Need to receive information about changes to email, calendars, or contacts? This example demonstrates how to receive webhook notifications from Nylas
* [Other Examples](./other) - Basic examples that demonstrate the Nylas Email, Calendar, and Contacts API endpoints. Take a look at our [Java SDK quickstart guide](https://docs.nylas.com/docs/quickstart-java) to learn more.

## How to Run the Examples
Clone this repo to your machine

`git clone https://github.com/nylas/nylas-java.git && cd nylas-java`

Create an example.properties file from the [provided template](https://github.com/nylas/nylas-java/blob/master/src/examples/resources/example.properties.template), and modify the file to include the values you need for the examples you want to run. At the very least, you need to set `nylas.client.id` and `nylas.client.secret` to match your Nylas App.

`cp src/examples/resources/example.properties.template src/examples/resources/example.properties`	

	
Here is an example of how to run the Hosted Authentication example:

`./gradlew runExample -Pexample=com.nylas.examples.hostedAuth.HostedAuthUrlExample` 

Replace `hostedAuth.HostedAuthUrlExample` with any of the examples found in the subdirectories here.