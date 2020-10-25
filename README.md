# POC RSocket

## Getting started

```bash
# build
mvn package
```

## Running vanilla-server

The vanilla-server is a project using only RSocket library, no framework.

```bash
java -jar application/vanilla-server/target/vanilla-server-jar-with-dependencies.jar
```

## Running vanilla-client

Several clients to showcase the RSocket models:

```bash
# fire & forget
java -cp client/vanilla-client/target/vanilla-client-jar-with-dependencies.jar \
    lin.louis.poc.rsocket.client.AddNumberFnFClient

# request/response
java -cp client/vanilla-client/target/vanilla-client-jar-with-dependencies.jar \
    lin.louis.poc.rsocket.client.SayHelloClient

# request / stream
java -cp client/vanilla-client/target/vanilla-client-jar-with-dependencies.jar \
    lin.louis.poc.rsocket.client.AddNumberStreamClient

# request / channel
java -cp client/vanilla-client/target/vanilla-client-jar-with-dependencies.jar \
    lin.louis.poc.rsocket.client.CountEvenNumbersClient
```

## Running spring-boot-app

Using spring framework to check how it works with RSocket:

```bash
java -jar application/spring-boot-app/target/spring-boot-app.jar
```

## Running spring-boot-client

Client using spring framework:

```bash
java -jar client/spring-boot-client/target/spring-boot-client.jar
```

At the time of writing this POC, I did not find anything about supporting request / channel model
in Spring framework unfortunately.

However, routing is seamlessly supported by Spring with its `@MessageMapping` annotation.
