[![Maven Central](https://img.shields.io/maven-central/v/com.payneteasy/api-servlet.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.payneteasy%22%20AND%20a:%22api-servlet%22)
[![Github Actions Status](https://github.com/payneteasy/api-servlet/workflows/Java%20CI/badge.svg)](https://github.com/payneteasy/api-servlet/actions)
[![CircleCI](https://circleci.com/gh/payneteasy/api-servlet.svg?style=svg)](https://circleci.com/gh/payneteasy/api-servlet)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.payneteasy%3Aapi-servlet&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.payneteasy%3Aapi-servlet)

Simple API Servlet for JSON
==========================


## Features

* supported: jackson, gson

## Setup with dependency managers

### Maven

```xml
<dependency>
  <groupId>com.payneteasy</groupId>
  <artifactId>api-servlet</artifactId>
  <version>1.0-3</version>
</dependency>
```

### Gradle

```groovy
compile 'com.payneteasy:api-servlet:1.0-3'
```

How to use
------------

Create a service class

```java
public class HelloServiceSample {

    public ResponseMessageSample sayHello(RequestMessageSample aName) {
        ResponseMessageSample response = new ResponseMessageSample();
        response.text = "Hello " + aName.name;
        return response;
    }
}
```

### Create servlet mapping with Jackson

```java
    Server                jetty   = new Server(8080);
    ServletContextHandler context = new ServletContextHandler(jetty, "/api", ServletContextHandler.NO_SESSIONS);
    ObjectMapper          mapper  = new ObjectMapper();
    HelloServiceSample service    = new HelloServiceSample();
    
    context.addServlet(new ServletHolder(new JacksonApiServlet<>(service::sayHello, RequestMessageSample.class, ResponseMessageSample.class, mapper)), "/user/*");
```

### Create with Gson

```java
    Server                  jetty   = new Server(8080);
    ServletContextHandler   context = new ServletContextHandler(jetty, "/api", ServletContextHandler.NO_SESSIONS);
    Gson                    gson    = new GsonBuilder().setPrettyPrinting().create();
    GsonJettyContextHandler handler = new GsonJettyContextHandler(context, gson);

    HelloServiceSample      service = new HelloServiceSample();

    handler.addApi("/user/*", service::sayHello, RequestMessageSample.class);
```


## License

The ApiServlet library is licensed under the Apache License 2.0
