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
docker build --tag sparcs/rabbitmq:latest src/docker/rabbitmq
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

...or in detached mode (i.e., no logs to stdout)
`docker-compose up -d`

# Identifying a Kiosk

The `docker-compose.yml` file sets the following environment variables for each Kiosk:
- `SHOP_ID` is a unique 6-digit number with leading zeroes (e.g., "000001" for shop #1)
- `KIOSK_INDEX` is a unique 2-digit number with leading zeroes that identifies a kiosk in a shop (e.g., "01")

The `axon-demo-kiosk` Web Application expects a `kiosk.id` property to uniquely identify "this" kiosk
The value is set via the Java command line to `${SHOP_ID}-${KIOSK_INDEX}` in the ENTRYPOINT of the Dockerfile
The internal web server port of 8080 is exposed to the docker host as port `30000 + ((${SHOP_ID} * 10) + ${KIOSK_INDEX})`
e.g., SHOP_ID "000001" and KIOSK_INDEX "01" becomes port 30011

# Start the Kiosk Web UI

Container **Kiosk1_1** is Shop #1, Index #1 at [http://localhost:30011](http://localhost:30011)
Container **Kiosk1_2** is Shop #1, Index #2 at [http://localhost:30012](http://localhost:30012)

You'll be presented with a number of Events from the "Tiddly-Winks World Championship 2017" Competition.

- Insert some cash using the dropdown menu in the top right
- Current balance is shown to the left of the dropdown
- Make your selection(s) by clicking the buttons
- Click again to deselect 

Log messages are appended to the `axondemo_kiosk` console (`docker logs kiosk1_1` if you're detached)

Events are stored in an in-memory H2 database.  You can examine the database using the H2 web console on the `h2-console` context path (e.g., [http://localhost:30011/h2-console](http://localhost:30011/h2-console) for **Kiosk1_1**)

The H2 connection info is in the `axon-demo-kiosk` module's `application.yml` (under `spring.datasource`)

```
url: jdbc:h2:mem:db;MODE=Oracle;DB_CLOSE_ON_EXIT=FALSE
username: sa
password:
```

Also in the `axon-demo-kiosk` module are some useful SQL scripts - look in `src/main/sql`

This one will show published events:

```
SELECT
   E.TIME_STAMP
,   E.TYPE AS AggregateType
,   E.AGGREGATE_IDENTIFIER AS AggregateId
,   RIGHT(E.PAYLOAD_TYPE, LENGTH(E.PAYLOAD_TYPE)-11) AS EventName
,   UTF8TOSTRING(E.PAYLOAD) AS Payload
FROM DOMAIN_EVENT_ENTRY E
ORDER BY
   E.TYPE
,   E.AGGREGATE_IDENTIFIER
,   E.SEQUENCE_NUMBER
```

# Sending Commands to a Kiosk from the Counter

## What part does RabbitMQ play?

Counter and Kiosk applications running in the same shop (i.e., `counter.shop.id` and `kiosk.shop.id` have the same value), connect to the same Rabbit MQ Exchange.  The Exchange is 'direct' and is called `SHOP-xxx` (where xxx is the Shop Id).  If the Exchange doesn't exist, then it is created when any Kiosk or Counter application within the Shop is started.

When a Kiosk application starts, it creates a non-durable, auto-delete Queue called `KIOSK-xxx` (where xxx is the Kiosk Id).  The queue is bound to the Shop Exchange.  The routing key that tells the Exchange which queue to deliver a specific message to is the Kiosk Id.

## Walkthrough of the setup

- Stop the docker compose if it's running (`docker-compose down`)
- Bring up the RabbitMQ server in detached mode (`docker-compose up -d rabbitmq`)
- Open the RabbitMQ management web console at [http://localhost:15672](http://localhost:15672) and log in with User Id `guest` and password `guest`
- On the Overview tab, note that there are no Connections, no Channels, 8 Exchanges (the default set), no Queues and no Consumers
- Bring up the Counter Application for Shop #1 (`docker-compose up -d counter1`)
- After a short wait you'll notice that the Overview tab reports 1 Connection, 1 Channel and 9 Exchanges (still no Queues or Consumers)
  - Click the **Exchanges** tab (or the button stating **Exchanges: 9**) and you'll see an Exchange named `SHOP-000001` in the list
  - Click the `SHOP-000001` label to see details of the Exchange - its direct, and durable
- Stop the Counter Application (`docker-compose stop counter1`) and observe that the Connection and Channel go back to zero but the durable Exchange remains
- Start the Counter Application for Shop #1 back up again
- Bring up the Application for Kiosk #1 in Shop #1 (`docker-compose up -d kiosk1_1`)
- After another short wait you'll notice that the Overview tab now reports 2 Connections, 2 Channels, a Queue and a Consumer
  - Click the **Queues** tab (or the button stating **Queues: 1**) to see the `KIOSK-000001-01` queue
  - Click the `KIOSK-000001-01` label to see details of the Queue - its configured to auto-delete, and there is 1 Consumer (the Kiosk app)
  - Click the **Get Message(s)** button and a **Queue is empty** message is displayed
- Stop the Kiosk Application (`docker-compose stop kiosk1_1`) and observe that the Queue and Consumer goes back to zero due to auto-delete
- Start the Kiosk Application up again, and add some money using the Web UI at [http://localhost:30011](http://localhost:30011)
- We're going to reset the Kiosk (which will set the balance back to zero) using the Counter Application
  - `curl -X POST localhost:30010/kiosk/000001-01/reset?reason=Maintainance`
  - Note that the Balance in the Web UI is now zero
  - Examine the _Message Rates_ information on the `KIOSK-000001-01` Queue page [http://localhost:15672/#/queues/%2F/KIOSK-000001-01](http://localhost:15672/#/queues/%2F/KIOSK-000001-01) to see the command message being processed - it's easier to see if you dispatch lots of Reset commands: `for i in {1..100}; do curl -s -X POST localhost:30010/kiosk/000001-01/reset?reason=Maintainance; done`
  - Alternatively, you can enable a trace on the queue to capture the messages are they are added to the queue
  - Click the **Admin** tab
  - Click **Tracing** on the right-hand pane
  - In the **Add a new trace** section, enter **All** for the name and then Click the **Add trace** button
  - `curl` a single reset request
  - Click the **All.log** link under **Trace log files** to open the log
  - You'll see two entries, one for the publication of the message (by the Counter application) and one for the reciept (by the Kiosk).  Also note the Exchange, Queue, Routing Key and Payload of the message.
- Execute `docker-compose up` to bring up two Counter Application (one in Shop #1 and one in Shop #2), and three Kiosk Applications (Kiosk #1 & #2 in Shop #1 and Kiosk #1 in Shop #2)

# TODO

More...
