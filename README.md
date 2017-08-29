# About

Yet another example of an enterprise application architecture built upon the [Axon 3 Framework](http://www.axonframework.org/)... with a little help from:

- Back-end
  - Spring Framework
  - Spring Boot
  - Spring Data (JPA)
  - Spring Messaging
  - Docker
- Databases
  - H2
  - MySql
- Messaging
  - RabbitMQ
- Front-end
  - Bootstrap
  - Angular.JS (v1)
  - ngStomp

# Build Base Docker Images

## sparcs/java8

```
cd src/docker/java8
docker build --tag sparcs/java8:latest .
```

# Build the java components

`mvn clean install`

# Build Docker Images

# axon-demo-kiosk

`docker build --tag axondemo_kiosk:latest axon-demo-kiosk`

# Start the Docker Containers

`docker-compose up`

# Examine the EventStore on the Kiosk

[http://localhost:8080/h2-console](http://localhost:8080/h2-console)

# TODO

More...
