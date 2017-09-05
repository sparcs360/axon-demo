# Introduction

This Project is a vehicle for evaluating various tools & technologies that I can use to build Enterprise Applications (my job).  My primary goal is to explore the [Command Query Responsibility Segregation](https://martinfowler.com/bliki/CQRS.html) (CQRS) and [Event Sourcing](https://martinfowler.com/eaaDev/EventSourcing.html) (ES) Architectural Patterns - more specifically, the capabilities of the [Axon 3 Framework](http://www.axonframework.org/).

## About the Project

The Project delivers the software components supporting a hypothetical Business that sells it's Products directly to Customers in it's Estate of Retail Shops.

The Estate is made up of an arbitrary number of Shops, each Shop contains:
- Several Web Browser based 'Kiosk' Applications that allow Customers to buy Products.
- A Web Browser based 'Counter' Application, used exclusively by members of Staff, which allows the Kiosks to be monitored and maintained.

All machines that make up the IT infrastructure are connected to the same network.
- All Applications in the estate connect to a single RabbitMQ server.
- Two seperate topologies of Exchanges and Queues allow:
  - Commands to be routed to specific Applications
  - Events to be routed to any listening Applications

Briefly, the design is:
- Each Kiosk and Counter Web UI communicates with it's Spring Web Application using [STOMP](https://en.wikipedia.org/wiki/Streaming_Text_Oriented_Messaging_Protocol) over Web Sockets.
  - Requests are sent to the Web Application via SEND commands (e.g., 'deposit cash')
  - Responses are received via SUBSCRIBE commands (e.g., 'balance updated')
- The Kiosk's Web Application converts the STOMP requests to Commands and dispatches them on it's local Axon `CommandBus`
- Axon routes the Command to a specific `@CommandHandler` method of a specific instance of an `@Aggregate` class.
- The method validates & processes the Command - if all is well, it publishes Events to the Axon `EventBus`
- `@EventSourcingHandler` methods change the state of the `@Aggregate` instance
- Published Events are stored in the Kiosk's `EventStore` (an in-memory H2 database), they are also published to a RabbitMQ Exchange.
- The Counter's Web Application has `@EventHandler` methods that are listening for specific events 
- The Counter Web Application sends Commands to a particular Kiosk (in the same Shop) via messages to a direct Exchange with a specific routing-key

TODO: More

## Tools & Technologies Used

- Docker & Docker Compose
- Spring Framework
  - Spring Boot
  - Spring Data (JPA)
  - Spring Messaging ([this example](https://spring.io/guides/gs/messaging-stomp-websocket/))
- Databases
  - H2
- Messaging
  - RabbitMQ
- Other
  - Lombok
- Front-end
  - Bootstrap
  - Angular.JS (v1)
  - ngStomp

## References

I found the information below invaluable while learning about these technologies: 
- [The excellent Axon 3 'Reference Guide' and 'JavaDocs'](http://www.axonframework.org/download/)
- [Ben Wilcock's microservice-sampler](https://github.com/benwilcock/microservice-sampler)
- [Trifork's AxonBank demo](https://github.com/AxonFramework/AxonBank)

# Building and Running the Project

## Pre-requisites

- Java 8 JRE
- Maven
- Docker
- Docker Compose
- Lombok

TODO: More...

## Build the Java Components

Maven is the build & dependency management tools.

Execute `mvn clean install` from the root of your local git repo to build the Java Components.

## Build Docker Images

### Base Images (only once)

```
docker build --tag sparcs/java8:latest src/docker/java8
docker build --tag sparcs/rabbitmq:latest src/docker/rabbitmq
```

### Application Images (each time the code changes)

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

## Start the Docker Containers

`docker-compose up`

...or in detached mode (i.e., no logs to stdout)
`docker-compose up -d`

# Kiosk Web UI

## Identifying a Kiosk

The `docker-compose.yml` file sets the following environment variables for each Kiosk:
- `SHOP_ID` is a unique 6-digit number with leading zeroes (e.g., "000001" for shop #1)
- `KIOSK_INDEX` is a unique 2-digit number with leading zeroes that identifies a kiosk in a shop (e.g., "01")

The `axon-demo-kiosk` Web Application expects a `kiosk.shop.id` property to uniquely identify which shop "this" kiosk is in, and a `kiosk.index` property to unique identify it's index.

The properties are set via the Java command line expressed in the ENTRYPOINT of the Dockerfile.

Docker container names follow the format kiosk`${SHOP_ID}`_`${KIOSK_INDEX}`

Similarly, the internal web server port of 8080 is exposed to the docker host as port `30000 + ((${SHOP_ID} * 10) + ${KIOSK_INDEX})`, e.g., **SHOP_ID** "000001" and **KIOSK_INDEX** "01" becomes port 30011.

The following Kiosks are defined in the docker-compose file:

|Shop Id|Kiosk Index|Container Name|URL|
|---|---|---|---|
|000001|01|Kiosk1_1|[http://localhost:30011](http://localhost:30011)|
|000001|02|Kiosk1_2|[http://localhost:30012](http://localhost:30011)|
|000002|01|Kiosk2_1|[http://localhost:30021](http://localhost:30011)|

## Using the Kiosk Web UI

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

Counter and Kiosk applications connect to the Rabbit MQ Server defined by the `counter.amqp.host-name` and `kiosk.amqp.host-name` properties respectively.

When a Counter starts, it:
- Subscribes to Events published by the Kiosks in the same shop, by:
  - Creating a 'durable' Exchange (if it doesn't already exist), who's name is defined in the `counter.amqp.kiosk-events-in.exchange-name` property.
  - Creating a 'non-durable', 'auto-delete' queue who's name is defined by the `counter.amqp.kiosk-events-in.queue-name` property
  - Binding the Queue to the Exchange
- Dispatches Commands intended for a specific Kiosk in the same shop, by:
  - Creating a 'durable' Exchange (if it doesn't already exist), who's name is defined in the `counter.amqp.kiosk-commands-out.exchange-name` property
  - The 'routing key' is set to the Id of the target Kiosk

When a Kiosk starts, it:
- Publishes Events to potential subscribers, by:
  - Creating a 'durable' Exchange (if it doesn't already exist), who's name is defined in the `kiosk.amqp.kiosk-events-out.exchange-name` property.
- Listens for Commands sent to it, by:
  - Creating a 'durable' Exchange (if it doesn't already exist), who's name is defined in the `counter.amqp.kiosk-commands-in.exchange-name` property.
  - Creating a 'non-durable', 'auto-delete' queue who's name is defined by the `counter.amqp.kiosk-commands-in.queue-name` property
  - Binding the Queue to the Exchange with a 'routing key' of `${kiosk.shop.id}-${kiosk.index}`

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
