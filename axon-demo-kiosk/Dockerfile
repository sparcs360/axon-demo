FROM sparcs/java8:latest

MAINTAINER Lee Newfeld

ADD target/axon-demo-kiosk-*.jar /app.jar

ENTRYPOINT ["/usr/bin/java", "-jar", "-Dkiosk.shop.id=${SHOP_ID}", "-Dkiosk.index=${KIOSK_INDEX}", "/app.jar"]

EXPOSE 8080
