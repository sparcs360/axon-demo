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

## Base Images (only once)

```
docker build --tag sparcs/java8:latest src/docker/java8
```

## Application Images (each time the code changes)

```
docker-compose build
```

Run this command to remove unused ('dangling') images from previous builds:

```
docker rmi $(docker images -qa -f 'dangling=true')
```

I often just do:

```
mvn clean install && docker-compose build && docker rmi $(docker images -qa -f 'dangling=true')
```

# Start the Docker Containers

`docker-compose up`

# Identifying a Kiosk

The `docker-compose.yml` file sets the following environment variables for each Kiosk:
- `SHOP_ID` is a unique 6-digit number with leading zeroes (e.g., "000001" for shop #1)
- `KIOSK_INDEX` is a unique 2-digit number with leading zeroes that identifies a kiosk in a shop (e.g., "01")

The `axon-demo-kiosk` Web Application expects a `kiosk.id` property to uniquely identify "this" kiosk
The value is set via the Java command line to `${SHOP_ID}-${KIOSK_INDEX}` in the ENTRYPOINT of the Dockerfile
The internal web server port of 8080 is exposed to the docker host as port `30000 + ((${SHOP_ID} * 10) + ${KIOSK_INDEX})`
e.g., SHOP_ID "000001" and KIOSK_INDEX "01" becomes port 30011

# Start the Kiosk Web Applciation

Container **Kiosk1_1** is Shop #1, Index #1 at [http://localhost:30011](http://localhost:30011)
Container **Kiosk1_2** is Shop #1, Index #2 at [http://localhost:30012](http://localhost:30012)

You'll be presented with a number of Events from the "Tiddly-Winks World Championship 2017" Competition.

- Insert some cash using the dropdown menu in the top right
- Current balance is shown to the left of the dropdown
- Make your selection(s) by clicking the buttons
- Click again to deselect 

Log messages are appended to the `axondemo_kiosk` console  

Events are stored in an in-memory H2 database.  You can examine the database using the H2 web console on the `h2-console` context path (e.g., [http://localhost:30011/h2-console](http://localhost:30011/h2-console) for **Kiosk1_1**)

The H2 connection info is in the `axon-demo-kiosk` module's `application.yml` (under `spring.datasource`)
```
url: jdbc:h2:mem:axon3db;MODE=Oracle;DB_CLOSE_ON_EXIT=FALSE
username: sa
password:
```
Also in the `axon-demo-kiosk` module are some useful SQL scripts - look in `src/main/sql`

# TODO

More...
