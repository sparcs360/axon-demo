FROM sparcs/java8:latest

MAINTAINER Lee Newfeld

ADD target/axon-demo-discovery-*.jar /app.jar

ENTRYPOINT ["/usr/bin/java", "-jar", "/app.jar"]

EXPOSE 8761
