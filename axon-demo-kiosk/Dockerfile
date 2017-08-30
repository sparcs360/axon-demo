FROM sparcs/java8:latest

MAINTAINER Lee Newfeld

ADD target/axon-demo-kiosk-*.jar /app.jar

ENTRYPOINT ["/usr/bin/java", "-jar", "-Dkiosk.id=${SHOP_ID}-${KIOSK_INDEX}", "/app.jar"]

EXPOSE 8080
