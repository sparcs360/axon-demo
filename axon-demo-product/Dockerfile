FROM sparcs/java8:latest

MAINTAINER Lee Newfeld

ADD target/axon-demo-product-*.jar /app.jar

ENTRYPOINT ["/usr/bin/java", "-jar", "-Deureka.instance.instance-id=${INSTANCE_ID}", "/app.jar"]

EXPOSE 8082
