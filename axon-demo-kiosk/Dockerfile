FROM sparcs/java8:latest

MAINTAINER Lee Newfeld

ADD target/axon-demo-kiosk-*.jar /app.jar

ENTRYPOINT ["/usr/bin/java", "-jar", "-Deureka.client.service-url.defaultZone=http://${DISCOVERY_SERVER_HOSTNAME}:8761/eureka/", "-Dinfo.shop.id=${SHOP_ID}", "-Dinfo.kiosk-index=${KIOSK_INDEX}", "/app.jar"]

EXPOSE 8080
