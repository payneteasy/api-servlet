[![maven](https://maven-badges.herokuapp.com/maven-central/com.payneteasy/api-servlet/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.payneteasy/api-servlet)
[![Build Status](https://travis-ci.org/evsinev/api-servlet.svg?branch=master)](https://travis-ci.org/evsinev/api-servlet)
[![CircleCI](https://circleci.com/gh/evsinev/api-servlet.svg?style=svg)](https://circleci.com/gh/evsinev/api-servlet)

Parse startup parameters
==========================


## Features

* supported types: amount, date, time, text, BCD, bytes
* thread safe (provides immutable container BerTlv)

## Setup with dependency managers

### Maven

```xml
<dependency>
  <groupId>com.payneteasy</groupId>
  <artifactId>api-servlet</artifactId>
  <version>$VERSION</version>
</dependency>
```

### Gradle

```groovy
compile 'com.payneteasy:api-servlet:$VERSION'
```

How to use
------------

Create a service class

```java
public interface ProjectService {

    public User getUser(VoidRequest aRequest) {
        return new User(1, "Test");
    }
}
```

Create servlet mapping

```java
    Server                jetty   = new Server(8080);
    ServletContextHandler context = new ServletContextHandler(jetty, "/api", ServletContextHandler.NO_SESSIONS);

    context.addServlet(new ServletHolder(new JacksonApiServlet<>(projectService::getUser, VoidRequest.class, User.class)), "/user/*");
```


## License

The ApiServlet library is licensed under the Apache License 2.0
