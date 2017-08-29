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

# Build the java components

`mvn clean install`

# Build Docker Images

```
docker build --tag sparcs/java8:latest src/docker/java8
docker build --tag axondemo_kiosk:latest axon-demo-kiosk
```

# Start the Docker Containers

`docker-compose up`

# Start the Kiosk Web Applciation

[http://localhost:8080](http://localhost:8080)

You'll be presented with a number of Events from the "Tiddly-Winks World Championship 2017" Competition.

- Insert some cash using the dropdown menu in the top right
- Current balance is shown to the left of the dropdown
- Make your selection(s) by clicking the buttons
- Click again to deselect 

Log messages are appended to the `axondemo_kiosk` console  

Events are stored in an in-memory H2 database.  You can examine the database using the H2 web console at [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
The H2 connection info is in the `axon-demo-kiosk` module's `application.yml` (under `spring.datasource`)
```
url: jdbc:h2:mem:axon3db;MODE=Oracle;DB_CLOSE_ON_EXIT=FALSE
username: sa
password:
```
Also in the `axon-demo-kiosk` module are some useful SQL scripts - look in `src/main/sql`

# TODO

More...
