FROM sparcs/java8:latest

MAINTAINER Lee Newfeld

ADD target/axon-demo-kiosk-0.0.1-SNAPSHOT.jar /app.jar

ENTRYPOINT ["/usr/bin/java", "-jar", "/app.jar"]

EXPOSE 8080
