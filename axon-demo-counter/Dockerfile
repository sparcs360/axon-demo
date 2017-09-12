FROM sparcs/java8:latest

MAINTAINER Lee Newfeld

ADD target/axon-demo-counter-*.jar /app.jar

ENTRYPOINT ["/usr/bin/java", "-jar", "-Dinfo.shop.id=${SHOP_ID}", "/app.jar"]

EXPOSE 8081
